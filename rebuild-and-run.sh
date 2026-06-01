#!/usr/bin/env bash
set -e

echo "==> 1. Fixing data folder and models folder ownership..."
sudo chown -R $(whoami):$(whoami) main/xiaozhi-server/data
if [ -d main/xiaozhi-server/models ]; then
    sudo chown -R $(whoami):$(whoami) main/xiaozhi-server/models
fi

# Check if model.pt is a directory (created by docker mount fallback)
MODEL_PT="main/xiaozhi-server/models/SenseVoiceSmall/model.pt"
if [ -d "$MODEL_PT" ]; then
    echo "Removing empty directory placeholder for model.pt..."
    sudo rm -rf "$MODEL_PT"
fi

# Download model.pt if it doesn't exist
if [ ! -f "$MODEL_PT" ]; then
    echo "Downloading SenseVoiceSmall model.pt..."
    mkdir -p main/xiaozhi-server/models/SenseVoiceSmall
    curl -L "https://modelscope.cn/models/iic/SenseVoiceSmall/resolve/master/model.pt" -o "$MODEL_PT"
fi


echo "==> 2. Rebuilding python server docker image..."
sudo docker build -f Dockerfile-server -t voiceal:latest .

echo "==> 3. Rebuilding web manager docker image (Vue + Java)..."
sudo docker build -f Dockerfile-web -t voiceal-web:latest .

echo "==> 4. Restarting all docker compose containers..."
cd main/xiaozhi-server
sudo docker compose -f docker-compose.yml down
sudo docker compose -f docker-compose.yml up -d --force-recreate

echo "==> 5. Verifying running containers..."
sudo docker ps

echo "==> 6. Printing python server logs..."
sudo docker logs xiaozhi-esp32-server


