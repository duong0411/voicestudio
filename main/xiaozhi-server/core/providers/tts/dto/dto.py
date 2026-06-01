from enum import Enum
from typing import Union, Optional


class SentenceType(Enum):

    FIRST = "FIRST"
    MIDDLE = "MIDDLE"
    LAST = "LAST"


class ContentType(Enum):

    TEXT = "TEXT"
    FILE = "FILE"
    ACTION = "ACTION"


class InterfaceType(Enum):

    DUAL_STREAM = "DUAL_STREAM"
    SINGLE_STREAM = "SINGLE_STREAM"
    NON_STREAM = "NON_STREAM"


class TTSMessageDTO:
    def __init__(
        self,
        sentence_id: str,

        sentence_type: SentenceType,

        content_type: ContentType,

        content_detail: Optional[str] = None,

        content_file: Optional[str] = None,
    ):
        self.sentence_id = sentence_id
        self.sentence_type = sentence_type
        self.content_type = content_type
        self.content_detail = content_detail
        self.content_file = content_file
