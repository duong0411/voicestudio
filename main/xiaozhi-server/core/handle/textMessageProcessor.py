import json
from typing import TYPE_CHECKING

if TYPE_CHECKING:
    from core.connection import ConnectionHandler
from core.handle.textMessageHandlerRegistry import TextMessageHandlerRegistry

TAG = __name__


class TextMessageProcessor:
    """"""

    def __init__(self, registry: TextMessageHandlerRegistry):
        self.registry = registry

    async def process_message(self, conn: "ConnectionHandler", message: str) -> None:
        """"""
        try:

            msg_json = json.loads(message)


            if isinstance(msg_json, dict):
                message_type = msg_json.get("type")


                conn.logger.bind(tag=TAG).info(f"{message_type}：{message}")


                handler = self.registry.get_handler(message_type)
                if handler:
                    await handler.handle(conn, msg_json)
                else:
                    conn.logger.bind(tag=TAG).error(f"：{message}")

            elif isinstance(msg_json, int):
                conn.logger.bind(tag=TAG).info(f"：{message}")
                await conn.websocket.send(message)

        except json.JSONDecodeError:

            conn.logger.bind(tag=TAG).error(f"：{message}")
            await conn.websocket.send(message)
