import { log } from '../../utils/logger.js?v=0205';


export async function webSocketConnect(otaUrl, config) {

    if (!validateConfig(config)) {
        return;
    }


    const otaResult = await sendOTA(otaUrl, config);
    if (!otaResult) {
        log('OTA', 'error');
        return;
    }


    const { websocket } = otaResult;
    if (!websocket || !websocket.url) {
        log('OTAwebsocket', 'error');
        return;
    }


    let connUrl = new URL(websocket.url);


    if (websocket.token) {
        if (websocket.token.startsWith("Bearer ")) {
            connUrl.searchParams.append('authorization', websocket.token);
        } else {
            connUrl.searchParams.append('authorization', 'Bearer ' + websocket.token);
        }
    }


    connUrl.searchParams.append('device-id', config.deviceId);
    connUrl.searchParams.append('client-id', config.clientId);
    connUrl.searchParams.append('device-name', config.deviceName || '');

    const wsurl = connUrl.toString()

    log(`: ${wsurl}`, 'info');

    if (wsurl) {
        document.getElementById('serverUrl').value = wsurl;
    }

    return new WebSocket(connUrl.toString());
}


function validateConfig(config) {
    if (!config.deviceMac) {
        log('MAC', 'error');
        return false;
    }
    if (!config.clientId) {
        log('ID', 'error');
        return false;
    }
    return true;
}


async function sendOTA(otaUrl, config) {
    try {
        const res = await fetch(otaUrl, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Device-Id': config.deviceId,
                'Client-Id': config.clientId
            },
            body: JSON.stringify({
                version: 0,
                uuid: '',
                application: {
                    name: 'xiaozhi-web-test',
                    version: '1.0.0',
                    compile_time: '2025-04-16 10:00:00',
                    idf_version: '4.4.3',
                    elf_sha256: '1234567890abcdef1234567890abcdef1234567890abcdef'
                },
                ota: { label: 'xiaozhi-web-test' },
                board: {
                    type: config.deviceName,
                    ssid: 'xiaozhi-web-test',
                    rssi: 0,
                    channel: 0,
                    ip: '192.168.1.1',
                    mac: config.deviceMac
                },
                flash_size: 0,
                minimum_free_heap_size: 0,
                mac_address: config.deviceMac,
                chip_model_name: '',
                chip_info: { model: 0, cores: 0, revision: 0, features: 0 },
                partition_table: [{ label: '', type: 0, subtype: 0, address: 0, size: 0 }]
            })
        });

        if (!res.ok) throw new Error(`${res.status} ${res.statusText}`);

        const result = await res.json();
        return result;
    } catch (err) {
        return null;
    }
}