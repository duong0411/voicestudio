import asyncio
import logging
import os
import time
import concurrent.futures
from typing import Dict, Optional
import aiohttp
from tabulate import tabulate
from core.utils.asr import create_instance as create_stt_instance


logging.basicConfig(level=logging.WARNING)

description = ""

class ASRPerformanceTester:
    def __init__(self):
        self.config = self._load_config_from_data_dir()
        self.test_wav_list = self._load_test_wav_files()
        self.results = {"stt": {}}
        

        print(f"[DEBUG] ASR: {self.config.get('ASR', {})}")
        print(f"[DEBUG] : {len(self.test_wav_list)}")

    def _load_config_from_data_dir(self) -> Dict:
        """ data  .config.yaml """
        config = {"ASR": {}}
        data_dir = os.path.join(os.getcwd(), "data")
        print(f"[DEBUG] : {data_dir}")

        for root, _, files in os.walk(data_dir):
            for file in files:
                if file.endswith(".config.yaml"):
                    file_path = os.path.join(root, file)
                    try:
                        with open(file_path, "r", encoding="utf-8") as f:
                            import yaml
                            file_config = yaml.safe_load(f)

                            asr_config = file_config.get("ASR") or file_config.get("asr")
                            if asr_config:
                                config["ASR"].update(asr_config)
                                print(f"[DEBUG]  {file_path}  ASR ")
                    except Exception as e:
                        print(f"  {file_path} : {str(e)}")
        return config

    def _load_test_wav_files(self) -> list:
        """（）"""
        wav_root = os.path.join(os.getcwd(), "config", "assets")
        print(f"[DEBUG] : {wav_root}")
        test_wav_list = []
        
        if os.path.exists(wav_root):
            file_list = os.listdir(wav_root)
            print(f"[DEBUG] : {file_list}")
            for file_name in file_list:
                file_path = os.path.join(wav_root, file_name)
                if os.path.getsize(file_path) > 300 * 1024:  # 300KB
                    with open(file_path, "rb") as f:
                        test_wav_list.append(f.read())
        else:
            print(f" : {wav_root}")
        return test_wav_list

    async def _test_single_audio(self, stt_name: str, stt, audio_data: bytes) -> Optional[float]:
        """"""
        try:
            start_time = time.time()
            text, _ = await stt.speech_to_text_wrapper([audio_data], "1", stt.audio_format)
            if text is None:
                return None
            
            duration = time.time() - start_time
            

            if abs(duration) < 0.001:
                print(f"{stt_name} : {duration:.6f}s ()")
                return None
                
            return duration
        except Exception as e:
            error_msg = str(e).lower()
            if "502" in error_msg or "bad gateway" in error_msg:
                print(f"{stt_name} 502")
                return None
            return None

    async def _test_stt_with_timeout(self, stt_name: str, config: Dict) -> Dict:
        """STT，"""
        try:

            token_fields = ["access_token", "api_key", "token"]
            if any(
                field in config
                and str(config[field]).lower() in ["", "placeholder", "none", "null", ""]
                for field in token_fields
            ):
                print(f"  STT {stt_name} access_token/api_key，")
                return {
                    "name": stt_name,
                    "type": "stt",
                    "errors": 1,
                    "error_type": ""
                }

            module_type = config.get("type", stt_name)
            stt = create_stt_instance(module_type, config, delete_audio_file=True)
            stt.audio_format = "pcm"

            print(f"  STT: {stt_name}")


            loop = asyncio.get_event_loop()
            

            try:
                with concurrent.futures.ThreadPoolExecutor() as executor:
                    future = executor.submit(
                        lambda: asyncio.run(self._test_single_audio(stt_name, stt, self.test_wav_list[0]))
                    )
                    first_result = await asyncio.wait_for(
                        asyncio.wrap_future(future), timeout=10.0
                    )
                    
                    if first_result is None:
                        print(f" {stt_name} ")
                        return {
                            "name": stt_name,
                            "type": "stt",
                            "errors": 1,
                            "error_type": ""
                        }
            except asyncio.TimeoutError:
                print(f" {stt_name} （10），")
                return {
                    "name": stt_name,
                    "type": "stt",
                    "errors": 1,
                    "error_type": ""
                }
            except Exception as e:
                error_msg = str(e).lower()
                if "502" in error_msg or "bad gateway" in error_msg:
                    print(f" {stt_name} 502，")
                    return {
                        "name": stt_name,
                        "type": "stt",
                        "errors": 1,
                        "error_type": "502"
                    }
                print(f" {stt_name} : {str(e)}")
                return {
                    "name": stt_name,
                    "type": "stt",
                    "errors": 1,
                    "error_type": ""
                }


            total_time = 0
            valid_tests = 0
            test_count = len(self.test_wav_list)
            
            for i, audio_data in enumerate(self.test_wav_list, 1):
                try:
                    with concurrent.futures.ThreadPoolExecutor() as executor:
                        future = executor.submit(
                            lambda: asyncio.run(self._test_single_audio(stt_name, stt, audio_data))
                        )
                        duration = await asyncio.wait_for(
                            asyncio.wrap_future(future), timeout=10.0
                        )
                        
                        if duration is not None and duration > 0.001:  
                            total_time += duration
                            valid_tests += 1
                            print(f" {stt_name} [{i}/{test_count}] : {duration:.2f}s")
                        else:
                            print(f" {stt_name} [{i}/{test_count}] (0.000s)")
                            
                except asyncio.TimeoutError:
                    print(f" {stt_name} [{i}/{test_count}] （10），")
                    continue
                except Exception as e:
                    error_msg = str(e).lower()
                    if "502" in error_msg or "bad gateway" in error_msg:
                        print(f" {stt_name} [{i}/{test_count}] 502，")
                        return {
                            "name": stt_name,
                            "type": "stt",
                            "errors": 1,
                            "error_type": "502"
                        }
                    print(f" {stt_name} [{i}/{test_count}] : {str(e)}")
                    continue

            if valid_tests < test_count * 0.3:
                print(f" {stt_name} ({valid_tests}/{test_count})，")
                return {
                    "name": stt_name,
                    "type": "stt",
                    "errors": 1,
                    "error_type": ""
                }

            if valid_tests == 0:
                return {
                    "name": stt_name,
                    "type": "stt",
                    "errors": 1,
                    "error_type": ""
                }

            avg_time = total_time / valid_tests
            return {
                "name": stt_name,
                "type": "stt",
                "avg_time": avg_time,
                "success_rate": f"{valid_tests}/{test_count}",
                "errors": 0,
            }

        except Exception as e:
            error_msg = str(e).lower()
            if "502" in error_msg or "bad gateway" in error_msg:
                error_type = "502"
            elif "timeout" in error_msg:
                error_type = ""
            else:
                error_type = ""
            print(f"⚠️ {stt_name} : {str(e)}")
            return {
                "name": stt_name,
                "type": "stt",
                "errors": 1,
                "error_type": error_type
            }

    def _print_results(self):
        """，"""
        print("\n" + "=" * 50)
        print("ASR ")
        print("=" * 50)

        if not self.results.get("stt"):
            print("")
            return

        headers = ["", "(s)", "", ""]
        table_data = []


        valid_results = []
        error_results = []

        for name, data in self.results["stt"].items():
            if data["errors"] == 0:

                avg_time = f"{data['avg_time']:.3f}"
                success_rate = data.get("success_rate", "N/A")
                status = "✅ "
                

                sort_key = data["avg_time"]
                
                valid_results.append({
                    "name": name,
                    "avg_time": avg_time,
                    "success_rate": success_rate,
                    "status": status,
                    "sort_key": sort_key,
                })
            else:

                avg_time = "-"
                success_rate = "0/N"
                

                error_type = data.get("error_type", "")
                status = f"❌ {error_type}"
                
                error_results.append([name, avg_time, success_rate, status])


        valid_results.sort(key=lambda x: x["sort_key"])


        for result in valid_results:
            table_data.append([
                result["name"],
                result["avg_time"],
                result["success_rate"],
                result["status"],
            ])


        table_data.extend(error_results)

        print(tabulate(table_data, headers=headers, tablefmt="grid"))
        print("\n:")
        print("- ：10")
        print("- ：502、")
        print("- ：/")
        print("- ：，")
        print("\nTesting completed！")

    async def run(self):
        """""" 
        print("ASR...")
        if not self.config.get("ASR"):
            print(" ASR ")
            return

        all_tasks = []
        for stt_name, config in self.config["ASR"].items():

            token_fields = ["access_token", "api_key", "token"]
            if any(
                field in config
                and str(config[field]).lower() in ["", "placeholder", "none", "null", ""]
                for field in token_fields
            ):
                print(f"ASR {stt_name} access_token/api_key，")
                continue
            
            print(f" ASR : {stt_name}")
            all_tasks.append(self._test_stt_with_timeout(stt_name, config))

        if not all_tasks:
            print("ASR。")
            return

        print(f"\n {len(all_tasks)} ASR")
        print("\nASR...")
        all_results = await asyncio.gather(*all_tasks, return_exceptions=True)


        for result in all_results:
            if isinstance(result, dict) and result.get("type") == "stt":
                self.results["stt"][result["name"]] = result


        self._print_results()


async def main():
    tester = ASRPerformanceTester()
    await tester.run()


if __name__ == "__main__":
    asyncio.run(main())