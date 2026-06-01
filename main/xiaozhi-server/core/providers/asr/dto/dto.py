from enum import Enum
from typing import Union, Optional


class InterfaceType(Enum):

    STREAM = "STREAM"
    NON_STREAM = "NON_STREAM"
    LOCAL = "LOCAL"
