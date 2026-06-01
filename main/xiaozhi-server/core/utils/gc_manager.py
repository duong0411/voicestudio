"""
GC
，GCGIL
"""

import gc
import asyncio
import threading
from config.logger import setup_logging

TAG = __name__
logger = setup_logging()


class GlobalGCManager:
    """"""

    def __init__(self, interval_seconds=300):
        """
        GC

        Args:
            interval_seconds: GC（），300（5）
        """
        self.interval_seconds = interval_seconds
        self._task = None
        self._stop_event = asyncio.Event()
        self._lock = threading.Lock()

    async def start(self):
        """GC"""
        if self._task is not None:
            logger.bind(tag=TAG).warning("GC")
            return

        logger.bind(tag=TAG).info(f"GC，{self.interval_seconds}")
        self._stop_event.clear()
        self._task = asyncio.create_task(self._gc_loop())

    async def stop(self):
        """GC"""
        if self._task is None:
            return

        logger.bind(tag=TAG).info("GC")
        self._stop_event.set()

        if self._task and not self._task.done():
            self._task.cancel()
            try:
                await self._task
            except asyncio.CancelledError:
                pass

        self._task = None

    async def _gc_loop(self):
        """GC"""
        try:
            while not self._stop_event.is_set():

                try:
                    await asyncio.wait_for(
                        self._stop_event.wait(), timeout=self.interval_seconds
                    )

                    break
                except asyncio.TimeoutError:

                    pass


                await self._run_gc()

        except asyncio.CancelledError:
            logger.bind(tag=TAG).info("GC")
            raise
        except Exception as e:
            logger.bind(tag=TAG).error(f"GC: {e}")
        finally:
            logger.bind(tag=TAG).info("GC")

    async def _run_gc(self):
        """"""
        try:

            loop = asyncio.get_running_loop()

            def do_gc():
                with self._lock:
                    before = len(gc.get_objects())
                    collected = gc.collect()
                    after = len(gc.get_objects())
                    return before, collected, after

            before, collected, after = await loop.run_in_executor(None, do_gc)
            logger.bind(tag=TAG).debug(
                f"GC - : {collected}, "
                f": {before} -> {after}"
            )
        except Exception as e:
            logger.bind(tag=TAG).error(f"GC: {e}")



_gc_manager_instance = None


def get_gc_manager(interval_seconds=300):
    """
    GC（）

    Args:
        interval_seconds: GC（），300（5）

    Returns:
        GlobalGCManager
    """
    global _gc_manager_instance
    if _gc_manager_instance is None:
        _gc_manager_instance = GlobalGCManager(interval_seconds)
    return _gc_manager_instance
