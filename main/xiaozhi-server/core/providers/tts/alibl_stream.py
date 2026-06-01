import os
import uuid
import json
import time
import queue
import asyncio
import traceback
import websockets

from asyncio import Task
from typing import Callable, Any
from config.logger import setup_logging
from core.utils.tts import MarkdownCleaner
from core.providers.tts.base import TTSProviderBase
from core.providers.tts.dto.dto import SentenceType, ContentType, InterfaceType

TAG = __name__
logger = setup_logging()


class TTSProvider(TTSProviderBase):
    TTS_PARAM_CONFIG = [
        ("ttsVolume", "volume", 0, 100, 50, int),
        ("ttsRate", "rate", 0.5, 2.0, 1.0, lambda v: round(v, 1)),
        ("ttsPitch", "pitch", 0.5, 2.0, 1.0, lambda v: round(v, 1)),
    ]

    def __init__(self, config, delete_audio_file):
        super().__init__(config, delete_audio_file)

        self.interface_type = InterfaceType.DUAL_STREAM

        self.api_key = config.get("api_key")
        if not self.api_key:
            raise ValueError("api_key is required for CosyVoice TTS")
        self.report_on_last = True


        self.ws_url = "wss://dashscope.aliyuncs.com/api-ws/v1/inference/"
        self.ws = None
        self._monitor_task = None
        self.activate_session = False
        self.last_active_time = None


        self.model = config.get("model", "cosyvoice-v2")
        self.voice = config.get("voice", "longxiaochun_v2")
        if config.get("private_voice"):
            self.voice = config.get("private_voice")


        self.format = config.get("format", "pcm")

        volume = config.get("volume", "50")
        self.volume = int(volume) if volume else 50

        rate = config.get("rate", "1.0")
        self.rate = float(rate) if rate else 1.0

        pitch = config.get("pitch", "1.0")
        self.pitch = float(pitch) if pitch else 1.0


        self._apply_percentage_params(config)

        self.header = {
            "Authorization": f"Bearer {self.api_key}",


            "X-DashScope-DataInspection": "enable",
        }

    async def _ensure_connection(self):
        """WebSocket，60"""
        try:
            current_time = time.time()
            if self.ws and current_time - self.last_active_time < 60:

                logger.bind(tag=TAG).debug(f"...")
                return self.ws
            logger.bind(tag=TAG).debug("...")

            self.ws = await websockets.connect(
                self.ws_url,
                additional_headers=self.header,
                ping_interval=30,
                ping_timeout=10,
                close_timeout=10,
            )

            logger.bind(tag=TAG).debug("WebSocket")
            self.last_active_time = current_time
            return self.ws
        except Exception as e:
            logger.bind(tag=TAG).error(f": {str(e)}")
            self.ws = None
            self.last_active_time = None
            raise

    def tts_text_priority_thread(self):
        """TTS"""
        while not self.conn.stop_event.is_set():
            try:
                message = self.tts_text_queue.get(timeout=1)

                if self.conn.client_abort:
                    try:
                        logger.bind(tag=TAG).info("，TTS")
                        asyncio.run_coroutine_threadsafe(
                            self.finish_session(self.conn.sentence_id),
                            loop=self.conn.loop,
                        )
                        continue
                    except Exception as e:
                        logger.bind(tag=TAG).error(f"TTS: {str(e)}")
                        continue


                if message.sentence_id != self.conn.sentence_id:
                    continue

                logger.bind(tag=TAG).debug(
                    f"TTS｜{message.sentence_type.name} ｜ {message.content_type.name} | ID: {message.sentence_id}"
                )

                if message.sentence_type == SentenceType.FIRST:

                    self.reset_stream_state()

                    try:
                        if not getattr(self.conn, "sentence_id", None): 
                            self.conn.sentence_id = uuid.uuid4().hex
                            logger.bind(tag=TAG).debug(f" ID: {self.conn.sentence_id}")

                        logger.bind(tag=TAG).debug("TTS...")
                        future = asyncio.run_coroutine_threadsafe(
                            self.start_session(self.conn.sentence_id),
                            loop=self.conn.loop,
                        )
                        future.result(timeout=self.tts_timeout)
                        self.before_stop_play_files.clear()
                        logger.bind(tag=TAG).debug("TTS")
                    except Exception as e:
                        logger.bind(tag=TAG).error(f"TTS: {str(e)}")
                        continue

                elif ContentType.TEXT == message.content_type:
                    if message.content_detail:
                        try:
                            logger.bind(tag=TAG).debug(
                                f"TTS: {message.content_detail}"
                            )
                            future = asyncio.run_coroutine_threadsafe(
                                self.text_to_speak(message.content_detail, None),
                                loop=self.conn.loop,
                            )
                            future.result(timeout=self.tts_timeout)
                        except Exception as e:
                            logger.bind(tag=TAG).error(f"TTS: {str(e)}")
                            continue

                elif ContentType.FILE == message.content_type:
                    logger.bind(tag=TAG).info(
                        f": {message.content_file}"
                    )
                    if message.content_file and os.path.exists(message.content_file):

                        self._process_audio_file_stream(message.content_file, callback=lambda audio_data: self.handle_audio_file(audio_data, message.content_detail))

                if message.sentence_type == SentenceType.LAST:
                    try:
                        logger.bind(tag=TAG).debug("TTS...")
                        future = asyncio.run_coroutine_threadsafe(
                            self.finish_session(self.conn.sentence_id),
                            loop=self.conn.loop,
                        )
                        future.result()
                    except Exception as e:
                        logger.bind(tag=TAG).error(f"TTS: {str(e)}")
                        continue

            except queue.Empty:
                continue
            except Exception as e:
                logger.bind(tag=TAG).error(
                    f"TTS: {str(e)}, : {type(e).__name__}, : {traceback.format_exc()}"
                )
                continue

    async def text_to_speak(self, text, _):
        """TTS"""
        try:
            if self.ws is None:
                logger.bind(tag=TAG).warning("WebSocket，")
                return


            filtered_text = MarkdownCleaner.clean_markdown(text)

            if filtered_text:

                confirmed_texts, self._pending_prefix = self._match_stream_text(filtered_text)


                for txt in confirmed_texts:
                    if txt and self.ws:
                        continue_task_message = {
                            "header": {
                                "action": "continue-task",
                                "task_id": self.conn.sentence_id,
                                "streaming": "duplex",
                            },
                            "payload": {"input": {"text": txt}},
                        }
                        await self.ws.send(json.dumps(continue_task_message))
                        self.last_active_time = time.time()
            return
        except Exception as e:
            logger.bind(tag=TAG).error(f"TTS: {str(e)}")
            if self.ws:
                try:
                    await self.ws.close()
                except:
                    pass
                self.ws = None
            raise

    async def start_session(self, session_id):
        """TTS"""
        logger.bind(tag=TAG).debug(f"～～{session_id}")
        try:

            if self.activate_session:
                await self.close()


            self.activate_session = True


            await self._ensure_connection()


            if self._monitor_task is None or self._monitor_task.done():
                logger.bind(tag=TAG).debug("...")
                self._monitor_task = asyncio.create_task(self._start_monitor_tts_response())


            run_task_message = {
                "header": {
                    "action": "run-task",
                    "task_id": session_id,
                    "streaming": "duplex",
                },
                "payload": {
                    "task_group": "audio",
                    "task": "tts",
                    "function": "SpeechSynthesizer",
                    "model": self.model,
                    "parameters": {
                        "text_type": "PlainText",
                        "voice": self.voice,
                        "format": self.format,
                        "sample_rate": self.conn.sample_rate,
                        "volume": self.volume,
                        "rate": self.rate,
                        "pitch": self.pitch,
                    },
                    "input": {}
                },
            }

            await self.ws.send(json.dumps(run_task_message))
            self.last_active_time = time.time()
            logger.bind(tag=TAG).debug("")
        except Exception as e:
            logger.bind(tag=TAG).error(f": {str(e)}")
            await self.close()
            raise

    async def finish_session(self, session_id):
        """TTS"""
        logger.bind(tag=TAG).debug(f"～～{session_id}")
        try:
            if self.ws and session_id:

                finish_task_message = {
                    "header": {
                        "action": "finish-task",
                        "task_id": session_id,
                        "streaming": "duplex",
                    },
                    "payload": {
                        "input": {}
                    }
                }

                await self.ws.send(json.dumps(finish_task_message))
                self.last_active_time = time.time()

        except Exception as e:
            logger.bind(tag=TAG).error(f": {str(e)}")
            await self.close()
            raise

    async def close(self):
        """"""
        await super().close()
        self.activate_session = False

        if self._monitor_task:
            try:
                self._monitor_task.cancel()
                await self._monitor_task
            except asyncio.CancelledError:
                pass
            except Exception as e:
                logger.bind(tag=TAG).warning(f": {e}")
            self._monitor_task = None


        if self.ws:
            try:
                await self.ws.close()
            except:
                pass
            self.ws = None
            self.last_active_time = None

    async def _start_monitor_tts_response(self):
        """TTS - """
        try:
            while not self.conn.stop_event.is_set():
                try:
                    msg = await self.ws.recv()
                    self.last_active_time = time.time()

                    if isinstance(msg, str):
                        try:
                            data = json.loads(msg)
                            header = data.get("header", {})
                            event = header.get("event")
                            task_id = header.get("task_id")


                            if task_id and self.conn.sentence_id != task_id:
                                if event in ["task-finished", "task-failed"]:
                                    logger.bind(tag=TAG).debug(f"～～")
                                    self.activate_session = False
                                continue

                            if event == "task-started":
                                logger.bind(tag=TAG).debug("TTS~")
                                self.tts_audio_queue.put((SentenceType.FIRST, [], None))
                            elif event == "result-generated":

                                tts_text = self.get_tts_text(self.conn.sentence_id)
                                if tts_text:
                                    logger.bind(tag=TAG).info(
                                        f"： {tts_text}"
                                    )
                                    self.tts_audio_queue.put(
                                        (SentenceType.FIRST, [], tts_text)
                                    )
                                    self.clear_tts_text(self.conn.sentence_id)
                            elif event == "task-finished":
                                logger.bind(tag=TAG).debug("TTS~")
                                self.activate_session = False
                                self._process_before_stop_play_files()
                            elif event == "task-failed":
                                error_code = header.get("error_code", "unknown")
                                error_message = header.get("error_message", "Unknown error")
                                logger.bind(tag=TAG).error(
                                    f"TTS: {error_code} - {error_message}"
                                )
                                break
                        except json.JSONDecodeError:
                            logger.bind(tag=TAG).warning("JSON")
                    elif isinstance(msg, (bytes, bytearray)):
                        self.opus_encoder.encode_pcm_to_opus_stream(
                            msg, False, callback=self.handle_opus
                        )
                except websockets.ConnectionClosed:
                    logger.bind(tag=TAG).warning("WebSocket")
                    break
                except Exception as e:
                    logger.bind(tag=TAG).error(
                        f"TTS: {e}\n{traceback.format_exc()}"
                    )
                    break


            if self.ws:
                try:
                    await self.ws.close()
                except:
                    pass
                self.ws = None

        finally:
            self.activate_session = False
            self._monitor_task = None

    def audio_to_opus_data_stream(
        self, audio_file_path, callback: Callable[[Any], Any] = None
    ):
        """：，TTS。
        TTS，monitorevent loopTTSself.opus_encoder，
        tts_text_priority_threadself.opus_encoder，
        encoder.buffer，SILK resampler。
        """
        from core.utils.util import audio_to_data_stream

        return audio_to_data_stream(
            audio_file_path,
            is_opus=True,
            callback=callback,
            sample_rate=self.conn.sample_rate,
            opus_encoder=None,
        )

    def to_tts(self, text: str) -> list:
        """，"""
        try:

            loop = asyncio.new_event_loop()
            asyncio.set_event_loop(loop)


            session_id = uuid.uuid4().hex

            audio_data = []

            async def _generate_audio():
                ws = await websockets.connect(
                    self.ws_url,
                    additional_headers=self.header,
                    ping_interval=30,
                    ping_timeout=10,
                    close_timeout=10,
                    max_size=10 * 1024 * 1024,
                )

                try:

                    run_task_message = {
                        "header": {
                            "action": "run-task",
                            "task_id": session_id,
                            "streaming": "duplex",
                        },
                        "payload": {
                            "task_group": "audio",
                            "task": "tts",
                            "function": "SpeechSynthesizer",
                            "model": self.model,
                            "parameters": {
                                "text_type": "PlainText",
                                "voice": self.voice,
                                "format": self.format,
                                "sample_rate": self.conn.sample_rate,
                                "volume": self.volume,
                                "rate": self.rate,
                                "pitch": self.pitch,
                            },
                            "input": {}
                        },
                    }
                    await ws.send(json.dumps(run_task_message))


                    task_started = False
                    while not task_started:
                        msg = await ws.recv()
                        if isinstance(msg, str):
                            data = json.loads(msg)
                            header = data.get("header", {})
                            if header.get("event") == "task-started":
                                task_started = True
                                logger.bind(tag=TAG).debug("TTS")
                            elif header.get("event") == "task-failed":
                                error_code = header.get("error_code", "unknown")
                                error_message = header.get("error_message", "Unknown error")
                                raise Exception(
                                    f": {error_code} - {error_message}"
                                )


                    filtered_text = MarkdownCleaner.clean_markdown(text)
                    if self._correct_words_pattern:
                        filtered_text = self._correct_words_pattern.sub(lambda m: self.correct_words[m.group(0)], filtered_text)

                    continue_task_message = {
                        "header": {
                            "action": "continue-task",
                            "task_id": session_id,
                            "streaming": "duplex",
                        },
                        "payload": {"input": {"text": filtered_text}},
                    }
                    await ws.send(json.dumps(continue_task_message))


                    finish_task_message = {
                        "header": {
                            "action": "finish-task",
                            "task_id": session_id,
                            "streaming": "duplex",
                        },
                        "payload": {
                            "input": {}
                        }
                    }
                    await ws.send(json.dumps(finish_task_message))


                    task_finished = False
                    while not task_finished:
                        msg = await ws.recv()
                        if isinstance(msg, (bytes, bytearray)):
                            self.opus_encoder.encode_pcm_to_opus_stream(
                                msg,
                                end_of_stream=False,
                                callback=lambda opus: audio_data.append(opus)
                            )
                        elif isinstance(msg, str):
                            data = json.loads(msg)
                            header = data.get("header", {})
                            if header.get("event") == "task-finished":
                                task_finished = True
                                logger.bind(tag=TAG).debug("TTS")
                            elif header.get("event") == "task-failed":
                                error_code = header.get("error_code", "unknown")
                                error_message = header.get("error_message", "Unknown error")
                                raise Exception(
                                    f": {error_code} - {error_message}"
                                )

                finally:

                    try:
                        await ws.close()
                    except:
                        pass


            loop.run_until_complete(_generate_audio())
            loop.close()

            return audio_data

        except Exception as e:
            logger.bind(tag=TAG).error(f": {str(e)}")
            return []