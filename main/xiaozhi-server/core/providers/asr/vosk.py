import os
import json
import time
from typing import Optional, Tuple, List
from .base import ASRProviderBase
from config.logger import setup_logging
from core.providers.asr.dto.dto import InterfaceType
import vosk

TAG = __name__
logger = setup_logging()

class ASRProvider(ASRProviderBase):
    def __init__(self, config: dict, delete_audio_file: bool = True):
        super().__init__()
        self.interface_type = InterfaceType.LOCAL
        self.model_path = config.get("model_path")
        self.output_dir = config.get("output_dir", "tmp/")
        self.delete_audio_file = delete_audio_file
        

        self.model = None
        self.recognizer = None
        self._load_model()
        

        os.makedirs(self.output_dir, exist_ok=True)

    def _load_model(self):
        """VOSK"""
        try:
            if not os.path.exists(self.model_path):
                raise FileNotFoundError(f"VOSK: {self.model_path}")
                
            logger.bind(tag=TAG).info(f"VOSK: {self.model_path}")
            self.model = vosk.Model(self.model_path)


            self.recognizer = vosk.KaldiRecognizer(self.model, 16000)

            logger.bind(tag=TAG).info("VOSK")
        except Exception as e:
            logger.bind(tag=TAG).error(f"VOSK: {e}")
            raise

    async def speech_to_text(
        self, opus_data: List[bytes], session_id: str, audio_format="opus", artifacts=None
    ) -> Tuple[Optional[str], Optional[str]]:
        """"""
        try:

            if not self.model:
                logger.bind(tag=TAG).error("VOSK，")
                return "", None
            
            if artifacts is None:
                return "", None
            if not artifacts.pcm_bytes:
                logger.bind(tag=TAG).warning("PCM")
                return "", None

            start_time = time.time()
            
            

            chunk_size = 2000
            text_result = ""
            
            for i in range(0, len(artifacts.pcm_bytes), chunk_size):
                chunk = artifacts.pcm_bytes[i:i+chunk_size]
                if self.recognizer.AcceptWaveform(chunk):
                    result = json.loads(self.recognizer.Result())
                    text = result.get('text', '')
                    if text:
                        text_result += text + " "
            

            final_result = json.loads(self.recognizer.FinalResult())
            final_text = final_result.get('text', '')
            if final_text:
                text_result += final_text
            
            logger.bind(tag=TAG).debug(
                f"VOSK: {time.time() - start_time:.3f}s | : {text_result.strip()}"
            )
            
            return text_result.strip(), artifacts.file_path
            
        except Exception as e:
            logger.bind(tag=TAG).error(f"VOSK: {e}")
            return "", None
