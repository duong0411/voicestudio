
import { getConfig, saveConnectionUrls } from '../../config/manager.js?v=0205';
import { uiController } from '../../ui/controller.js?v=0205';
import { log } from '../../utils/logger.js?v=0205';
import { getAudioPlayer } from '../audio/player.js?v=0205';
import { getAudioRecorder } from '../audio/recorder.js?v=0205';
import { executeMcpTool, getMcpTools, setWebSocket as setMcpWebSocket } from '../mcp/tools.js?v=0205';
import { webSocketConnect } from './ota-connector.js?v=0205';


export class WebSocketHandler {
    constructor() {
        this.websocket = null;
        this.onConnectionStateChange = null;
        this.onRecordButtonStateChange = null;
        this.onSessionStateChange = null;
        this.onSessionEmotionChange = null;
        this.onChatMessage = null;
        this.currentSessionId = null;
        this.isRemoteSpeaking = false;
        /** User clicked hang up — do not auto-reconnect */
        this._intentionalClose = false;
        this._reconnectAttempts = 0;
        this._reconnectTimer = null;
        this._maxReconnectAttempts = 5;
        /** Server hello completed — used for auto-reconnect */
        this._sessionWasReady = false;
    }


    async sendHelloMessage() {
        if (!this.websocket || this.websocket.readyState !== WebSocket.OPEN) return false;

        try {
            const config = getConfig();

            const helloMessage = {
                type: 'hello',
                device_id: config.deviceId,
                device_name: config.deviceName,
                device_mac: config.deviceMac,
                token: config.token,
                features: {
                    mcp: true,
                    emoji: config.emojiEnabled
                },
                audio_params: {
                    format: 'opus',
                    sample_rate: 16000,
                    channels: 1,
                    frame_duration: 60
                }
            };

            log('hello', 'info');
            this.websocket.send(JSON.stringify(helloMessage));

            return new Promise(resolve => {
                const timeout = setTimeout(() => {
                    log('hello', 'error');
                    log(': ""', 'info');
                    resolve(false);
                }, 5000);

                const onMessageHandler = (event) => {
                    try {
                        const response = JSON.parse(event.data);
                        if (response.type === 'hello' && response.session_id) {
                            log(`，ID: ${response.session_id}`, 'success');
                            this.currentSessionId = response.session_id;
                            clearTimeout(timeout);
                            this.websocket.removeEventListener('message', onMessageHandler);
                            resolve(true);
                        }
                    } catch (e) {

                    }
                };

                this.websocket.addEventListener('message', onMessageHandler);
            });
        } catch (error) {
            log(`hello: ${error.message}`, 'error');
            return false;
        }
    }

    _sendWakeupMessages(sessionId) {
        if (!this.websocket || this.websocket.readyState !== WebSocket.OPEN) return;

        // listen detect
        this.websocket.send(JSON.stringify({
            session_id: sessionId,
            type: 'listen',
            state: 'detect',
            text: '，'
        }));
        log('listen detect，: ，', 'info');


        this.websocket.send(JSON.stringify({
            session_id: sessionId,
            type: 'listen',
            state: 'start',
            mode: 'auto'
        }));
        log('listen start', 'info');
    }


    sendListenStart(mode = 'auto') {
        if (!this.websocket || this.websocket.readyState !== WebSocket.OPEN) return false;
        const sid = this.currentSessionId;
        if (!sid) {
            log('listen start: no session_id', 'warning');
            return false;
        }
        try {
            this.websocket.send(JSON.stringify({
                session_id: sid,
                type: 'listen',
                state: 'start',
                mode
            }));
            log('listen start (mic session)', 'info');
            return true;
        } catch (e) {
            log(`listen start: ${e.message}`, 'error');
            return false;
        }
    }

