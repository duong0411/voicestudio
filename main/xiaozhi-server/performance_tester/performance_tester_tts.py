import asyncio
import logging
import os
import time
import threading
from typing import Dict
import yaml
from tabulate import tabulate


from core.utils.tts import create_instance as create_tts_instance
from config.settings import load_config


logging.basicConfig(level=logging.WARNING)

description = ""


class TTSPerformanceTester:
    def __init__(self):
        self.config = load_config()
        self.test_sentences = self.config.get("module_test", {}).get(
            "test_sentences",
            [
                "，，；",
                "，，，；，。，，",
                "，，，。，。",
            ],
        )
        self.results = {}

    async def _test_tts(self, tts_name: str, config: Dict) -> Dict:
        """TTS"""
        try:
            token_fields = ["access_token", "api_key", "token"]
            if any(
                field in config
                and any(x in config[field] for x in ["", "placeholder"])
                for field in token_fields
            ):
                print(f"TTS {tts_name} access_token/api_key，")
                return {"name": tts_name, "errors": 1}

            module_type = config.get("type", tts_name)
            tts = create_tts_instance(module_type, config, delete_audio_file=True)


            class MockConn:
                sample_rate = 16000
                audio_format = "pcm"
                stop_event = threading.Event()
                client_abort = False
                headers = {}
            tts.conn = MockConn()


            class MockOpusEncoder:
                pass
            if not hasattr(tts, 'opus_encoder') or tts.opus_encoder is None:
                tts.opus_encoder = MockOpusEncoder()

            print(f" TTS: {tts_name}")


            tmp_file = tts.generate_filename()
            await tts.text_to_speak("", tmp_file)

            if not tmp_file or not os.path.exists(tmp_file):
                print(f"{tts_name} ")
                return {"name": tts_name, "errors": 1}

            total_time = 0
            test_count = len(self.test_sentences[:3])

            for i, sentence in enumerate(self.test_sentences[:2], 1):
                start = time.time()
                tmp_file = tts.generate_filename()
                await tts.text_to_speak(sentence, tmp_file)
                duration = time.time() - start
                total_time += duration

                if tmp_file and os.path.exists(tmp_file):
                    print(f"{tts_name} [{i}/{test_count}] ")
                else:
                    print(f"{tts_name} [{i}/{test_count}] ")
                    return {"name": tts_name, "errors": 1}

            return {
                "name": tts_name,
                "avg_time": total_time / test_count,
                "errors": 0,
            }

        except Exception as e:
            print(f"{tts_name} : {str(e)}")
            return {"name": tts_name, "errors": 1}

    def _print_results(self):
        """"""
        if not self.results:
            print("TTS")
            return

        headers = ["TTS", "()", "", ""]
        table_data = []


        valid_results = []
        error_results = []

        for name, data in self.results.items():
            if data["errors"] == 0:

                avg_time = f"{data['avg_time']:.3f}"
                test_count = len(self.test_sentences[:3])
                status = "✅ "
                

                valid_results.append({
                    "name": name,
                    "avg_time": avg_time,
                    "test_count": test_count,
                    "status": status,
                    "sort_key": data['avg_time']
                })
            else:

                avg_time = "-"
                test_count = "0/3"
                

                error_type = ""
                status = f"❌ {error_type}"
                
                error_results.append([name, avg_time, test_count, status])


        valid_results.sort(key=lambda x: x["sort_key"])


        for result in valid_results:
            table_data.append([
                result["name"],
                result["avg_time"],
                result["test_count"],
                result["status"]
            ])


        table_data.extend(error_results)

        print("\nTTS:")
        print(
            tabulate(
                table_data,
                headers=headers,
                tablefmt="grid",
                colalign=("left", "right", "right", "left"),
            )
        )
        print("\n:")
        print("- : 10")
        print("- : ")
        print("- : ")

    async def run(self):
        """"""
        print("TTS...")

        if not self.config.get("TTS"):
            print("TTS")
            return


        tasks = []
        for tts_name, config in self.config.get("TTS", {}).items():
            tasks.append(self._test_tts(tts_name, config))


        results = await asyncio.gather(*tasks)


        for result in results:
            self.results[result["name"]] = result


        self._print_results()



async def main():
    tester = TTSPerformanceTester()
    await tester.run()


if __name__ == "__main__":
    tester = TTSPerformanceTester()
    asyncio.run(tester.run())
