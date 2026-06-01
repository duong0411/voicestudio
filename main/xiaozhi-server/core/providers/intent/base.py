from abc import ABC, abstractmethod
from typing import List, Dict
from config.logger import setup_logging

TAG = __name__
logger = setup_logging()


class IntentProviderBase(ABC):
    def __init__(self, config):
        self.config = config

    def set_llm(self, llm):
        self.llm = llm

        model_name = getattr(llm, "model_name", str(llm.__class__.__name__))

        logger.bind(tag=TAG).info(f"LLM: {model_name}")

    @abstractmethod
    async def detect_intent(self, conn, dialogue_history: List[Dict], text: str) -> str:
        """
        
        Args:
            dialogue_history: ，rolecontent
        Returns:
            ，:
            - ""
            - ""
            - " "  ""
            - " "  " []"
        """
        pass