    sendListenStop() {
        if (!this.websocket || this.websocket.readyState !== WebSocket.OPEN) return false;
        const sid = this.currentSessionId;
        if (!sid) {
            log('listen stop: no session_id', 'warning');
            return false;
        }
        try {
            this.websocket.send(JSON.stringify({
                session_id: sid,
                type: 'listen',
                state: 'stop'
            }));
            log('listen stop (flush ASR)', 'info');
            return true;
        } catch (e) {
            log(`listen stop: ${e.message}`, 'error');
            return false;
        }
    }

    handleTextMessage(message) {
        if (message.type === 'hello') {
            log(`：${JSON.stringify(message, null, 2)}`, 'success');
            window.cameraAvailable = true;
            log('，', 'success');
            uiController.updateDialButton(true);

            if (message.session_id) {
                this.currentSessionId = message.session_id;
            }

            this._sendWakeupMessages(message.session_id);

            uiController.startAIChatSession();
        } else if (message.type === 'tts') {
            this.handleTTSMessage(message);
        } else if (message.type === 'audio') {
            log(`: ${JSON.stringify(message)}`, 'info');
        } else if (message.type === 'stt') {
            log(`: ${message.text}`, 'info');

            if (message.text && (message.text.includes('') || message.text.includes('bind'))) {
                log('，', 'warning');
                window.cameraAvailable = false;

                if (typeof window.stopCamera === 'function') {
                    window.stopCamera();
                }

                const cameraBtn = document.getElementById('cameraBtn');
                if (cameraBtn) {
                    cameraBtn.classList.remove('camera-active');
                    cameraBtn.querySelector('.btn-text').textContent = '';
                    cameraBtn.disabled = true;
                    cameraBtn.title = '';
                }
            }

            if (this.onChatMessage && message.text) {
                this.onChatMessage(message.text, true);
            }
        } else if (message.type === 'llm') {
            log(`: ${message.text}`, 'info');

            if (this.onChatMessage && message.text) {
                this.onChatMessage(message.text, false);
            }


            if (message.text && /[\u{1F300}-\u{1F9FF}]|[\u{2600}-\u{26FF}]|[\u{2700}-\u{27BF}]/u.test(message.text)) {

                const emojiMatch = message.text.match(/[\u{1F300}-\u{1F9FF}]|[\u{2600}-\u{26FF}]|[\u{2700}-\u{27BF}]/u);
                if (emojiMatch && this.onSessionEmotionChange) {
                    this.onSessionEmotionChange(emojiMatch[0]);
                }


                if (message.emotion) {
                    console.log(`: emotion=${message.emotion}, text=${message.text}`);
                    this.triggerLive2DEmotionAction(message.emotion);
                }
            }



            const textWithoutEmoji = message.text ? message.text.replace(/[\u{1F300}-\u{1F9FF}]|[\u{2600}-\u{26FF}]|[\u{2700}-\u{27BF}]/gu, '').trim() : '';
            if (textWithoutEmoji && this.onChatMessage) {
                this.onChatMessage(message.text, false);
            }
        } else if (message.type === 'mcp') {
            this.handleMCPMessage(message);
        } else {
            log(`: ${message.type}`, 'info');
            if (this.onChatMessage) {
                this.onChatMessage(`: ${message.type}\n${JSON.stringify(message, null, 2)}`, false);
            }
        }
    }


    handleTTSMessage(message) {
        if (message.state === 'start') {
            log('', 'info');
            this.currentSessionId = message.session_id;
            this.isRemoteSpeaking = true;
            if (this.onSessionStateChange) {
                this.onSessionStateChange(true);
            }


            this.startLive2DTalking();
        } else if (message.state === 'sentence_start') {
            log(`: ${message.text}`, 'info');
            this.ttsSentenceCount = (this.ttsSentenceCount || 0) + 1;

            if (message.text && this.onChatMessage) {
                this.onChatMessage(message.text, false);
            }


            const live2dManager = window.chatApp?.live2dManager;
            if (live2dManager && !live2dManager.isTalking) {
                this.startLive2DTalking();
            }
        } else if (message.state === 'sentence_end') {
            log(`: ${message.text}`, 'info');


        } else if (message.state === 'stop') {
            log('，', 'info');


            const audioPlayer = getAudioPlayer();
            audioPlayer.clearAllAudio();

            this.isRemoteSpeaking = false;
            if (this.onRecordButtonStateChange) {
                this.onRecordButtonStateChange(false);
            }
            if (this.onSessionStateChange) {
                this.onSessionStateChange(false);
            }


            setTimeout(() => {
                this.stopLive2DTalking();
                this.ttsSentenceCount = 0;
            }, 1000);
        }
    }


