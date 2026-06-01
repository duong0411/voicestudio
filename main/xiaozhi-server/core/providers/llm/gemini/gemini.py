import os, json, uuid
from types import SimpleNamespace
from typing import Any, Dict, List

import requests
from google import generativeai as genai
from google.generativeai import types, GenerationConfig

from core.providers.llm.base import LLMProviderBase
from core.utils.util import check_model_key
from config.logger import setup_logging
from google.generativeai.types import GenerateContentResponse
from requests import RequestException

log = setup_logging()
TAG = __name__


def test_proxy(proxy_url: str, test_url: str) -> bool:
    try:
        resp = requests.get(test_url, proxies={"http": proxy_url, "https": proxy_url})
        return 200 <= resp.status_code < 400
    except RequestException:
        return False


def setup_proxy_env(http_proxy: str | None, https_proxy: str | None):
    """
     HTTP  HTTPS ，。
     HTTPS  HTTP ， HTTPS_PROXY  HTTP。
    """
    test_http_url = "http://www.google.com"
    test_https_url = "https://www.google.com"

    ok_http = ok_https = False

    if http_proxy:
        ok_http = test_proxy(http_proxy, test_http_url)
        if ok_http:
            os.environ["HTTP_PROXY"] = http_proxy
            log.bind(tag=TAG).info(f"Gemini HTTPS: {http_proxy}")
        else:
            log.bind(tag=TAG).warning(f"Gemini HTTP: {http_proxy}")

    if https_proxy:
        ok_https = test_proxy(https_proxy, test_https_url)
        if ok_https:
            os.environ["HTTPS_PROXY"] = https_proxy
            log.bind(tag=TAG).info(f"Gemini HTTPS: {https_proxy}")
        else:
            log.bind(tag=TAG).warning(
                f"Gemini HTTPS: {https_proxy}"
            )


    if ok_http and not ok_https:
        if test_proxy(http_proxy, test_https_url):
            os.environ["HTTPS_PROXY"] = http_proxy
            ok_https = True
            log.bind(tag=TAG).info(f"HTTPHTTPS: {http_proxy}")

    if not ok_http and not ok_https:
        log.bind(tag=TAG).error(
            f"Gemini : HTTP  HTTPS ，"
        )
        raise RuntimeError("HTTP  HTTPS ，")


