import json
import time
from typing import Dict, Any

from core.handle.textMessageHandler import TextMessageHandler
from core.handle.textMessageType import TextMessageType

TAG = __name__


class PingMessageHandler(TextMessageHandler):
    """Ping，WebSocket"""

    @property
    def message_type(self) -> TextMessageType:
        return TextMessageType.PING

    async def handle(self, conn, msg_json: Dict[str, Any]) -> None:
        """
        PING，PONG
        ：{"type": "ping"}
        Args:
            conn: WebSocket
            msg_json: PINGJSON
        """

        enable_websocket_ping = conn.config.get("enable_websocket_ping", False)
        if not enable_websocket_ping:
            conn.logger.debug(f"WebSocket，PING")
            return

        try:
            conn.logger.debug(f"PING，PONG")
            conn.last_activity_time = time.time() * 1000

            pong_message = {
                "type": "pong",
                "timestamp": time.strftime("%Y-%m-%d %H:%M:%S", time.localtime()),
            }


            await conn.websocket.send(json.dumps(pong_message))

        except Exception as e:
            conn.logger.error(f"PING: {e}")