    startLive2DTalking() {
        try {

            const live2dManager = window.chatApp?.live2dManager;
            if (live2dManager && live2dManager.live2dModel) {

                live2dManager.startTalking();
                log('Live2D', 'info');
            }
        } catch (error) {
            log(`Live2D: ${error.message}`, 'error');
        }
    }


    stopLive2DTalking() {
        try {
            const live2dManager = window.chatApp?.live2dManager;
            if (live2dManager) {
                live2dManager.stopTalking();
                log('Live2D', 'info');
            }
        } catch (error) {
            log(`Live2D: ${error.message}`, 'error');
        }
    }


    initializeLive2DAudioAnalyzer() {
        try {
            const live2dManager = window.chatApp?.live2dManager;
            if (live2dManager) {

                if (live2dManager.initializeAudioAnalyzer()) {
                    log('Live2D，', 'success');
                } else {
                    log('Live2D，', 'warning');
                }
            }
        } catch (error) {
            log(`Live2D: ${error.message}`, 'error');
        }
    }


    handleMCPMessage(message) {
        const payload = message.payload || {};
        log(`: ${JSON.stringify(message)}`, 'info');

        if (payload.method === 'tools/list') {
            const tools = getMcpTools();

            const replyMessage = JSON.stringify({
                "session_id": message.session_id || "",
                "type": "mcp",
                "payload": {
                    "jsonrpc": "2.0",
                    "id": payload.id,
                    "result": {
                        "tools": tools
                    }
                }
            });
            log(`: ${replyMessage}`, 'info');
            this.websocket.send(replyMessage);
            log(`MCP: ${tools.length} `, 'info');

        } else if (payload.method === 'tools/call') {
            const toolName = payload.params?.name;
            const toolArgs = payload.params?.arguments;

            log(`: ${toolName} : ${JSON.stringify(toolArgs)}`, 'info');

            executeMcpTool(toolName, toolArgs).then(result => {
                const replyMessage = JSON.stringify({
                    "session_id": message.session_id || "",
                    "type": "mcp",
                    "payload": {
                        "jsonrpc": "2.0",
                        "id": payload.id,
                        "result": {
                            "content": [
                                {
                                    "type": "text",
                                    "text": JSON.stringify(result)
                                }
                            ],
                            "isError": false
                        }
                    }
                });

                log(`: ${replyMessage}`, 'info');
                this.websocket.send(replyMessage);
            }).catch(error => {
                log(`: ${error.message}`, 'error');
                const errorReply = JSON.stringify({
                    "session_id": message.session_id || "",
                    "type": "mcp",
                    "payload": {
                        "jsonrpc": "2.0",
                        "id": payload.id,
                        "error": {
                            "code": -32603,
                            "message": error.message
                        }
                    }
                });
                this.websocket.send(errorReply);
            });
        } else if (payload.method === 'initialize') {
            log(`: ${JSON.stringify(payload.params)}`, 'info');

            const visionUrl = document.getElementById('visionUrl');
            const visionConfig = payload?.params?.capabilities?.vision;
            if (visionConfig && typeof visionConfig === 'object' && visionConfig.url && visionConfig.token) {
                const visionConfigStr = JSON.stringify(visionConfig);
                localStorage.setItem('xz_tester_vision', visionConfigStr);
                if (visionUrl) visionUrl.value = visionConfig.url;
            } else {
                localStorage.removeItem('xz_tester_vision');
                if (visionUrl) visionUrl.value = '';
            }

            const replyMessage = JSON.stringify({
                "session_id": message.session_id || "",
                "type": "mcp",
                "payload": {
                    "jsonrpc": "2.0",
                    "id": payload.id,
                    "result": {
                        "protocolVersion": "2024-11-05",
                        "capabilities": {
                            "tools": {}
                        },
                        "serverInfo": {
                            "name": "xiaozhi-web-test",
                            "version": "2.1.0"
                        }
                    }
                }
            });
            log(``, 'info');
            this.websocket.send(replyMessage);
        } else {
            log(`MCP: ${payload.method}`, 'warning');
        }
    }


