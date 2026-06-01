import re
from config.logger import setup_logging

TAG = __name__
logger = setup_logging()

EMOTION_EMOJI_MAP = {
    "HAPPY": "🙂",
    "SAD": "😔",
    "ANGRY": "😡",
    "NEUTRAL": "😶",
    "FEARFUL": "😰",
    "DISGUSTED": "🤢",
    "SURPRISED": "😲",
    "EMO_UNKNOWN": "😶",
}
# EVENT_EMOJI_MAP = {
#     "<|BGM|>": "🎼",
#     "<|Speech|>": "",
#     "<|Applause|>": "👏",
#     "<|Laughter|>": "😀",
#     "<|Cry|>": "😭",
#     "<|Sneeze|>": "🤧",
#     "<|Breath|>": "",
#     "<|Cough|>": "🤧",
# }

def lang_tag_filter(text: str) -> dict | str:
    """
     FunASR ，

    Args:
        text: ASR ，

    Returns:
        dict: {"language": "zh", "emotion": "SAD", "emoji": "😔", "content": ""} 
        str: ，

    Examples:
        FunASR ：<||><||><||><||>
        >>> lang_tag_filter("<|zh|><|SAD|><|Speech|><|withitn|>，。")
        {"language": "zh", "emotion": "SAD", "emoji": "😔", "content": "，。"}
        >>> lang_tag_filter("<|en|><|HAPPY|><|Speech|><|withitn|>Hello hello.")
        {"language": "en", "emotion": "HAPPY", "emoji": "🙂", "content": "Hello hello."}
        >>> lang_tag_filter("plain text")
        "plain text"
    """

    tag_pattern = r"<\|([^|]+)\|>"
    all_tags = re.findall(tag_pattern, text)


    clean_text = re.sub(tag_pattern, "", text).strip()


    if not all_tags:
        return clean_text


    language = all_tags[0] if len(all_tags) > 0 else "zh"
    emotion = all_tags[1] if len(all_tags) > 1 else "NEUTRAL"


    result = {
        "content": clean_text,
        "language": language,
        "emotion": emotion,
        # "event": event,
    }


    if emotion in EMOTION_EMOJI_MAP:
        result["emotion"] = EMOTION_EMOJI_MAP[emotion]

    # if event in EVENT_EMOJI_MAP:
    #     result["event"] = EVENT_EMOJI_MAP[event]

    return result

