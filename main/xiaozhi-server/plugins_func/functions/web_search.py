import requests
from config.logger import setup_logging
from plugins_func.register import (
    register_function,
    ToolType,
    ActionResponse,
    Action,
)
from typing import TYPE_CHECKING

if TYPE_CHECKING:
    from core.connection import ConnectionHandler

TAG = __name__
logger = setup_logging()

_DEFAULT_DESCRIPTION = (
    "。。"
)

WEB_SEARCH_FUNCTION_DESC = {
    "type": "function",
    "function": {
        "name": "web_search",
        "description": _DEFAULT_DESCRIPTION,
        "parameters": {
            "type": "object",
            "properties": {
                "query": {
                    "type": "string",
                    "description": "",
                }
            },
            "required": ["query"],
        },
    },
}


def _search_metaso(api_key: str, query: str, max_results: int) -> str:
    """API"""
    url = "https://metaso.cn/api/v1/search"
    headers = {
        "Authorization": f"Bearer {api_key}",
        "Content-Type": "application/json",
    }
    payload = {
        "q": query,
        "size": max_results,
        "stream": False,
        "scope": "webpage",
        "includeSummary": True,
        "includeRawContent": False,
        "conciseSnippet": False,
    }
    logger.bind(tag=TAG).debug(f" | URL: {url} | payload: {payload}")
    response = requests.post(url, json=payload, headers=headers, timeout=15)
    response.raise_for_status()
    data = response.json()
    logger.bind(tag=TAG).debug(f" | status: {response.status_code}")

    webpages = data.get("webpages", [])
    if not webpages:
        return "。"

    lines = ["【】"]
    for i, item in enumerate(webpages, 1):
        title = item.get("title", "")
        snippet = item.get("summary", "")
        date = item.get("date", "")
        lines.append(f"{i}. ：{title}")
        if date:
            lines.append(f"   ：{date}")
        if snippet:
            lines.append(f"   ：{snippet}")

    return "\n".join(lines)


def _search_tavily(api_key: str, query: str, max_results: int) -> str:
    """TavilyAPI"""
    url = "https://api.tavily.com/search"
    headers = {
        "Authorization": f"Bearer {api_key}",
        "Content-Type": "application/json",
    }
    payload = {
        "query": query,
        "max_results": max_results,
        "search_depth": "advanced",
        "include_answer": "advanced",
    }
    logger.bind(tag=TAG).debug(f"Tavily | URL: {url} | payload: {payload}")
    response = requests.post(url, json=payload, headers=headers, timeout=15)
    response.raise_for_status()
    data = response.json()
    logger.bind(tag=TAG).debug(f"Tavily | status: {response.status_code} | data: {data}")

    results = data.get("results", [])
    if not results:
        return "。"

    answer = data.get("answer", "")
    lines = [f"【】\n：{answer}"]
    # for i, item in enumerate(results, 1):

    #     summary = item.get("content", "")

    #     if summary:


    return "\n".join(lines)


@register_function("web_search", WEB_SEARCH_FUNCTION_DESC, ToolType.SYSTEM_CTL)
def web_search(conn: "ConnectionHandler", query: str = None):
    logger.bind(tag=TAG).info(f"web_search  | query={query}")
    if not query:
        return ActionResponse(Action.REQLLM, "。", None)

    web_search_config = conn.config.get("plugins", {}).get("web_search", {})
    provider = web_search_config.get("provider", "").lower()
    max_results = int(web_search_config.get("max_results", 3))
    logger.bind(tag=TAG).info(f"web_search  | provider={provider} | max_results={max_results} | config_keys={list(web_search_config.keys())}")

    api_key = web_search_config.get("api_key", "")
    if not api_key:
        return ActionResponse(
            Action.REQLLM,
            "API Key，。",
            None,
        )

    if provider == "metaso":
        search_fn = lambda: _search_metaso(api_key, query, max_results)
    elif provider == "tavily":
        search_fn = lambda: _search_tavily(api_key, query, max_results)
    else:
        return ActionResponse(
            Action.REQLLM,
            f"（：{provider}），。",
            None,
        )

    try:
        result_text = search_fn()
        logger.bind(tag=TAG).info(f":\n{result_text}")
    except requests.exceptions.Timeout:
        logger.bind(tag=TAG).error("")
        result_text = "，。"
    except requests.exceptions.RequestException as e:
        logger.bind(tag=TAG).error(f": {e}")
        result_text = "，。"
    except Exception as e:
        logger.bind(tag=TAG).error(f": {e}")
        result_text = "，。"

    return ActionResponse(Action.REQLLM, result_text, None)