    async handleBinaryMessage(data) {
        try {
            let arrayBuffer;
            if (data instanceof ArrayBuffer) {
                arrayBuffer = data;
            } else if (data instanceof Blob) {
                arrayBuffer = await data.arrayBuffer();
                log(`Blob，: ${arrayBuffer.byteLength}`, 'debug');
            } else {
                log(`: ${typeof data}`, 'warning');
                return;
            }

            const opusData = new Uint8Array(arrayBuffer);
            const audioPlayer = getAudioPlayer();
            audioPlayer.enqueueAudioData(opusData);
        } catch (error) {
            log(`: ${error.message}`, 'error');
        }
    }


    async connect() {
        this._intentionalClose = false;
        const config = getConfig();
        log('OTA...', 'info');
        saveConnectionUrls();

        try {
            const otaUrl = document.getElementById('otaUrl').value.trim();
            const ws = await webSocketConnect(otaUrl, config);
            if (ws === undefined) {
                return false;
            }
            this.websocket = ws;


            this.websocket.binaryType = 'arraybuffer';


            setMcpWebSocket(this.websocket);


            const audioRecorder = getAudioRecorder();
            audioRecorder.setWebSocket(this.websocket);
            audioRecorder.onBeforeAudioSend = () => this.sendListenStart('auto');
            audioRecorder.onAfterAudioFlush = () => this.sendListenStop();

            this.setupEventHandlers();

            return true;
        } catch (error) {
            log(`: ${error.message}`, 'error');
            if (this.onConnectionStateChange) {
                this.onConnectionStateChange(false);
            }
            return false;
        }
    }


    _scheduleReconnect() {
        if (this._intentionalClose || this._reconnectAttempts >= this._maxReconnectAttempts) {
            if (this._reconnectAttempts >= this._maxReconnectAttempts) {
                uiController.addChatMessage('⚠️ WebSocket: reconnect limit reached.', false);
            }
            return;
        }
        if (this._reconnectTimer) {
            clearTimeout(this._reconnectTimer);
        }
        const nextAttempt = this._reconnectAttempts + 1;
        const delay = Math.min(30000, 2000 * Math.pow(2, nextAttempt - 1));
        log(`WS reconnect scheduled in ${delay}ms (attempt ${nextAttempt})`, 'info');
        this._reconnectTimer = setTimeout(async () => {
            this._reconnectTimer = null;
            this._reconnectAttempts = nextAttempt;
            try {
                uiController.addChatMessage(`Reconnecting (${nextAttempt}/${this._maxReconnectAttempts})...`, false);
            } catch (e) { /* */ }
            const ok = await this.connect();
            if (ok) {
                this._reconnectAttempts = 0;
            } else if (!this._intentionalClose && this._reconnectAttempts < this._maxReconnectAttempts) {
                this._scheduleReconnect();
            }
        }, delay);
    }


