""""""

from abc import ABC, abstractmethod
from typing import Dict, Any
from .tool_types import ToolDefinition
from plugins_func.register import ActionResponse


class ToolExecutor(ABC):
    """"""

    @abstractmethod
    async def execute(
        self, conn, tool_name: str, arguments: Dict[str, Any]
    ) -> ActionResponse:
        """"""
        pass

    @abstractmethod
    def get_tools(self) -> Dict[str, ToolDefinition]:
        """"""
        pass

    @abstractmethod
    def has_tool(self, tool_name: str) -> bool:
        """"""
        pass
