import requests
from requests.exceptions import RequestException
from config.logger import setup_logging
from core.providers.llm.base import LLMProviderBase

TAG = __name__
logger = setup_logging()


class LLMProvider(LLMProviderBase):
    def __init__(self, config):
        self.agent_id = config.get("agent_id")
        self.api_key = config.get("api_key")
        self.base_url = config.get("base_url", config.get("url"))
        self.api_url = f"{self.base_url}/api/conversation/process"

    def response(self, session_id, dialogue, **kwargs):



        input_text = None
        if isinstance(dialogue, list):

            for message in reversed(dialogue):
                if message.get("role") == "user":
                    input_text = message.get("content", "")
                    break


        payload = {
            "text": input_text,
            "agent_id": self.agent_id,
            "conversation_id": session_id,
        }

        headers = {
            "Authorization": f"Bearer {self.api_key}",
            "Content-Type": "application/json",
        }


        with requests.post(self.api_url, json=payload, headers=headers) as response:

            response.raise_for_status()


            data = response.json()
        speech = (
            data.get("response", {})
            .get("speech", {})
            .get("plain", {})
            .get("speech", "")
        )


        if speech:
            yield speech
        else:
            logger.bind(tag=TAG).warning("API  speech ")

    def response_with_functions(self, session_id, dialogue, functions=None):
        logger.bind(tag=TAG).error(
            f"homeassistant（function call），"
        )