    setupEventHandlers() {
        this.websocket.onopen = async () => {
            const url = document.getElementById('serverUrl').value;
            log(`: ${url}`, 'success');

            if (this.onConnectionStateChange) {
                this.onConnectionStateChange(true);
            }


            this.isRemoteSpeaking = false;
            if (this.onSessionStateChange) {
                this.onSessionStateChange(false);
            }


            this.initializeLive2DAudioAnalyzer();

            const helloOk = await this.sendHelloMessage();
            if (helloOk) {
                this._sessionWasReady = true;
                this._reconnectAttempts = 0;
            } else {
                this._sessionWasReady = false;
            }
        };

        this.websocket.onclose = () => {
            log('', 'info');

            if (this.onConnectionStateChange) {
                this.onConnectionStateChange(false);
            }

            const audioRecorder = getAudioRecorder();
            audioRecorder.stop();


            if (typeof window.stopCamera === 'function') {
                window.stopCamera();
            }


            const cameraContainer = document.getElementById('cameraContainer');
            if (cameraContainer) {
                cameraContainer.classList.remove('active');
            }

            const shouldReconnect = this._sessionWasReady && !this._intentionalClose
                && this._reconnectAttempts < this._maxReconnectAttempts;
            this._sessionWasReady = false;
            if (shouldReconnect) {
                this._scheduleReconnect();
            }
        };

        this.websocket.onerror = (error) => {
            log(`WebSocket: ${error.message || 'Unknown error'}`, 'error');
            uiController.addChatMessage(`⚠️ WebSocket: ${error.message || 'Unknown error'}`, false);
            if (this.onConnectionStateChange) {
                this.onConnectionStateChange(false);
            }
        };

        this.websocket.onmessage = (event) => {
            try {
                if (typeof event.data === 'string') {
                    const message = JSON.parse(event.data);
                    this.handleTextMessage(message);
                } else {
                    this.handleBinaryMessage(event.data);
                }
            } catch (error) {
                log(`WebSocket: ${error.message}`, 'error');


            }
        };
    }


    disconnect() {
        this._intentionalClose = true;
        this._sessionWasReady = false;
        if (this._reconnectTimer) {
            clearTimeout(this._reconnectTimer);
            this._reconnectTimer = null;
        }
        if (!this.websocket) return;

        this.websocket.close();
        const audioRecorder = getAudioRecorder();
        audioRecorder.stop();


        if (typeof window.stopCamera === 'function') {
            window.stopCamera();
        }


        const cameraContainer = document.getElementById('cameraContainer');
        if (cameraContainer) {
            cameraContainer.classList.remove('active');
        }
    }


    sendTextMessage(text) {
        if (text === '' || !this.websocket || this.websocket.readyState !== WebSocket.OPEN) {
            return false;
        }

        try {

            if (this.isRemoteSpeaking && this.currentSessionId) {
                const abortMessage = {
                    session_id: this.currentSessionId,
                    type: 'abort',
                    reason: 'wake_word_detected'
                };
                this.websocket.send(JSON.stringify(abortMessage));
                log('', 'info');
            }

            const listenMessage = {
                session_id: this.currentSessionId || '',
                type: 'listen',
                state: 'detect',
                text: text
            };

            this.websocket.send(JSON.stringify(listenMessage));
            log(`: ${text}`, 'info');

            return true;
        } catch (error) {
            log(`: ${error.message}`, 'error');
            return false;
        }
    }

    
    triggerLive2DEmotionAction(emotion) {
        try {
            const live2dManager = window.chatApp?.live2dManager;
            if (live2dManager && typeof live2dManager.triggerEmotionAction === 'function') {
                live2dManager.triggerEmotionAction(emotion);
                log(`Live2D: ${emotion}`, 'info');
            } else {
                log(`Live2D: Live2D`, 'warning');
            }
        } catch (error) {
            log(`Live2D: ${error.message}`, 'error');
        }
    }


    getWebSocket() {
        return this.websocket;
    }


    isConnected() {
        return this.websocket && this.websocket.readyState === WebSocket.OPEN;
    }
}


let wsHandlerInstance = null;

export function getWebSocketHandler() {
    if (!wsHandlerInstance) {
        wsHandlerInstance = new WebSocketHandler();
    }
    return wsHandlerInstance;
}
