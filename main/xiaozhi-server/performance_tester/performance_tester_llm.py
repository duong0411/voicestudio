import asyncio
import logging
import os
import statistics
import time
import concurrent.futures
from typing import Dict, Optional
import yaml
import aiohttp
from tabulate import tabulate
from core.utils.llm import create_instance as create_llm_instance
from config.settings import load_config


logging.basicConfig(level=logging.WARNING)

description = ""


class LLMPerformanceTester:
    def __init__(self):
        self.config = load_config()

        self.system_prompt = self._load_system_prompt()
        self.test_sentences = self.config.get("module_test", {}).get(
            "test_sentences",
            [
                "，，？",
                "？",
                "，？",
                "？？",
                "8",
            ],
        )
        self.results = {}

    def _load_system_prompt(self) -> str:
        """"""
        try:
            prompt_file = os.path.join(
                os.path.dirname(os.path.dirname(__file__)), self.config.get("prompt_template", "agent-base-prompt.txt")
            )
            with open(prompt_file, "r", encoding="utf-8") as f:
                content = f.read()

                content = content.replace(
                    "{{base_prompt}}", "，AI"
                )
                content = content.replace(
                    "{{emojiList}}", "😀,😃,😄,😁,😊,😍,🤔,😮,😱,😢,😭,😴,😵,🤗,🙄"
                )
                content = content.replace("{{current_time}}", "2024817 12:30:45")
                content = content.replace("{{today_date}}", "2024817")
                content = content.replace("{{today_weekday}}", "")
                content = content.replace("{{lunar_date}}", "")
                content = content.replace("{{local_address}}", "")
                content = content.replace("{{weather_info}}", "，25-32℃")
                return content
        except Exception as e:
            print(f": {e}")
            return "，AI。。"

    def _collect_response_sync(self, llm, messages, llm_name, sentence_start):
        """"""
        chunks = []
        first_token_received = False
        first_token_time = None

        try:
            response_generator = llm.response("perf_test", messages)
            chunk_count = 0
            for chunk in response_generator:
                chunk_count += 1

                if chunk_count % 10 == 0:

                    import threading

                    if (
                        threading.current_thread().ident
                        != threading.main_thread().ident
                    ):

                        pass


                chunk_str = str(chunk)
                if (
                    "" in chunk_str
                    or "" in chunk_str
                    or "502" in chunk_str.lower()
                ):
                    error_msg = chunk_str.lower()
                    print(f"{llm_name} : {error_msg}")

                    raise Exception(chunk_str)

                if not first_token_received and chunk.strip() != "":
                    first_token_time = time.time() - sentence_start
                    first_token_received = True
                    print(f"{llm_name}  Token: {first_token_time:.3f}s")
                chunks.append(chunk)
        except Exception as e:

            error_msg = str(e).lower()
            print(f"{llm_name} : {error_msg}")

            if (
                "502" in error_msg
                or "bad gateway" in error_msg
                or "error code: 502" in error_msg
                or "" in str(e)
                or "" in str(e)
            ):
                raise e

            return chunks, first_token_time

        return chunks, first_token_time

    async def _check_ollama_service(self, base_url: str, model_name: str) -> bool:
        """ Ollama """
        async with aiohttp.ClientSession() as session:
            try:
                async with session.get(f"{base_url}/api/version") as response:
                    if response.status != 200:
                        print(f"Ollama : {base_url}")
                        return False
                async with session.get(f"{base_url}/api/tags") as response:
                    if response.status == 200:
                        data = await response.json()
                        models = data.get("models", [])
                        if not any(model["name"] == model_name for model in models):
                            print(
                                f"Ollama  {model_name} ， `ollama pull {model_name}` "
                            )
                            return False
                    else:
                        print(" Ollama ")
                        return False
                return True
            except Exception as e:
                print(f" Ollama : {str(e)}")
                return False

    async def _test_single_sentence(
        self, llm_name: str, llm, sentence: str
    ) -> Optional[Dict]:
        """"""
        try:
            print(f"{llm_name} : {sentence[:20]}...")
            sentence_start = time.time()
            first_token_received = False
            first_token_time = None


            messages = [
                {"role": "system", "content": self.system_prompt},
                {"role": "user", "content": sentence},
            ]


            try:
                loop = asyncio.get_event_loop()
                with concurrent.futures.ThreadPoolExecutor() as executor:

                    future = executor.submit(
                        self._collect_response_sync,
                        llm,
                        messages,
                        llm_name,
                        sentence_start,
                    )


                    try:
                        response_chunks, first_token_time = await asyncio.wait_for(
                            asyncio.wrap_future(future), timeout=10.0
                        )
                    except asyncio.TimeoutError:
                        print(f"{llm_name} （10），")

                        future.cancel()

                        try:
                            await asyncio.wait_for(
                                asyncio.wrap_future(future), timeout=1.0
                            )
                        except (
                            asyncio.TimeoutError,
                            concurrent.futures.CancelledError,
                            Exception,
                        ):

                            pass
                        return None

            except Exception as timeout_error:
                print(f"{llm_name} : {timeout_error}")
                return None

            response_time = time.time() - sentence_start
            print(f"{llm_name} : {response_time:.3f}s")

            return {
                "name": llm_name,
                "type": "llm",
                "first_token_time": first_token_time,
                "response_time": response_time,
            }
        except Exception as e:
            error_msg = str(e).lower()

            if (
                "502" in error_msg
                or "bad gateway" in error_msg
                or "error code: 502" in error_msg
            ):
                print(f"{llm_name} 502，")
                return {
                    "name": llm_name,
                    "type": "llm",
                    "errors": 1,
                    "error_type": "502",
                }
            print(f"{llm_name} : {str(e)}")
            return None

    async def _test_llm(self, llm_name: str, config: Dict) -> Dict:
        """ LLM """
        try:

            if llm_name == "Ollama":
                base_url = config.get("base_url", "http://localhost:11434")
                model_name = config.get("model_name")
                if not model_name:
                    print("Ollama  model_name")
                    return {
                        "name": llm_name,
                        "type": "llm",
                        "errors": 1,
                        "error_type": "",
                    }

                if not await self._check_ollama_service(base_url, model_name):
                    return {
                        "name": llm_name,
                        "type": "llm",
                        "errors": 1,
                        "error_type": "",
                    }
            else:
                if "api_key" in config and any(
                    x in config["api_key"] for x in ["", "placeholder", "sk-xxx"]
                ):
                    print(f" LLM: {llm_name}")
                    return {
                        "name": llm_name,
                        "type": "llm",
                        "errors": 1,
                        "error_type": "",
                    }


            module_type = config.get("type", llm_name)
            llm = create_llm_instance(module_type, config)


            test_sentences = [
                s.encode("utf-8").decode("utf-8") for s in self.test_sentences
            ]


            sentence_tasks = []
            for sentence in test_sentences:
                sentence_tasks.append(
                    self._test_single_sentence(llm_name, llm, sentence)
                )


            sentence_results = await asyncio.gather(
                *sentence_tasks, return_exceptions=True
            )


            valid_results = []
            for result in sentence_results:
                if isinstance(result, dict) and result is not None:
                    valid_results.append(result)
                elif isinstance(result, Exception):
                    error_msg = str(result).lower()
                    if "502" in error_msg or "bad gateway" in error_msg:
                        print(f"{llm_name} 502，")
                        return {
                            "name": llm_name,
                            "type": "llm",
                            "errors": 1,
                            "error_type": "502",
                        }
                    else:
                        print(f"{llm_name} : {result}")

            if not valid_results:
                print(f"{llm_name} ，")
                return {
                    "name": llm_name,
                    "type": "llm",
                    "errors": 1,
                    "error_type": "",
                }


            if len(valid_results) < len(test_sentences) * 0.3:
                print(
                    f"{llm_name} ({len(valid_results)}/{len(test_sentences)})，"
                )
                return {
                    "name": llm_name,
                    "type": "llm",
                    "errors": 1,
                    "error_type": "",
                }

            first_token_times = [
                r["first_token_time"]
                for r in valid_results
                if r.get("first_token_time")
            ]
            response_times = [r["response_time"] for r in valid_results]


            if len(response_times) > 1:
                mean = statistics.mean(response_times)
                stdev = statistics.stdev(response_times)
                filtered_times = [t for t in response_times if t <= mean + 3 * stdev]
            else:
                filtered_times = response_times

            return {
                "name": llm_name,
                "type": "llm",
                "avg_response": sum(response_times) / len(response_times),
                "avg_first_token": (
                    sum(first_token_times) / len(first_token_times)
                    if first_token_times
                    else 0
                ),
                "success_rate": f"{len(valid_results)}/{len(test_sentences)}",
                "errors": 0,
            }
        except Exception as e:
            error_msg = str(e).lower()
            if "502" in error_msg or "bad gateway" in error_msg:
                print(f"LLM {llm_name} 502，")
            else:
                print(f"LLM {llm_name} : {str(e)}")
            error_type = ""
            if "timeout" in str(e).lower():
                error_type = ""
            return {
                "name": llm_name,
                "type": "llm",
                "errors": 1,
                "error_type": error_type,
            }

    def _print_results(self):
        """"""
        print("\n" + "=" * 50)
        print("LLM ")
        print("=" * 50)

        if not self.results:
            print("")
            return

        headers = ["", "(s)", "Token(s)", "", ""]
        table_data = []


        valid_results = []
        error_results = []

        for name, data in self.results.items():
            if data["errors"] == 0:

                avg_response = f"{data['avg_response']:.3f}"
                avg_first_token = (
                    f"{data['avg_first_token']:.3f}"
                    if data["avg_first_token"] > 0
                    else "-"
                )
                success_rate = data.get("success_rate", "N/A")
                status = "✅ "


                first_token_value = (
                    data["avg_first_token"]
                    if data["avg_first_token"] > 0
                    else float("inf")
                )

                valid_results.append(
                    {
                        "name": name,
                        "avg_response": avg_response,
                        "avg_first_token": avg_first_token,
                        "success_rate": success_rate,
                        "status": status,
                        "sort_key": first_token_value,
                    }
                )
            else:

                avg_response = "-"
                avg_first_token = "-"
                success_rate = "0/5"


                error_type = data.get("error_type", "")
                status = f"❌ {error_type}"

                error_results.append(
                    [name, avg_response, avg_first_token, success_rate, status]
                )


        valid_results.sort(key=lambda x: x["sort_key"])


        for result in valid_results:
            table_data.append(
                [
                    result["name"],
                    result["avg_response"],
                    result["avg_first_token"],
                    result["success_rate"],
                    result["status"],
                ]
            )


        table_data.extend(error_results)

        print(tabulate(table_data, headers=headers, tablefmt="grid"))
        print("\n:")
        print("- ：")
        print("- ：10")
        print("- ：502")
        print("- ：/")
        print("\nTesting completed！")

    async def run(self):
        """"""
        print(" LLM ...")


        all_tasks = []


        if self.config.get("LLM") is not None:
            for llm_name, config in self.config.get("LLM", {}).items():

                if llm_name == "CozeLLM":
                    if any(x in config.get("bot_id", "") for x in [""]) or any(
                        x in config.get("user_id", "") for x in [""]
                    ):
                        print(f"LLM {llm_name}  bot_id/user_id，")
                        continue
                elif "api_key" in config and any(
                    x in config["api_key"] for x in ["", "placeholder", "sk-xxx"]
                ):
                    print(f"LLM {llm_name}  api_key，")
                    continue


                if llm_name == "Ollama":
                    base_url = config.get("base_url", "http://localhost:11434")
                    model_name = config.get("model_name")
                    if not model_name:
                        print("Ollama  model_name")
                        continue

                    if not await self._check_ollama_service(base_url, model_name):
                        continue

                print(f" LLM : {llm_name}")
                all_tasks.append(self._test_llm(llm_name, config))

        print(f"\n {len(all_tasks)}  LLM ")
        print("\n...\n")


        async def test_with_timeout(task, timeout=30):
            """"""
            try:
                return await asyncio.wait_for(task, timeout=timeout)
            except asyncio.TimeoutError:
                print(f"（{timeout}），")
                return {
                    "name": "Unknown",
                    "type": "llm",
                    "errors": 1,
                    "error_type": "",
                }
            except Exception as e:
                print(f": {str(e)}")
                return {
                    "name": "Unknown",
                    "type": "llm",
                    "errors": 1,
                    "error_type": "",
                }


        protected_tasks = [test_with_timeout(task) for task in all_tasks]


        all_results = await asyncio.gather(*protected_tasks, return_exceptions=True)


        for result in all_results:
            if isinstance(result, dict):
                if result.get("errors") == 0:
                    self.results[result["name"]] = result
                else:

                    if result.get("name") != "Unknown":
                        self.results[result["name"]] = result
            elif isinstance(result, Exception):
                print(f": {str(result)}")


        print("\n...")
        self._print_results()


async def main():
    tester = LLMPerformanceTester()
    await tester.run()


if __name__ == "__main__":
    asyncio.run(main())
