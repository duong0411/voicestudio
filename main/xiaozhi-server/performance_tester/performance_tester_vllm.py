import time
import asyncio
import logging
import statistics
import base64
from typing import Dict
from tabulate import tabulate
from core.utils.vllm import create_instance
from config.settings import load_config


logging.basicConfig(level=logging.WARNING)

description = ""


class AsyncVisionPerformanceTester:
    def __init__(self):
        self.config = load_config()

        self.test_images = [
            "../../docs/images/demo1.png",
            "../../docs/images/demo2.png",
        ]
        self.test_questions = [
            "？",
            "",
        ]


        self.results = {"vllm": {}}

    async def _test_vllm(self, vllm_name: str, config: Dict) -> Dict:
        """"""
        try:

            if "api_key" in config and any(
                x in config["api_key"] for x in ["", "placeholder", "sk-xxx"]
            ):
                print(f"⏭️  VLLM {vllm_name} api_key，")
                return {"name": vllm_name, "type": "vllm", "errors": 1}


            module_type = config.get("type", vllm_name)
            vllm = create_instance(module_type, config)

            print(f"🖼️  VLLM: {vllm_name}")


            test_tasks = []
            for question in self.test_questions:
                for image in self.test_images:
                    test_tasks.append(
                        self._test_single_vision(vllm_name, vllm, question, image)
                    )


            test_results = await asyncio.gather(*test_tasks)


            valid_results = [r for r in test_results if r is not None]
            if not valid_results:
                print(f"⚠️  {vllm_name} ，")
                return {"name": vllm_name, "type": "vllm", "errors": 1}

            response_times = [r["response_time"] for r in valid_results]


            mean = statistics.mean(response_times)
            stdev = statistics.stdev(response_times) if len(response_times) > 1 else 0
            filtered_times = [t for t in response_times if t <= mean + 3 * stdev]

            if len(filtered_times) < len(test_tasks) * 0.5:
                print(f"⚠️  {vllm_name} ，")
                return {"name": vllm_name, "type": "vllm", "errors": 1}

            return {
                "name": vllm_name,
                "type": "vllm",
                "avg_response": sum(response_times) / len(response_times),
                "std_response": (
                    statistics.stdev(response_times) if len(response_times) > 1 else 0
                ),
                "errors": 0,
            }

        except Exception as e:
            print(f"⚠️ VLLM {vllm_name} : {str(e)}")
            return {"name": vllm_name, "type": "vllm", "errors": 1}

    async def _test_single_vision(
        self, vllm_name: str, vllm, question: str, image: str
    ) -> Dict:
        """"""
        try:
            print(f"📝 {vllm_name} : {question[:20]}...")
            start_time = time.time()


            with open(image, "rb") as image_file:
                image_data = image_file.read()
                image_base64 = base64.b64encode(image_data).decode("utf-8")


            response = vllm.response(question, image_base64)
            response_time = time.time() - start_time
            print(f"✓ {vllm_name} : {response_time:.3f}s")

            return {
                "name": vllm_name,
                "type": "vllm",
                "response_time": response_time,
            }
        except Exception as e:
            print(f"⚠️ {vllm_name} : {str(e)}")
            return None

    def _print_results(self):
        """"""
        vllm_table = []
        for name, data in self.results["vllm"].items():
            if data["errors"] == 0:
                stability = data["std_response"] / data["avg_response"]
                vllm_table.append(
                    [
                        name,
                        f"{data['avg_response']:.3f}",
                        f"{stability:.3f}",
                    ]
                )

        if vllm_table:
            print("\n:\n")
            print(
                tabulate(
                    vllm_table,
                    headers=["", "", ""],
                    tablefmt="github",
                    colalign=("left", "right", "right"),
                    disable_numparse=True,
                )
            )
        else:
            print("\n⚠️ 。")

    async def run(self):
        """"""
        print("🔍 ...")

        if not self.test_images:
            print(f"\n⚠️  {self.image_root} ，")
            return


        all_tasks = []


        if self.config.get("VLLM") is not None:
            for vllm_name, config in self.config.get("VLLM", {}).items():
                if "api_key" in config and any(
                    x in config["api_key"] for x in ["", "placeholder", "sk-xxx"]
                ):
                    print(f"⏭️  VLLM {vllm_name} api_key，")
                    continue
                print(f"🖼️ VLLM: {vllm_name}")
                all_tasks.append(self._test_vllm(vllm_name, config))

        print(f"\n✅  {len(all_tasks)} ")
        print(f"✅  {len(self.test_images)} ")
        print(f"✅  {len(self.test_questions)} ")
        print("\n⏳ ...\n")


        all_results = await asyncio.gather(*all_tasks, return_exceptions=True)


        for result in all_results:
            if isinstance(result, dict) and result["errors"] == 0:
                self.results["vllm"][result["name"]] = result


        print("\n📊 ...")
        self._print_results()


async def main():
    tester = AsyncVisionPerformanceTester()
    await tester.run()


if __name__ == "__main__":
    asyncio.run(main())
