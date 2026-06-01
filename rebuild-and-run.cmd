@echo off
setlocal enabledelayedexpansion

echo ==> 1. Checking data folder and models folder...
:: Note: Windows handles permissions differently, so Unix-specific 'sudo chown' is omitted.

set "MODEL_PT=main\xiaozhi-server\models\SenseVoiceSmall\model.pt"

:: Check if model.pt is a directory (created by docker mount fallback)
:: In Windows CMD, 'exist [path]\' checks specifically if the path is a directory.
if exist "%MODEL_PT%\" (
    echo Removing empty directory placeholder for model.pt...
    rmdir /s /q "%MODEL_PT%"
)

:: Download model.pt if it doesn't exist
if not exist "%MODEL_PT%" (
    echo Downloading SenseVoiceSmall model.pt...
    if not exist "main\xiaozhi-server\models\SenseVoiceSmall" (
        mkdir "main\xiaozhi-server\models\SenseVoiceSmall"
    )
    curl -L "https://modelscope.cn/models/iic/SenseVoiceSmall/resolve/master/model.pt" -o "%MODEL_PT%"
    if errorlevel 1 (
        echo Error downloading model.pt
        exit /b 1
    )
)

:: Ensure the destination directory for the config exists
if not exist "main\xiaozhi-server\data" (
    mkdir "main\xiaozhi-server\data"
)


echo ==> 2. Rebuilding python server docker image...
docker build -f Dockerfile-server -t voiceal:latest .
if errorlevel 1 (
    echo Error building Dockerfile-server
    exit /b 1
)

echo ==> 3. Rebuilding web manager docker image (Vue + Java)...
docker build --no-cache -f Dockerfile-web -t voiceal-web:latest .
if errorlevel 1 (
    echo Error building Dockerfile-web
    exit /b 1
)

echo ==> 4. Restarting all docker compose containers...
pushd main\xiaozhi-server
docker compose -f docker-compose.yml down
docker compose -f docker-compose.yml up -d --force-recreate
if errorlevel 1 (
    popd
    echo Error restarting docker compose containers
    exit /b 1
)
popd

echo ==> 5. Verifying running containers...
docker ps

echo ==> 6. Printing python server logs...
docker logs xiaozhi-esp32-server

endlocal
