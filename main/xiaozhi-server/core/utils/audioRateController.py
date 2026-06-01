import time
import asyncio
from collections import deque
from config.logger import setup_logging

TAG = __name__
logger = setup_logging()


class AudioRateController:
    """
     - 60ms
    
    """

    def __init__(self, frame_duration=60):
        """
        Args:
            frame_duration: （），60ms
        """
        self.frame_duration = frame_duration
        self.queue = deque()
        self.play_position = 0
        self.start_timestamp = None
        self.pending_send_task = None
        self.logger = logger
        self.queue_empty_event = asyncio.Event()
        self.queue_empty_event.set()
        self.queue_has_data_event = asyncio.Event()
        self._last_queue_empty_time = 0

    def reset(self):
        """"""
        if self.pending_send_task and not self.pending_send_task.done():
            self.pending_send_task.cancel()


        self.queue.clear()
        self.play_position = 0
        self.start_timestamp = None
        self._last_queue_empty_time = 0

        self.queue_empty_event.set()
        self.queue_has_data_event.clear()

    def add_audio(self, opus_packet):
        """"""



        if len(self.queue) == 0 and self.play_position > 0:
            elapsed_since_empty = (time.monotonic() - self._last_queue_empty_time) * 1000

            if elapsed_since_empty >= self.frame_duration:
                self.start_timestamp = time.monotonic() - (self.play_position / 1000)
                self.logger.bind(tag=TAG).debug(
                    f"，，: {self.play_position}ms，: {elapsed_since_empty:.0f}ms"
                )

        self.queue.append(("audio", opus_packet))

        self.queue_empty_event.clear()
        self.queue_has_data_event.set()

    def add_message(self, message_callback):
        """
        （，）

        Args:
            message_callback:  async def()
        """
        if len(self.queue) == 0 and self.play_position > 0:
            elapsed_since_empty = (time.monotonic() - self._last_queue_empty_time) * 1000
            if elapsed_since_empty >= self.frame_duration:
                self.start_timestamp = time.monotonic() - (self.play_position / 1000)
                self.logger.bind(tag=TAG).debug(
                    f"，，: {self.play_position}ms，: {elapsed_since_empty:.0f}ms"
                )

        self.queue.append(("message", message_callback))

        self.queue_empty_event.clear()
        self.queue_has_data_event.set()

    def _get_elapsed_ms(self):
        """（）"""
        if self.start_timestamp is None:
            return 0
        return (time.monotonic() - self.start_timestamp) * 1000

    async def check_queue(self, send_audio_callback):
        """
        /

        Args:
            send_audio_callback:  async def(opus_packet)
        """
        while self.queue:
            item = self.queue[0]
            item_type = item[0]

            if item_type == "message":

                _, message_callback = item
                self.queue.popleft()
                try:
                    await message_callback()
                except Exception as e:
                    self.logger.bind(tag=TAG).error(f": {e}")
                    raise

            elif item_type == "audio":
                if self.start_timestamp is None:
                    self.start_timestamp = time.monotonic()

                _, opus_packet = item


                while True:

                    elapsed_ms = self._get_elapsed_ms()
                    output_ms = self.play_position

                    if elapsed_ms < output_ms:

                        wait_ms = output_ms - elapsed_ms


                        try:
                            await asyncio.sleep(wait_ms / 1000)
                        except asyncio.CancelledError:
                            self.logger.bind(tag=TAG).debug("")
                            raise

                    else:

                        break


                self.queue.popleft()
                self.play_position += self.frame_duration
                try:
                    await send_audio_callback(opus_packet)
                except Exception as e:
                    self.logger.bind(tag=TAG).error(f": {e}")
                    raise


        self.queue_empty_event.set()
        self.queue_has_data_event.clear()
        self._last_queue_empty_time = time.monotonic()

    def start_sending(self, send_audio_callback):
        """
        

        Args:
            send_audio_callback: 

        Returns:
            asyncio.Task: 
        """

        async def _send_loop():
            try:
                while True:

                    await self.queue_has_data_event.wait()

                    await self.check_queue(send_audio_callback)
            except asyncio.CancelledError:
                self.logger.bind(tag=TAG).debug("")
            except Exception as e:
                self.logger.bind(tag=TAG).error(f": {e}")

        self.pending_send_task = asyncio.create_task(_send_loop())
        return self.pending_send_task

    def stop_sending(self):
        """"""
        if self.pending_send_task and not self.pending_send_task.done():
            self.pending_send_task.cancel()
            self.logger.bind(tag=TAG).debug("")
