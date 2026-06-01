import edge_tts
import asyncio

async def test():
    texts = [
        "Hệ thống đang bận",
        "vui lòng thử lại sau nhé",
        "Hệ thống đang bận vui lòng thử lại sau nhé",
        "Hệ thống đang bận, vui lòng thử lại sau nhé",
        "Hệ thống đang bận, vui lòng thử lại sau nhé.",
    ]
    for t in texts:
        try:
            c = edge_tts.Communicate(t, "vi-VN-HoaiMyNeural")
            data = b""
            async for chunk in c.stream():
                if chunk["type"] == "audio":
                    data += chunk["data"]
            print(f"OK ({len(data):>6} bytes): {repr(t)}")
        except Exception as e:
            print(f"FAIL: {repr(t)} -> {e}")

asyncio.run(test())
