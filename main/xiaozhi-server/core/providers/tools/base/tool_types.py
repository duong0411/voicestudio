""""""

from enum import Enum

from dataclasses import dataclass
from typing import Any, Dict, Optional
from plugins_func.register import Action


class ToolType(Enum):
    """"""

    SERVER_PLUGIN = "server_plugin"
    SERVER_MCP = "server_mcp"
    DEVICE_IOT = "device_iot"
    DEVICE_MCP = "device_mcp"
    MCP_ENDPOINT = "mcp_endpoint"


@dataclass
class ToolDefinition:
    """"""

    name: str
    description: Dict[str, Any]
    tool_type: ToolType
    parameters: Optional[Dict[str, Any]] = None
