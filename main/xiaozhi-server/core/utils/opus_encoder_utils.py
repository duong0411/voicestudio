"""
Opus
PCMOpus
"""

import logging
import traceback
import numpy as np
from opuslib_next import Encoder
from opuslib_next import constants
from typing import Optional, Callable, Any

class OpusEncoderUtils:
    """PCMOpus"""

    def __init__(self, sample_rate: int, channels: int, frame_size_ms: int):
        """
        Opus

        Args:
            sample_rate:  (Hz)
            channels:  (1=, 2=)
            frame_size_ms:  ()
        """
        self.sample_rate = sample_rate
        self.channels = channels
        self.frame_size_ms = frame_size_ms

        self.frame_size = (sample_rate * frame_size_ms) // 1000

        self.total_frame_size = self.frame_size * channels


        self.bitrate = 24000  # bps
        self.complexity = 10


        self.buffer = np.array([], dtype=np.int16)

        try:

            self.encoder = Encoder(
                sample_rate, channels, constants.APPLICATION_AUDIO
            )
            self.encoder.bitrate = self.bitrate
            self.encoder.complexity = self.complexity
            self.encoder.signal = constants.SIGNAL_VOICE
        except Exception as e:
            logging.error(f"Opus: {e}")
            raise RuntimeError("") from e

    def reset_state(self):
        """"""
        self.encoder.reset_state()
        self.buffer = np.array([], dtype=np.int16)

    def encode_pcm_to_opus_stream(self, pcm_data: bytes, end_of_stream: bool, callback: Callable[[Any], Any]):
        """
        PCMOpus，

        Args:
            pcm_data: PCM
            end_of_stream: ,
            callback: opus

        Returns:
            Opus
        """

        new_samples = self._convert_bytes_to_shorts(pcm_data)


        self._validate_pcm_data(new_samples)


        self.buffer = np.append(self.buffer, new_samples)

        offset = 0


        while offset <= len(self.buffer) - self.total_frame_size:
            frame = self.buffer[offset : offset + self.total_frame_size]
            output = self._encode(frame)
            if output:
                callback(output)
            offset += self.total_frame_size


        self.buffer = self.buffer[offset:]


        if end_of_stream and len(self.buffer) > 0:

            last_frame = np.zeros(self.total_frame_size, dtype=np.int16)
            last_frame[: len(self.buffer)] = self.buffer

            output = self._encode(last_frame)
            if output:
                callback(output)
            self.buffer = np.array([], dtype=np.int16)

    def _encode(self, frame: np.ndarray) -> Optional[bytes]:
        """"""
        try:

            if not hasattr(self, 'encoder') or self.encoder is None:
                return None

            frame_bytes = frame.tobytes()

            encoded = self.encoder.encode(frame_bytes, self.frame_size)
            return encoded
        except Exception as e:
            logging.error(f"Opus: {e}")
            traceback.print_exc()
            return None

    def _convert_bytes_to_shorts(self, bytes_data: bytes) -> np.ndarray:
        """short (16PCM)"""

        return np.frombuffer(bytes_data, dtype=np.int16)

    def _validate_pcm_data(self, pcm_shorts: np.ndarray) -> None:
        """PCM"""

        if np.any((pcm_shorts < -32768) | (pcm_shorts > 32767)):
            invalid_samples = pcm_shorts[(pcm_shorts < -32768) | (pcm_shorts > 32767)]
            logging.warning(f"PCM: {invalid_samples[:5]}...")

            # np.clip(pcm_shorts, -32768, 32767, out=pcm_shorts)

    def close(self):
        """"""
        if hasattr(self, 'encoder') and self.encoder:
            try:
                del self.encoder
                self.encoder = None
            except Exception as e:
                logging.error(f"Error releasing Opus encoder: {e}")