from typing import List, Dict, TYPE_CHECKING

if TYPE_CHECKING:
    from core.connection import ConnectionHandler
from ..base import IntentProviderBase
from plugins_func.functions.play_music import initialize_music_handler
from config.logger import setup_logging
from core.utils.util import get_system_error_response
import re
import json
import hashlib
import time



TAG = __name__
logger = setup_logging()


class IntentProvider(IntentProviderBase):
    def __init__(self, config):
        super().__init__(config)
        self.llm = None
        self.promot = ""

        from core.utils.cache.manager import cache_manager, CacheType

        self.cache_manager = cache_manager
        self.CacheType = CacheType
        self.history_count = 4

    def get_intent_system_prompt(self, functions_list: str) -> str:
        """
        
        Args:
            functions: ，JSON
        Returns:
            
        """


        functions_desc = "：\n"
        for func in functions_list:
            func_info = func.get("function", {})
            name = func_info.get("name", "")
            desc = func_info.get("description", "")
            params = func_info.get("parameters", {})

            functions_desc += f"\n: {name}\n"
            functions_desc += f": {desc}\n"

            if params:
                functions_desc += ":\n"
                for param_name, param_info in params.get("properties", {}).items():
                    param_desc = param_info.get("description", "")
                    param_type = param_info.get("type", "")
                    functions_desc += f"- {param_name} ({param_type}): {param_desc}\n"

            functions_desc += "---\n"

        prompt = (
            "【】JSON，！\n\n"
            "。，。\n\n"
            "【】result_for_context，：\n"
            "- （：、、）\n"
            "- （：、、）\n"
            "- （：、）\n"
            "- （：、）"
            "。\n\n"
            "- （''、''、''）（'？'），， {'function_call': {'name': 'continue_chat'}\n"
            "- ''、''、''， handle_exit_intent\n\n"
            f"{functions_desc}\n"
            ":\n"
            "1. ，\n"
            "2. （、），result_for_context\n"
            "3. \n"
            "4. ，function_call \n"
            '5. ，{"function_call": {"name": "continue_chat"}}\n\n'
            "：\n"
            "1. JSON，\n"
            "2. function_call\n"
            "3. function_callname\n"
            "4. ，arguments\n\n"
            "：\n"
            "```\n"
            ": ？\n"
            ': {"function_call": {"name": "result_for_context"}}\n'
            "```\n"
            "```\n"
            ": ？\n"
            ': {"function_call": {"name": "get_battery_level", "arguments": {"response_success": "{value}%", "response_failure": "Battery"}}}\n'
            "```\n"
            "```\n"
            ": ？\n"
            ': {"function_call": {"name": "self_screen_get_brightness"}}\n'
            "```\n"
            "```\n"
            ": 50%\n"
            ': {"function_call": {"name": "self_screen_set_brightness", "arguments": {"brightness": 50}}}\n'
            "```\n"
            "```\n"
            ": \n"
            ': {"function_call": {"name": "handle_exit_intent", "arguments": {"say_goodbye": "goodbye"}}}\n'
            "```\n"
            "```\n"
            ": \n"
            ': {"function_call": {"name": "continue_chat"}}\n'
            "```\n\n"
            "：\n"
            "1. JSON，\n"
            '2. （、），{"function_call": {"name": "result_for_context"}}，arguments\n'
            '3. ，{"function_call": {"name": "continue_chat"}}\n'
            "4. JSON，\n"
            "5. result_for_context，\n"
            "：\n"
            "- （''）\n"
            "- function_callJSON\n"
            "- ：{'function_calls': [{name:'light_on'}, {name:'volume_up'}]}\n\n"
            "【】、！JSON！！"
        )
        return prompt

    def replyResult(self, text: str, original_text: str):
        try:
            llm_result = self.llm.response_no_stream(
                system_prompt=text,
                user_prompt="，，，。："
                + original_text,
            )
            return llm_result
        except Exception as e:
            logger.bind(tag=TAG).error(f"Error in generating reply result: {e}")
            return get_system_error_response(self.config)

    async def detect_intent(
        self, conn: "ConnectionHandler", dialogue_history: List[Dict], text: str
    ) -> str:
        if not self.llm:
            raise ValueError("LLM provider not set")
        if conn.func_handler is None:
            return '{"function_call": {"name": "continue_chat"}}'


        total_start_time = time.time()


        model_info = getattr(self.llm, "model_name", str(self.llm.__class__.__name__))
        logger.bind(tag=TAG).debug(f": {model_info}")


        cache_key = hashlib.md5((conn.device_id + text).encode()).hexdigest()


        cached_intent = self.cache_manager.get(self.CacheType.INTENT, cache_key)
        if cached_intent is not None:
            cache_time = time.time() - total_start_time
            logger.bind(tag=TAG).debug(
                f": {cache_key} -> {cached_intent}, : {cache_time:.4f}"
            )
            return cached_intent

        if self.promot == "":
            functions = conn.func_handler.get_functions()
            if hasattr(conn, "mcp_client"):
                mcp_tools = conn.mcp_client.get_available_tools()
                if mcp_tools is not None and len(mcp_tools) > 0:
                    if functions is None:
                        functions = []
                    functions.extend(mcp_tools)

            self.promot = self.get_intent_system_prompt(functions)

        music_config = initialize_music_handler(conn)
        music_file_names = music_config["music_file_names"]
        prompt_music = f"{self.promot}\n<musicNames>{music_file_names}\n</musicNames>"

        home_assistant_cfg = conn.config["plugins"].get("home_assistant")
        if home_assistant_cfg:
            devices = home_assistant_cfg.get("devices", [])
        else:
            devices = []
        if len(devices) > 0:
            hass_prompt = "\n（，，entity_id），homeassistant\n"
            for device in devices:
                hass_prompt += device + "\n"
            prompt_music += hass_prompt

        logger.bind(tag=TAG).debug(f"User prompt: {prompt_music}")


        msgStr = ""


        start_idx = max(0, len(dialogue_history) - self.history_count)
        for i in range(start_idx, len(dialogue_history)):
            msgStr += f"{dialogue_history[i].role}: {dialogue_history[i].content}\n"

        msgStr += f"User: {text}\n"
        user_prompt = f"current dialogue:\n{msgStr}"


        preprocess_time = time.time() - total_start_time
        logger.bind(tag=TAG).debug(f": {preprocess_time:.4f}")


        llm_start_time = time.time()
        logger.bind(tag=TAG).debug(f"LLM, : {model_info}")

        try:
            intent = self.llm.response_no_stream(
                system_prompt=prompt_music, user_prompt=user_prompt
            )
        except Exception as e:
            logger.bind(tag=TAG).error(f"Error in intent detection LLM call: {e}")
            return '{"function_call": {"name": "continue_chat"}}'


        llm_time = time.time() - llm_start_time
        logger.bind(tag=TAG).debug(
            f", : {model_info}, : {llm_time:.4f}"
        )


        postprocess_start_time = time.time()


        intent = intent.strip()

        match = re.search(r"\{.*\}", intent, re.DOTALL)
        if match:
            intent = match.group(0)


        total_time = time.time() - total_start_time
        logger.bind(tag=TAG).debug(
            f"【】: {model_info}, : {total_time:.4f}, LLM: {llm_time:.4f}, : '{text[:20]}...'"
        )


        try:
            intent_data = json.loads(intent)

            if "function_call" in intent_data:
                function_data = intent_data["function_call"]
                function_name = function_data.get("name")
                function_args = function_data.get("arguments", {})


                logger.bind(tag=TAG).info(
                    f"llm : {function_name}, : {function_args}"
                )


                if function_name == "result_for_context":

                    logger.bind(tag=TAG).info(
                        "result_for_context，"
                    )

                elif function_name == "continue_chat":


                    clean_history = [
                        msg
                        for msg in conn.dialogue.dialogue
                        if msg.role not in ["tool", "function"]
                    ]
                    conn.dialogue.dialogue = clean_history

                else:

                    logger.bind(tag=TAG).info(f": {function_name}")


            self.cache_manager.set(self.CacheType.INTENT, cache_key, intent)
            postprocess_time = time.time() - postprocess_start_time
            logger.bind(tag=TAG).debug(f": {postprocess_time:.4f}")
            return intent
        except json.JSONDecodeError:

            postprocess_time = time.time() - postprocess_start_time
            logger.bind(tag=TAG).error(
                f"JSON: {intent}, : {postprocess_time:.4f}"
            )

            return '{"function_call": {"name": "continue_chat"}}'
