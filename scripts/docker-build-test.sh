#!/usr/bin/env bash
# Build Voice AL Docker images and run smoke tests.
# Requires: docker daemon + user in "docker" group (or run with sudo).
set -euo pipefail

ROOT="$(cd "$(dirname "$0")/.." && pwd)"
cd "$ROOT"

SERVER_BASE_TAG="${SERVER_BASE_TAG:-ghcr.io/xinnan-tech/xiaozhi-esp32-server:server-base}"
SERVER_TAG="${SERVER_TAG:-voiceal-server:local}"
WEB_TAG="${WEB_TAG:-voiceal-web:local}"
RUN_SMOKE="${RUN_SMOKE:-1}"

echo "==> [1/4] Build server base image (may take several minutes)..."
docker build -f Dockerfile-server-base -t "$SERVER_BASE_TAG" .

echo "==> [2/4] Build xiaozhi-server image..."
docker build -f Dockerfile-server -t "$SERVER_TAG" .

echo "==> [3/4] Build manager-web image (Vue + Java + Nginx)..."
docker build -f Dockerfile-web -t "$WEB_TAG" .

if [[ "$RUN_SMOKE" != "1" ]]; then
  echo "Build complete. Set RUN_SMOKE=1 to run container smoke tests."
  exit 0
fi

echo "==> [4/4] Smoke test xiaozhi-server..."
CID=$(docker run -d --rm -p 18000:8000 -p 18003:8003 "$SERVER_TAG")
trap 'docker stop "$CID" >/dev/null 2>&1 || true' EXIT

for i in $(seq 1 30); do
  if curl -sf "http://127.0.0.1:18003/" >/dev/null 2>&1 || curl -sf "http://127.0.0.1:18000/" >/dev/null 2>&1; then
    echo "Server container responded (attempt $i)."
    break
  fi
  if [[ "$i" -eq 30 ]]; then
    echo "ERROR: server did not become ready in time."
    docker logs "$CID" 2>&1 | tail -50
    exit 1
  fi
  sleep 2
done

docker logs "$CID" 2>&1 | tail -15
echo "All Docker build + smoke steps finished."
