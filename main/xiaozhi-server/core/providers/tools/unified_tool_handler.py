""""""

import json
from typing import Dict, List, Any, Optional
from config.logger import setup_logging
from plugins_func.loadplugins import auto_import_modules

from .base import ToolType
from plugins_func.register import Action, ActionResponse
from .unified_tool_manager import ToolManager
from .server_plugins import ServerPluginExecutor
from .server_mcp import ServerMCPExecutor
from .device_iot import DeviceIoTExecutor
from .device_mcp import DeviceMCPExecutor
from .mcp_endpoint import MCPEndpointExecutor
from core.handle.sendAudioHandle import send_display_message


class UnifiedToolHandler:
    """"""

    def __init__(self, conn):
        self.conn = conn
        self.config = conn.config
        self.logger = setup_logging()


        self.tool_manager = ToolManager(conn)


        self.server_plugin_executor = ServerPluginExecutor(conn)
        self.server_mcp_executor = ServerMCPExecutor(conn)
        self.device_iot_executor = DeviceIoTExecutor(conn)
        self.device_mcp_executor = DeviceMCPExecutor(conn)
        self.mcp_endpoint_executor = MCPEndpointExecutor(conn)


        self.tool_manager.register_executor(
            ToolType.SERVER_PLUGIN, self.server_plugin_executor
        )
        self.tool_manager.register_executor(
            ToolType.SERVER_MCP, self.server_mcp_executor
        )
        self.tool_manager.register_executor(
            ToolType.DEVICE_IOT, self.device_iot_executor
        )
        self.tool_manager.register_executor(
            ToolType.DEVICE_MCP, self.device_mcp_executor
        )
        self.tool_manager.register_executor(
            ToolType.MCP_ENDPOINT, self.mcp_endpoint_executor
        )


        self.finish_init = False

    async def _initialize(self):
        """"""
        try:

            auto_import_modules("plugins_func.functions")


            await self.server_mcp_executor.initialize()


            await self._initialize_mcp_endpoint()


            self._initialize_home_assistant()

            self.finish_init = True
            self.logger.debug("")


            self.current_support_functions()

        except Exception as e:
            self.logger.error(f": {e}")

    async def _initialize_mcp_endpoint(self):
        """MCP"""
        try:
            from .mcp_endpoint import connect_mcp_endpoint


            mcp_endpoint_url = self.config.get("mcp_endpoint", "")

            if (
                mcp_endpoint_url
                and "" not in mcp_endpoint_url
                and mcp_endpoint_url != "null"
            ):
                self.logger.info(f"MCP: {mcp_endpoint_url}")
                mcp_endpoint_client = await connect_mcp_endpoint(
                    mcp_endpoint_url, self.conn
                )

                if mcp_endpoint_client:

                    self.conn.mcp_endpoint_client = mcp_endpoint_client
                    self.logger.info("MCP")
                else:
                    self.logger.warning("MCP")

        except Exception as e:
            self.logger.error(f"MCP: {e}")

    def _initialize_home_assistant(self):
        """Home Assistant"""
        try:
            from plugins_func.functions.hass_init import append_devices_to_prompt

            append_devices_to_prompt(self.conn)
        except ImportError:
            pass
        except Exception as e:
            self.logger.error(f"Home Assistant: {e}")

    def get_functions(self) -> List[Dict[str, Any]]:
        """"""
        return self.tool_manager.get_function_descriptions()

    def current_support_functions(self) -> List[str]:
        """"""
        func_names = self.tool_manager.get_supported_tool_names()
        self.logger.info(f": {func_names}")
        return func_names

    def upload_functions_desc(self):
        """"""
        self.tool_manager.refresh_tools()
        self.logger.info("")

    def has_tool(self, tool_name: str) -> bool:
        """"""
        return self.tool_manager.has_tool(tool_name)

    async def handle_llm_function_call(
        self, conn, function_call_data: Dict[str, Any]
    ) -> Optional[ActionResponse]:
        """LLM"""
        try:

            if "function_calls" in function_call_data:
                responses = []
                for call in function_call_data["function_calls"]:
                    result = await self.tool_manager.execute_tool(
                        call["name"], call.get("arguments", {})
                    )
                    responses.append(result)
                return self._combine_responses(responses)


            function_name = function_call_data["name"]
            arguments = function_call_data.get("arguments", {})


            if isinstance(arguments, str):
                try:
                    arguments = json.loads(arguments) if arguments else {}
                except json.JSONDecodeError:
                    self.logger.error(f": {arguments}")
                    return ActionResponse(
                        action=Action.ERROR,
                        response="",
                    )

            self.logger.debug(f": {function_name}, : {arguments}")


            try:
                await send_display_message(self.conn, f"% {function_name}")
            except Exception as e:
                self.logger.warning(f": {e}")


            result = await self.tool_manager.execute_tool(function_name, arguments)
            return result

        except Exception as e:
            self.logger.error(f"function call: {e}")
            return ActionResponse(action=Action.ERROR, response=str(e))

    def _combine_responses(self, responses: List[ActionResponse]) -> ActionResponse:
        """"""
        if not responses:
            return ActionResponse(action=Action.NONE, response="")


        for response in responses:
            if response.action == Action.ERROR:
                return response


        contents = []
        responses_text = []

        for response in responses:
            if response.content:
                contents.append(response.content)
            if response.response:
                responses_text.append(response.response)


        final_action = Action.RESPONSE
        for response in responses:
            if response.action == Action.REQLLM:
                final_action = Action.REQLLM
                break

        return ActionResponse(
            action=final_action,
            result="; ".join(contents) if contents else None,
            response="; ".join(responses_text) if responses_text else None,
        )

    async def register_iot_tools(self, descriptors: List[Dict[str, Any]]):
        """IoT"""
        self.device_iot_executor.register_iot_tools(descriptors)
        self.tool_manager.refresh_tools()
        self.logger.info(f"{len(descriptors)}IoT")

    def get_tool_statistics(self) -> Dict[str, int]:
        """"""
        return self.tool_manager.get_tool_statistics()

    async def cleanup(self):
        """"""
        try:
            await self.server_mcp_executor.cleanup()


            if (
                hasattr(self.conn, "mcp_endpoint_client")
                and self.conn.mcp_endpoint_client
            ):
                await self.conn.mcp_endpoint_client.close()

            self.logger.info("")
        except Exception as e:
            self.logger.error(f": {e}")
