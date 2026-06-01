import random
import requests
import json
from config.logger import setup_logging
from plugins_func.register import register_function, ToolType, ActionResponse, Action
from markitdown import MarkItDown
from typing import TYPE_CHECKING

if TYPE_CHECKING:
    from core.connection import ConnectionHandler


TAG = __name__
logger = setup_logging()

CHANNEL_MAP = {
    "V2EX": "v2ex-share",
    "": "zhihu",
    "": "weibo",
    "": "zaobao",
    "": "coolapk",
    "MKTNews": "mktnews-flash",
    "": "wallstreetcn-quick",
    "36": "36kr-quick",
    "": "douyin",
    "": "hupu",
    "": "tieba",
    "": "toutiao",
    "IT": "ithome",
    "": "thepaper",
    "": "sputniknewscn",
    "": "cankaoxiaoxi",
    "": "pcbeta-windows11",
    "": "cls-depth",
    "": "xueqiu-hotstock",
    "": "gelonghui",
    "": "fastbull-express",
    "Solidot": "solidot",
    "Hacker News": "hackernews",
    "Product Hunt": "producthunt",
    "Github": "github-trending-today",
    "": "bilibili-hot-search",
    "": "kuaishou",
    "": "kaopu",
    "": "jin10",
    "": "baidu",
    "": "nowcoder",
    "": "sspai",
    "": "juejin",
    "": "ifeng",
    "": "chongbuluo-latest",
}


DEFAULT_NEWS_SOURCES = ";;"

def _get_newsnow_config(conn):

    plugins = conn.config.get("plugins", {})
    newsnow = plugins.get("get_news_from_newsnow", {})
    sources = newsnow.get("news_sources", "")
    if isinstance(sources, str) and sources.strip():
        return sources

    return ""

def get_news_sources_from_config(conn):
    """"""
    try:
        result = _get_newsnow_config(conn)
        if result:
            logger.bind(tag=TAG).debug(f": {result}")
            return result

        logger.bind(tag=TAG).debug("，")
        return DEFAULT_NEWS_SOURCES

    except Exception as e:
        logger.bind(tag=TAG).error(f": {e}，")
        return DEFAULT_NEWS_SOURCES



example_sources_str = DEFAULT_NEWS_SOURCES.replace(";","、")

GET_NEWS_FROM_NEWSNOW_FUNCTION_DESC = {
    "type": "function",
    "function": {
        "name": "get_news_from_newsnow",
        "description": "（''''）。",
        "parameters": {
            "type": "object",
            "properties": {
                "source": {
                    "type": "string",
                    "description": f"，{example_sources_str}。，",
                },
                "detail": {
                    "type": "boolean",
                    "description": "，false。true，",
                },
                "lang": {
                    "type": "string",
                    "description": "code，zh_CN/zh_HK/en_US/ja_JP，zh_CN",
                },
            },
            "required": ["lang"],
        },
    },
}


def fetch_news_from_api(conn: "ConnectionHandler", source="thepaper"):
    """API"""
    try:
        api_url = f"https://newsnow.busiyi.world/api/s?id={source}"

        news_config = conn.config.get("plugins", {}).get("get_news_from_newsnow", {})
        if news_config.get("url"):
            api_url = news_config["url"] + source

        headers = {"User-Agent": "Mozilla/5.0"}
        response = requests.get(api_url, headers=headers, timeout=10)
        response.raise_for_status()

        data = response.json()

        if "items" in data:
            return data["items"]
        else:
            logger.bind(tag=TAG).error(f"API: {data}")
            return []

    except Exception as e:
        logger.bind(tag=TAG).error(f"API: {e}")
        return []


def fetch_news_detail(url):
    """MarkItDownHTML"""
    try:
        headers = {"User-Agent": "Mozilla/5.0"}
        response = requests.get(url, headers=headers, timeout=10)
        response.raise_for_status()


        md = MarkItDown(enable_plugins=False)
        result = md.convert(response)


        clean_text = result.text_content


        if not clean_text or len(clean_text.strip()) == 0:
            logger.bind(tag=TAG).warning(f": {url}")
            return "，。"

        return clean_text
    except Exception as e:
        logger.bind(tag=TAG).error(f": {e}")
        return ""


@register_function(
    "get_news_from_newsnow",
    GET_NEWS_FROM_NEWSNOW_FUNCTION_DESC,
    ToolType.SYSTEM_CTL,
)
def get_news_from_newsnow(
    conn: "ConnectionHandler",
    source: str = "",
    detail: bool = False,
    lang: str = "zh_CN",
):
    """，"""
    try:

        news_sources = get_news_sources_from_config(conn)


        detail = str(detail).lower() == "true"
        if detail:
            if (
                not hasattr(conn, "last_newsnow_link")
                or not conn.last_newsnow_link
                or "url" not in conn.last_newsnow_link
            ):
                return ActionResponse(
                    Action.REQLLM,
                    "，，。",
                    None,
                )

            url = conn.last_newsnow_link.get("url")
            title = conn.last_newsnow_link.get("title", "")
            source_id = conn.last_newsnow_link.get("source_id", "thepaper")
            source_name = CHANNEL_MAP.get(source_id, "")

            if not url or url == "#":
                return ActionResponse(
                    Action.REQLLM, "，。", None
                )

            logger.bind(tag=TAG).debug(
                f": {title}, : {source_name}, URL={url}"
            )


            detail_content = fetch_news_detail(url)

            if not detail_content or detail_content == "":
                return ActionResponse(
                    Action.REQLLM,
                    f"，《{title}》，。",
                    None,
                )


            detail_report = (
                f"，{lang}：\n\n"
                f": {title}\n"

                f": {detail_content}\n\n"
                f"(，，、，"
                f"，)"
            )

            return ActionResponse(Action.REQLLM, detail_report, None)



        english_source_id = None


        news_sources_list = [
            name.strip() for name in news_sources.split(";") if name.strip()
        ]
        if source in news_sources_list:

            english_source_id = CHANNEL_MAP.get(source)


        if not english_source_id:
            logger.bind(tag=TAG).warning(f": {source}，")
            english_source_id = "thepaper"
            source = ""

        logger.bind(tag=TAG).info(f": ={source}({english_source_id})")


        news_items = fetch_news_from_api(conn, english_source_id)

        if not news_items:
            return ActionResponse(
                Action.REQLLM,
                f"，{source}，。",
                None,
            )


        selected_news = random.choice(news_items)


        if not hasattr(conn, "last_newsnow_link"):
            conn.last_newsnow_link = {}
        conn.last_newsnow_link = {
            "url": selected_news.get("url", "#"),
            "title": selected_news.get("title", ""),
            "source_id": english_source_id,
        }


        news_report = (
            f"，{lang}：\n\n"
            f": {selected_news['title']}\n"

            f"(、，"
            f"，。)"
        )

        return ActionResponse(Action.REQLLM, news_report, None)

    except Exception as e:
        logger.bind(tag=TAG).error(f": {e}")
        return ActionResponse(
            Action.REQLLM, "，，。", None
        )
