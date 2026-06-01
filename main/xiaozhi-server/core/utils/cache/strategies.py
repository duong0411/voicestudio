"""

"""

import time
from enum import Enum
from typing import Any, Optional
from dataclasses import dataclass


class CacheStrategy(Enum):
    """"""

    TTL = "ttl"
    LRU = "lru"
    FIXED_SIZE = "fixed_size"
    TTL_LRU = "ttl_lru"


@dataclass
class CacheEntry:
    """"""

    value: Any
    timestamp: float
    ttl: Optional[float] = None
    access_count: int = 0
    last_access: float = None

    def __post_init__(self):
        if self.last_access is None:
            self.last_access = self.timestamp

    def is_expired(self) -> bool:
        """"""
        if self.ttl is None:
            return False
        return time.time() - self.timestamp > self.ttl

    def touch(self):
        """"""
        self.last_access = time.time()
        self.access_count += 1
