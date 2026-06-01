import random
import requests
import xml.etree.ElementTree as ET
from bs4 import BeautifulSoup
from config.logger import setup_logging
from plugins_func.register import register_function, ToolType, ActionResponse, Action
from typing import TYPE_CHECKING

if TYPE_CHECKING:
    from core.connection import ConnectionHandler


TAG = __name__
logger = setup_logging()

GET_NEWS_FROM_CHINANEWS_FUNCTION_DESC = {
    "type": "function",
    "function": {
        "name": "get_news_from_chinanews",
        "description": (
            "（''''）。"
            "，、、。"
            "，。"
        ),
        "parameters": {
            "type": "object",
            "properties": {
                "category": {
                    "type": "string",
                    "description": "，、、。，",
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


def fetch_news_from_rss(rss_url):
    """RSS"""
    try:
        response = requests.get(rss_url)
        response.raise_for_status()


        root = ET.fromstring(response.content)


        news_items = []
        for item in root.findall(".//item"):
            title = (
                item.find("title").text if item.find("title") is not None else ""
            )
            link = item.find("link").text if item.find("link") is not None else "#"
            description = (
                item.find("description").text
                if item.find("description") is not None
                else ""
            )
            pubDate = (
                item.find("pubDate").text
                if item.find("pubDate") is not None
                else ""
            )

            news_items.append(
                {
                    "title": title,
                    "link": link,
                    "description": description,
                    "pubDate": pubDate,
                }
            )

        return news_items
    except Exception as e:
        logger.bind(tag=TAG).error(f"RSS: {e}")
        return []


def fetch_news_detail(url):
    """"""
    try:
        response = requests.get(url)
        response.raise_for_status()

        soup = BeautifulSoup(response.content, "html.parser")


        content_div = soup.select_one(
            ".content_desc, .content, article, .article-content"
        )
        if content_div:
            paragraphs = content_div.find_all("p")
            content = "\n".join(
                [p.get_text().strip() for p in paragraphs if p.get_text().strip()]
            )
            return content
        else:

            paragraphs = soup.find_all("p")
            content = "\n".join(
                [p.get_text().strip() for p in paragraphs if p.get_text().strip()]
            )
            return content[:2000]
    except Exception as e:
        logger.bind(tag=TAG).error(f": {e}")
        return ""


def map_category(category_text):
    """"""
    if not category_text:
        return None


    category_map = {

        "": "society_rss_url",
        "": "society_rss_url",

        "": "world_rss_url",
        "": "world_rss_url",

        "": "finance_rss_url",
        "": "finance_rss_url",
        "": "finance_rss_url",
        "": "finance_rss_url",
    }


    normalized_category = category_text.lower().strip()


    return category_map.get(normalized_category, category_text)


@register_function(
    "get_news_from_chinanews",
    GET_NEWS_FROM_CHINANEWS_FUNCTION_DESC,
    ToolType.SYSTEM_CTL,
)
def get_news_from_chinanews(
    conn: "ConnectionHandler",
    category: str = None,
    detail: bool = False,
    lang: str = "zh_CN",
):
    """，"""
    try:

        if detail:
            if (
                not hasattr(conn, "last_news_link")
                or not conn.last_news_link
                or "link" not in conn.last_news_link
            ):
                return ActionResponse(
                    Action.REQLLM,
                    "，，。",
                    None,
                )

            link = conn.last_news_link.get("link")
            title = conn.last_news_link.get("title", "")

            if link == "#":
                return ActionResponse(
                    Action.REQLLM, "，。", None
                )

            logger.bind(tag=TAG).debug(f": {title}, URL={link}")


            detail_content = fetch_news_detail(link)

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



        rss_config = conn.config.get("plugins", {}).get("get_news_from_chinanews", {})
        default_rss_url = rss_config.get(
            "default_rss_url", "https://www.chinanews.com.cn/rss/society.xml"
        )


        mapped_category = map_category(category)


        rss_url = default_rss_url
        if mapped_category and mapped_category in rss_config:
            rss_url = rss_config[mapped_category]

        logger.bind(tag=TAG).info(
            f": ={category}, ={mapped_category}, URL={rss_url}"
        )


        news_items = fetch_news_from_rss(rss_url)

        if not news_items:
            return ActionResponse(
                Action.REQLLM, "，，。", None
            )


        selected_news = random.choice(news_items)


        if not hasattr(conn, "last_news_link"):
            conn.last_news_link = {}
        conn.last_news_link = {
            "link": selected_news.get("link", "#"),
            "title": selected_news.get("title", ""),
        }


        news_report = (
            f"，{lang}：\n\n"
            f": {selected_news['title']}\n"
            f": {selected_news['pubDate']}\n"
            f": {selected_news['description']}\n"
            f"(、，，"
            f"，。"
            f"，'')"
        )

        return ActionResponse(Action.REQLLM, news_report, None)

    except Exception as e:
        logger.bind(tag=TAG).error(f": {e}")
        return ActionResponse(
            Action.REQLLM, "，，。", None
        )
