import sys
import uuid
import signal
import asyncio
from aioconsole import ainput
from config.settings import load_config
from config.logger import setup_logging
from core.utils.util import get_local_ip, validate_mcp_endpoint
from core.http_server import SimpleHttpServer
from core.websocket_server import WebSocketServer
from core.utils.util import check_ffmpeg_installed
from core.utils.gc_manager import get_gc_manager

TAG = __name__
logger = setup_logging()


async def wait_for_exit() -> None:
    """
     Ctrl‑C / SIGTERM。
    - Unix:  add_signal_handler
    - Windows:  KeyboardInterrupt
    """
    loop = asyncio.get_running_loop()
    stop_event = asyncio.Event()

    if sys.platform != "win32":  # Unix / macOS
        for sig in (signal.SIGINT, signal.SIGTERM):
            loop.add_signal_handler(sig, stop_event.set)
        await stop_event.wait()
    else:


        try:
            await asyncio.Future()
        except KeyboardInterrupt:  # Ctrl‑C
            pass


async def monitor_stdin():
    """，"""
    while True:
        await ainput()


async def main():
    check_ffmpeg_installed()
    config = load_config()




    auth_key = config["server"].get("auth_key", "")
    

    if not auth_key or len(auth_key) == 0 or "" in auth_key:
        auth_key = config.get("manager-api", {}).get("secret", "")

        if not auth_key or len(auth_key) == 0 or "" in auth_key:
            auth_key = str(uuid.uuid4().hex)
    
    config["server"]["auth_key"] = auth_key


    stdin_task = asyncio.create_task(monitor_stdin())


    gc_manager = get_gc_manager(interval_seconds=300)
    await gc_manager.start()


    ws_server = WebSocketServer(config)
    ws_task = asyncio.create_task(ws_server.start())

    ota_server = SimpleHttpServer(config)
    ota_task = asyncio.create_task(ota_server.start())

    read_config_from_api = config.get("read_config_from_api", False)
    port = int(config["server"].get("http_port", 8003))
    if not read_config_from_api:
        logger.bind(tag=TAG).info(
            "OTA\t\thttp://{}:{}/xiaozhi/ota/",
            get_local_ip(),
            port,
        )
    logger.bind(tag=TAG).info(
        "\thttp://{}:{}/mcp/vision/explain",
        get_local_ip(),
        port,
    )
    mcp_endpoint = config.get("mcp_endpoint", None)
    if mcp_endpoint is not None and "" not in mcp_endpoint:

        if validate_mcp_endpoint(mcp_endpoint):
            logger.bind(tag=TAG).info("mcp\t{}", mcp_endpoint)

            mcp_endpoint = mcp_endpoint.replace("/mcp/", "/call/")
            config["mcp_endpoint"] = mcp_endpoint
        else:
            logger.bind(tag=TAG).error("mcp")
            config["mcp_endpoint"] = " websocket"


    websocket_port = 8000
    server_config = config.get("server", {})
    if isinstance(server_config, dict):
        websocket_port = int(server_config.get("port", 8000))

    logger.bind(tag=TAG).info(
        "Websocket\tws://{}:{}/xiaozhi/v1/",
        get_local_ip(),
        websocket_port,
    )

    logger.bind(tag=TAG).info(
        "=======websocket，======="
    )
    logger.bind(tag=TAG).info(
        "websocketdigital-human，"
    )
    logger.bind(tag=TAG).info(
        "=============================================================\n"
    )

    try:
        await wait_for_exit()
    except asyncio.CancelledError:
        print("，...")
    finally:

        await gc_manager.stop()


        stdin_task.cancel()
        ws_task.cancel()
        if ota_task:
            ota_task.cancel()


        await asyncio.wait(
            [stdin_task, ws_task, ota_task] if ota_task else [stdin_task, ws_task],
            timeout=3.0,
            return_when=asyncio.ALL_COMPLETED,
        )
        print("，。")


if __name__ == "__main__":
    try:
        asyncio.run(main())
    except KeyboardInterrupt:
        print("，。")
