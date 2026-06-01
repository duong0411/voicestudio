import requests
from bs4 import BeautifulSoup
from config.logger import setup_logging
from plugins_func.register import register_function, ToolType, ActionResponse, Action
from core.utils.util import get_ip_info
from typing import TYPE_CHECKING

if TYPE_CHECKING:
    from core.connection import ConnectionHandler

TAG = __name__
logger = setup_logging()

GET_WEATHER_FUNCTION_DESC = {
    "type": "function",
    "function": {
        "name": "get_weather",
        "description": (
            "，，，：。"
            "，。，。"
            "：7，。"
        ),
        "parameters": {
            "type": "object",
            "properties": {
                "location": {
                    "type": "string",
                    "description": "，。，",
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

HEADERS = {
    "User-Agent": (
        "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 "
        "(KHTML, like Gecko) Chrome/92.0.4515.107 Safari/537.36"
    )
}


WEATHER_CODE_MAP = {
    "100": "",
    "101": "",
    "102": "",
    "103": "",
    "104": "",
    "150": "",
    "151": "",
    "152": "",
    "153": "",
    "300": "",
    "301": "",
    "302": "",
    "303": "",
    "304": "",
    "305": "",
    "306": "",
    "307": "",
    "308": "",
    "309": "/",
    "310": "",
    "311": "",
    "312": "",
    "313": "",
    "314": "",
    "315": "",
    "316": "",
    "317": "",
    "318": "",
    "350": "",
    "351": "",
    "399": "",
    "400": "",
    "401": "",
    "402": "",
    "403": "",
    "404": "",
    "405": "",
    "406": "",
    "407": "",
    "408": "",
    "409": "",
    "410": "",
    "456": "",
    "457": "",
    "499": "",
    "500": "",
    "501": "",
    "502": "",
    "503": "",
    "504": "",
    "507": "",
    "508": "",
    "509": "",
    "510": "",
    "511": "",
    "512": "",
    "513": "",
    "514": "",
    "515": "",
    "900": "",
    "901": "",
    "999": "",
}


def fetch_city_info(location, api_key, api_host):
    url = f"https://{api_host}/geo/v2/city/lookup?key={api_key}&location={location}&lang=zh"
    response = requests.get(url, headers=HEADERS).json()
    if response.get("error") is not None:
        logger.bind(tag=TAG).error(
            f"，：{response.get('error', {}).get('detail')}"
        )
        return None
    return response.get("location", [])[0] if response.get("location") else None


def fetch_weather_page(url):
    response = requests.get(url, headers=HEADERS)
    return BeautifulSoup(response.text, "html.parser") if response.ok else None


def parse_weather_info(soup):
    city_name = soup.select_one("h1.c-submenu__location").get_text(strip=True)

    current_abstract = soup.select_one(".c-city-weather-current .current-abstract")
    current_abstract = (
        current_abstract.get_text(strip=True) if current_abstract else ""
    )

    current_basic = {}
    for item in soup.select(
        ".c-city-weather-current .current-basic .current-basic___item"
    ):
        parts = item.get_text(strip=True, separator=" ").split(" ")
        if len(parts) == 2:
            key, value = parts[1], parts[0]
            current_basic[key] = value

    temps_list = []
    for row in soup.select(".city-forecast-tabs__row")[:7]:
        date = row.select_one(".date-bg .date").get_text(strip=True)
        weather_code = (
            row.select_one(".date-bg .icon")["src"].split("/")[-1].split(".")[0]
        )
        weather = WEATHER_CODE_MAP.get(weather_code, "")
        temps = [span.get_text(strip=True) for span in row.select(".tmp-cont .temp")]
        high_temp, low_temp = (temps[0], temps[-1]) if len(temps) >= 2 else (None, None)
        temps_list.append((date, weather, high_temp, low_temp))

    return city_name, current_abstract, current_basic, temps_list


@register_function("get_weather", GET_WEATHER_FUNCTION_DESC, ToolType.SYSTEM_CTL)
def get_weather(conn: "ConnectionHandler", location: str = None, lang: str = "zh_CN"):
    from core.utils.cache.manager import cache_manager, CacheType

    weather_config = conn.config.get("plugins", {}).get("get_weather", {})
    api_host = weather_config.get("api_host", "mj7p3y7naa.re.qweatherapi.com")
    api_key = weather_config.get("api_key", "a861d0d5e7bf4ee1a83d9a9e4f96d4da")
    default_location = weather_config.get("default_location", "")
    client_ip = conn.client_ip


    if not location:

        if client_ip:

            cached_ip_info = cache_manager.get(CacheType.IP_INFO, client_ip)
            if cached_ip_info:
                location = cached_ip_info.get("city")
            else:

                ip_info = get_ip_info(client_ip, logger)
                if ip_info:
                    cache_manager.set(CacheType.IP_INFO, client_ip, ip_info)
                    location = ip_info.get("city")

            if not location:
                location = default_location
        else:

            location = default_location

    weather_cache_key = f"full_weather_{location}_{lang}"
    cached_weather_report = cache_manager.get(CacheType.WEATHER, weather_cache_key)
    if cached_weather_report:
        return ActionResponse(Action.REQLLM, cached_weather_report, None)


    city_info = fetch_city_info(location, api_key, api_host)
    if not city_info:
        return ActionResponse(
            Action.REQLLM, f": {location}，", None
        )
    soup = fetch_weather_page(city_info["fxLink"])
    if not soup:
        return ActionResponse(Action.REQLLM, None, "")
    city_name, current_abstract, current_basic, temps_list = parse_weather_info(soup)

    weather_report = f"：{city_name}\n\n: {current_abstract}\n"


    if current_basic:
        weather_report += "：\n"
        for key, value in current_basic.items():
            if value != "0":
                weather_report += f"  · {key}: {value}\n"


    weather_report += "\n7：\n"
    for date, weather, high, low in temps_list:
        weather_report += f"{date}: {weather}， {low}~{high}\n"


    weather_report += "\n（，）"


    cache_manager.set(CacheType.WEATHER, weather_cache_key, weather_report)

    return ActionResponse(Action.REQLLM, weather_report, None)