class LLMProvider(LLMProviderBase):
    def __init__(self, cfg: Dict[str, Any]):
        self.model_name = cfg.get("model_name", "gemini-3-flash")
        self.api_key = cfg["api_key"]
        http_proxy = cfg.get("http_proxy")
        https_proxy = cfg.get("https_proxy")

        model_key_msg = check_model_key("LLM", self.api_key)
        if model_key_msg:
            log.bind(tag=TAG).error(model_key_msg)

        if http_proxy or https_proxy:
            log.bind(tag=TAG).info(
                f"Gemini，..."
            )
            setup_proxy_env(http_proxy, https_proxy)
            log.bind(tag=TAG).info(
                f"Gemini  - HTTP: {http_proxy}, HTTPS: {https_proxy}"
            )

        genai.configure(api_key=self.api_key)
        self.timeout = cfg.get("timeout", 120)
        self.model = genai.GenerativeModel(self.model_name)

        self.gen_cfg = GenerationConfig(
            temperature=0.7,
            top_p=0.9,
            top_k=40,
            max_output_tokens=2048,
        )

    @staticmethod
    def _sanitize_schema(schema: Any) -> Any:
        if not isinstance(schema, dict):
            return schema
        allowed_keys = {
            "type",
            "format",
            "description",
            "nullable",
            "enum",
            "items",
            "properties",
            "required",
        }
        cleaned = {}
        for k, v in schema.items():
            if k in allowed_keys:
                if k == "properties" and isinstance(v, dict):
                    cleaned[k] = {pk: LLMProvider._sanitize_schema(pv) for pk, pv in v.items()}
                elif k == "items" and isinstance(v, dict):
                    cleaned[k] = LLMProvider._sanitize_schema(v)
                else:
                    cleaned[k] = v
        return cleaned

    def _build_tools(self, funcs: List[Dict[str, Any]] | None):
        if not funcs:
            return None
        self.tool_name_map = {}
        decls = []
        for f in funcs:
            try:
                orig_name = f["function"]["name"]
                # Sanitize name to match Gemini requirements: ^[a-zA-Z0-9_]*$
                import re
                sanitized_name = re.sub(r'[^a-zA-Z0-9_]', '_', orig_name)
                self.tool_name_map[sanitized_name] = orig_name
                params = LLMProvider._sanitize_schema(f["function"].get("parameters"))
                # Ensure parameters has required structure for Gemini
                if params and isinstance(params, dict):
                    if "type" not in params:
                        params["type"] = "object"
                    if "properties" not in params:
                        params["properties"] = {}
                else:
                    params = {"type": "object", "properties": {}}
                desc = f["function"].get("description", "") or ""
                decls.append(
                    types.FunctionDeclaration(
                        name=sanitized_name,
                        description=desc,
                        parameters=params,
                    )
                )
            except Exception as e:
                log.warning(TAG, f"Skipping malformed function declaration: {f.get('function', {}).get('name', 'unknown')}, error: {e}")
                continue
        if not decls:
            return None
        try:
            return [types.Tool(function_declarations=decls)]
        except ValueError as e:
            log.warning(TAG, f"Failed to build Tool with function_declarations, disabling tools: {e}")
            return None


    def response(self, session_id, dialogue, **kwargs):
        yield from self._generate(dialogue, None, expect_functions=False)

    def response_with_functions(self, session_id, dialogue, functions=None):
        yield from self._generate(dialogue, self._build_tools(functions), expect_functions=True)

    def _generate(self, dialogue, tools, expect_functions=False):
        role_map = {"assistant": "model", "user": "user"}
        contents: list = []
        system_instruction_parts = []

        for m in dialogue:
            r = m["role"]
            if r == "system":
                system_instruction_parts.append(str(m.get("content", "")))
                continue

            if r == "assistant" and "tool_calls" in m:
                tc = m["tool_calls"][0]
                contents.append(
                    {
                        "role": "model",
                        "parts": [
                            {
                                "function_call": {
                                    "name": tc["function"]["name"],
                                    "args": json.loads(tc["function"]["arguments"]),
                                }
                            }
                        ],
                    }
                )
                continue

            if r == "tool":
                contents.append(
                    {
                        "role": "model",
                        "parts": [{"text": str(m.get("content", ""))}],
                    }
                )
                continue

            contents.append(
                {
                    "role": role_map.get(r, "user"),
                    "parts": [{"text": str(m.get("content", ""))}],
                }
            )
        # Merge consecutive turns of the same role
        merged_contents = []
        for content in contents:
            if merged_contents and merged_contents[-1]["role"] == content["role"]:
                merged_contents[-1]["parts"].extend(content["parts"])
            else:
                merged_contents.append(content)

        system_instruction = "\n".join(system_instruction_parts) if system_instruction_parts else None

        try:
            genai.configure(api_key=self.api_key)
            model = genai.GenerativeModel(self.model_name, system_instruction=system_instruction)
            stream: GenerateContentResponse = model.generate_content(
                contents=merged_contents,
                generation_config=self.gen_cfg,
                tools=tools,
                stream=True,
                request_options={"timeout": self.timeout},
            )

            for chunk in stream:
                cand = chunk.candidates[0]
                for part in cand.content.parts:

                    if getattr(part, "function_call", None):
                        fc = part.function_call
                        # Map back to original name if found
                        orig_name = getattr(self, "tool_name_map", {}).get(fc.name, fc.name)
                        yield None, [
                            SimpleNamespace(
                                id=uuid.uuid4().hex,
                                type="function",
                                function=SimpleNamespace(
                                    name=orig_name,
                                    arguments=json.dumps(
                                        dict(fc.args), ensure_ascii=False
                                    ),
                                ),
                            )
                        ]
                        return

                    if getattr(part, "text", None):
                        yield (part.text, None) if expect_functions else part.text

        except Exception as e:
            import traceback
            log.bind(tag=TAG).error(
                f"Gemini generation error: {e}\nTraceback:\n{traceback.format_exc()}\nContents:\n{json.dumps(merged_contents, ensure_ascii=False)}"
            )
            raise e
        finally:
            if expect_functions:
                yield None, None


    @staticmethod
    def _safe_finish_stream(stream: GenerateContentResponse):
        if hasattr(stream, "resolve"):
            stream.resolve()  # Gemini SDK version ≥ 0.5.0
        elif hasattr(stream, "close"):
            stream.close()  # Gemini SDK version < 0.5.0
        else:
            for _ in stream:
                pass
