const translations = {
    en: {
        title: "Live Chat Studio",
        status: {
            disconnected: "Disconnected",
            connecting: "Connecting...",
            connected: "Connected",
            micBlocked: "⚠️ HTTP connection detected on non-localhost. Please use HTTPS or localhost to enable microphone access.",
            micWarning: "⚠️ Please grant microphone permission to chat.",
            disconnectedGoodbye: "Disconnected. See you next time! 😊",
            modelLoaded: "Model loaded: ",
            fillOtaUrl: "Please fill in the OTA server URL.",
            micUnavailable: "⚠️ Microphone not available.",
            modelLoadedSuccess: "Model loaded successfully.",
            modelLoadFailed: "Model switch failed.",
            live2dNotInit: "Live2D Manager not initialized.",
            wakewordEnabledMsg: "Wake word detection enabled. Start talking.",
            wakewordDisabledMsg: "Wake word detection disabled.",
            toolCallReceived: "Tool call received, processing...",
            toolExecFinished: "Tool execution finished.",
            toolExecError: "Tool execution error.",
            connectedWelcome: "Connected, welcome! 😊",
            otaFailed: "OTA request failed.",
            wakeWordDetected: 'Wake word "{wakeWord}" detected, listening...',
            esp32AudioOnly: "ESP32-only mode: audio comes from your hardware device. Browser microphone is disabled."
        },
        buttons: {
            settings: "Settings",
            camera: "Camera",
            dial: "Dial",
            hangUp: "Hang Up",
            record: "Hold to Talk",
            apply: "Apply",
            addTool: "➕ Add New Tool",
            changeBg: "Change Background",
            addParam: "➕ Add Parameter",
            cancel: "Cancel",
            saveTool: "Save Tool",
            saveParam: "Save Parameter",
            close: "Close"
        },
        tabs: {
            device: "Device",
            wakeword: "Wake Word",
            other: "Appearance"
        },
        labels: {
            deviceMac: "Device MAC:",
            deviceName: "Device Name:",
            clientId: "Client ID:",
            emojiEnabled: "Expression (Emoji):",
            emojiTrue: "Enabled",
            emojiFalse: "Disabled",
            wakewordEnabled: "Enable Wake Word:",
            wakewordTrue: "Yes",
            wakewordFalse: "No",
            wakewordWsUrl: "Wake Word WS URL:",
            wakewordList: "Wake Words (one per line):",
            live2dModel: "Live2D Model:",
            background: "Switch Background:",
            inputSource: "Audio input:",
            micCaptureMode: "Mic mode:",
            optBrowserMic: "Browser microphone",
            optEsp32Only: "ESP32 device only (hide browser mic)",
            optPtt: "Push-to-talk",
            optContinuous: "Continuous (hands-free)",
            mcpTools: "MCP Tool List",
            mcpToolName: "Tool Name *",
            mcpToolDesc: "Tool Description *",
            mcpMockRes: "Mock Response (JSON, optional)",
            mcpPropName: "Parameter Name *",
            mcpPropType: "Parameter Type *",
            mcpPropMin: "Minimum Value",
            mcpPropMax: "Maximum Value",
            mcpPropDesc: "Description",
            mcpPropReq: "Required Parameter",
            mcpToolParams: "Parameters:",
            mcpToolMockResponse: "Mock Response:",
            mcpParamCount: "{count} params",
            mcpParamRequired: "({count} required)",
            mcpConfigured: "Configured",
            mcpNotConfigured: "Not Configured",
            mcpParamRequiredBadge: "[Required]",
            mcpParamName: "Parameter Name:",
            mcpParamType: "Type:",
            mcpParamDesc: "Description:",
            mcpNoTools: "No MCP tools configured.",
            mcpToolNameExists: "Tool name already exists.",
            mcpMockResponseInvalidJson: "Mock response is not a valid JSON format:",
            mcpWebSocketConnectedError: "WebSocket is connected. Cannot edit tools.",
            mcpConfirmDelete: 'Are you sure you want to delete tool "{name}"?',
            mcpParamDuplicate: "Parameter name already exists.",
            mcpCameraFeatureUnavailable: "Camera feature is not available.",
            mcpCameraNotStarted: "Camera not started or photo capture not supported."
        },
        placeholders: {
            messageInput: "Type a message...",
            deviceName: "Enter device name...",
            clientId: "Enter client ID...",
            otaUrl: "e.g., http://127.0.0.1:8003/xiaozhi/ota/",
            mcpToolName: "e.g., get_weather",
            mcpToolDesc: "Describe what the tool does...",
            mcpMockRes: "JSON object representing response...",
            mcpPropName: "e.g., location",
            mcpPropDesc: "Describe the parameter..."
        },
        modals: {
            settingsTitle: "Settings",
            mcpTitle: "Add/Edit MCP Tool",
            mcpPropTitle: "Add Parameter"
        }
    },
    vi: {
        title: "Phòng Trò Chuyện Trực Tiếp",
        status: {
            disconnected: "Đã ngắt kết nối",
            connecting: "Đang kết nối...",
            connected: "Đã kết nối",
            micBlocked: "⚠️ Đang kết nối HTTP từ xa. Vui lòng sử dụng HTTPS hoặc localhost để cấp quyền micro.",
            micWarning: "⚠️ Vui lòng cấp quyền micro để bắt đầu nói chuyện.",
            disconnectedGoodbye: "Đã ngắt kết nối. Hẹn gặp lại bạn lần sau! 😊",
            modelLoaded: "Đã tải mô hình: ",
            fillOtaUrl: "Vui lòng nhập địa chỉ máy chủ OTA.",
            micUnavailable: "⚠️ Micro không khả dụng.",
            modelLoadedSuccess: "Tải mô hình thành công.",
            modelLoadFailed: "Đổi mô hình thất bại.",
            live2dNotInit: "Trình quản lý Live2D chưa được khởi tạo.",
            wakewordEnabledMsg: "Đã bật chế độ từ đánh thức. Hãy bắt đầu nói.",
            wakewordDisabledMsg: "Chế độ từ đánh thức đã tắt.",
            toolCallReceived: "Đã nhận lệnh gọi công cụ, đang xử lý...",
            toolExecFinished: "Thực thi công cụ hoàn tất.",
            toolExecError: "Lỗi thực thi công cụ.",
            connectedWelcome: "Đã kết nối, xin chào! 😊",
            otaFailed: "Yêu cầu OTA thất bại.",
            wakeWordDetected: 'Đã phát hiện từ đánh thức "{wakeWord}", đang nghe...',
            esp32AudioOnly: "Chế độ chỉ ESP32: âm thanh từ phần cứng ESP32. Micro trình duyệt đã tắt."
        },
        buttons: {
            settings: "Cài đặt",
            camera: "Máy ảnh",
            dial: "Gọi",
            hangUp: "Gác máy",
            record: "Giữ để Nói",
            apply: "Áp dụng",
            addTool: "➕ Thêm công cụ mới",
            changeBg: "Thay đổi hình nền",
            addParam: "➕ Thêm tham số",
            cancel: "Hủy",
            saveTool: "Lưu công cụ",
            saveParam: "Lưu tham số",
            close: "Đóng"
        },
        tabs: {
            device: "Thiết bị",
            wakeword: "Từ đánh thức",
            other: "Giao diện"
        },
        labels: {
            deviceMac: "Địa chỉ MAC:",
            deviceName: "Tên thiết bị:",
            clientId: "Mã khách hàng:",
            emojiEnabled: "Biểu cảm (Emoji):",
            emojiTrue: "Bật",
            emojiFalse: "Tắt",
            wakewordEnabled: "Bật từ đánh thức:",
            wakewordTrue: "Có",
            wakewordFalse: "Không",
            wakewordWsUrl: "WS URL từ đánh thức:",
            wakewordList: "Danh sách từ đánh thức (mỗi dòng một từ):",
            live2dModel: "Mẫu Live2D:",
            background: "Đổi hình nền:",
            inputSource: "Nguồn âm thanh:",
            micCaptureMode: "Chế độ micro:",
            optBrowserMic: "Micro trình duyệt",
            optEsp32Only: "Chỉ thiết bị ESP32 (ẩn micro trình duyệt)",
            optPtt: "Bấm giữ để nói",
            optContinuous: "Liên tục (rảnh tay)",
            mcpTools: "Danh sách công cụ MCP",
            mcpToolName: "Tên công cụ *",
            mcpToolDesc: "Mô tả công cụ *",
            mcpMockRes: "Kết quả mô phỏng (JSON, tùy chọn)",
            mcpPropName: "Tên tham số *",
            mcpPropType: "Kiểu tham số *",
            mcpPropMin: "Giá trị nhỏ nhất",
            mcpPropMax: "Giá trị lớn nhất",
            mcpPropDesc: "Mô tả",
            mcpPropReq: "Tham số bắt buộc",
            mcpToolParams: "Tham số:",
            mcpToolMockResponse: "Kết quả mô phỏng:",
            mcpParamCount: "{count} tham số",
            mcpParamRequired: "({count} bắt buộc)",
            mcpConfigured: "Đã cấu hình",
            mcpNotConfigured: "Chưa cấu hình",
            mcpParamRequiredBadge: "[Bắt buộc]",
            mcpParamName: "Tên tham số:",
            mcpParamType: "Kiểu:",
            mcpParamDesc: "Mô tả:",
            mcpNoTools: "Chưa cấu hình công cụ MCP.",
            mcpToolNameExists: "Tên công cụ đã tồn tại.",
            mcpMockResponseInvalidJson: "Kết quả mô phỏng không hợp lệ định dạng JSON:",
            mcpWebSocketConnectedError: "WebSocket đã kết nối. Không thể chỉnh sửa công cụ.",
            mcpConfirmDelete: 'Bạn có chắc chắn muốn xóa công cụ "{name}" không?',
            mcpParamDuplicate: "Tên tham số đã tồn tại.",
            mcpCameraFeatureUnavailable: "Tính năng máy ảnh không khả dụng.",
            mcpCameraNotStarted: "Máy ảnh chưa được khởi động hoặc không hỗ trợ chụp ảnh."
        },
        placeholders: {
            messageInput: "Nhập tin nhắn...",
            deviceName: "Nhập tên thiết bị...",
            clientId: "Nhập mã khách hàng...",
            otaUrl: "Ví dụ: http://127.0.0.1:8003/xiaozhi/ota/",
            mcpToolName: "Ví dụ: get_weather",
            mcpToolDesc: "Mô tả chức năng của công cụ...",
            mcpMockRes: "Đối tượng JSON mô phỏng kết quả...",
            mcpPropName: "Ví dụ: location",
            mcpPropDesc: "Mô tả tham số..."
        },
        modals: {
            settingsTitle: "Cài đặt",
            mcpTitle: "Thêm/Sửa công cụ MCP",
            mcpPropTitle: "Thêm tham số"
        }
    }
};

export function getTranslation(lang) {
    const l = (lang === "vi" || lang === "vi-VN") ? "vi" : "en";
    return translations[l];
}

export function localizeDOM(lang) {
    const t = getTranslation(lang);

    // Browser tab title
    document.title = t.title;

    // Translate main screen controls
    const settingsBtnText = document.querySelector("#settingsBtn .btn-text");
    if (settingsBtnText) settingsBtnText.textContent = t.buttons.settings;

    const cameraBtnText = document.querySelector("#cameraBtn .btn-text");
    if (cameraBtnText) cameraBtnText.textContent = t.buttons.camera;

    const dialBtnText = document.querySelector("#dialBtn .btn-text");
    if (dialBtnText) dialBtnText.textContent = t.buttons.dial;

    const recordBtnText = document.querySelector("#recordBtn .btn-text");
    if (recordBtnText) recordBtnText.textContent = t.buttons.record;

    // Settings Modal headers & tabs
    const settingsTitle = document.querySelector("#settingsModal .modal-header h2");
    if (settingsTitle) settingsTitle.textContent = t.modals.settingsTitle;

    const tabDevice = document.querySelector('[data-tab="device"]');
    if (tabDevice) tabDevice.textContent = t.tabs.device;

    const tabWakeword = document.querySelector('[data-tab="wakeword"]');
    if (tabWakeword) tabWakeword.textContent = t.tabs.wakeword;

    const tabOther = document.querySelector('[data-tab="other"]');
    if (tabOther) tabOther.textContent = t.tabs.other;

    // Device Tab Labels
    const labelMac = document.querySelector('label[for="deviceMac"]');
    if (labelMac) labelMac.textContent = t.labels.deviceMac;

    const labelName = document.querySelector('label[for="deviceName"]');
    if (labelName) labelName.textContent = t.labels.deviceName;

    const labelClientId = document.querySelector('label[for="clientId"]');
    if (labelClientId) labelClientId.textContent = t.labels.clientId;

    const labelEmoji = document.querySelector('label[for="emojiEnabled"]');
    if (labelEmoji) labelEmoji.textContent = t.labels.emojiEnabled;

    const emojiTrueOption = document.querySelector('#emojiEnabled option[value="true"]');
    if (emojiTrueOption) emojiTrueOption.textContent = t.labels.emojiTrue;

    const emojiFalseOption = document.querySelector('#emojiEnabled option[value="false"]');
    if (emojiFalseOption) emojiFalseOption.textContent = t.labels.emojiFalse;

    const labelInputSource = document.querySelector('label[for="inputSourceSelect"]');
    if (labelInputSource) labelInputSource.textContent = t.labels.inputSource;

    const labelMicMode = document.querySelector('label[for="micCaptureModeSelect"]');
    if (labelMicMode) labelMicMode.textContent = t.labels.micCaptureMode;

    const optBrowserMic = document.querySelector('#inputSourceSelect option[value="browser_mic"]');
    if (optBrowserMic) optBrowserMic.textContent = t.labels.optBrowserMic;

    const optEsp32Only = document.querySelector('#inputSourceSelect option[value="esp32_only"]');
    if (optEsp32Only) optEsp32Only.textContent = t.labels.optEsp32Only;

    const optPtt = document.querySelector('#micCaptureModeSelect option[value="push-to-talk"]');
    if (optPtt) optPtt.textContent = t.labels.optPtt;

    const optContinuous = document.querySelector('#micCaptureModeSelect option[value="continuous"]');
    if (optContinuous) optContinuous.textContent = t.labels.optContinuous;

    // Wake Word Tab Labels & buttons
    const labelWwEnabled = document.querySelector('label[for="wakewordEnabled"]');
    if (labelWwEnabled) labelWwEnabled.textContent = t.labels.wakewordEnabled;

    const wwTrueOption = document.querySelector('#wakewordEnabled option[value="true"]');
    if (wwTrueOption) wwTrueOption.textContent = t.labels.wakewordTrue;

    const wwFalseOption = document.querySelector('#wakewordEnabled option[value="false"]');
    if (wwFalseOption) wwFalseOption.textContent = t.labels.wakewordFalse;

    const labelWwWsUrl = document.querySelector('label[for="wakewordWsUrl"]');
    if (labelWwWsUrl) labelWwWsUrl.textContent = t.labels.wakewordWsUrl;

    const labelWwList = document.querySelector('label[for="wakewordList"]');
    if (labelWwList) labelWwList.textContent = t.labels.wakewordList;

    const applyWakewordBtn = document.getElementById("applyWakewordBtn");
    if (applyWakewordBtn) applyWakewordBtn.textContent = t.buttons.apply;

    // MCP tab labels & buttons
    const mcpHeader = document.querySelector("#mcpTab h3");
    if (mcpHeader) mcpHeader.textContent = t.labels.mcpTools;

    const addMcpToolBtn = document.getElementById("addMcpToolBtn");
    if (addMcpToolBtn) {
        // Keep the emoji but translate text
        addMcpToolBtn.childNodes.forEach(node => {
            if (node.nodeType === Node.TEXT_NODE) {
                node.textContent = node.textContent.includes("➕") ? " ➕ " + t.buttons.addTool.replace("➕", "").trim() : t.buttons.addTool.replace("➕", "").trim();
            }
        });
    }

    // Appearance tab
    const labelModel = document.querySelector('label[for="live2dModelSelect"]');
    if (labelModel) labelModel.textContent = t.labels.live2dModel;

    const labelBg = document.querySelector('#otherTab label:nth-of-type(2)');
    if (labelBg) labelBg.textContent = t.labels.background;

    const changeBgText = document.querySelector("#backgroundBtn .btn-text");
    if (changeBgText) changeBgText.textContent = t.buttons.changeBg;

    // Placeholders
    const messageInput = document.getElementById("messageInput");
    if (messageInput) messageInput.placeholder = t.placeholders.messageInput;

    const inputDeviceName = document.getElementById("deviceName");
    if (inputDeviceName) inputDeviceName.placeholder = t.placeholders.deviceName;

    const inputClientId = document.getElementById("clientId");
    if (inputClientId) inputClientId.placeholder = t.placeholders.clientId;

    const inputOtaUrl = document.getElementById("otaUrl");
    if (inputOtaUrl) inputOtaUrl.placeholder = t.placeholders.otaUrl;

    // MCP Modals
    const mcpModalTitle = document.getElementById("mcpModalTitle");
    if (mcpModalTitle) mcpModalTitle.textContent = t.modals.mcpTitle;

    const labelMcpName = document.querySelector('label[for="mcpToolName"]');
    if (labelMcpName) labelMcpName.textContent = t.labels.mcpToolName;

    const labelMcpDesc = document.querySelector('label[for="mcpToolDescription"]');
    if (labelMcpDesc) labelMcpDesc.textContent = t.labels.mcpToolDesc;

    const labelMcpMock = document.querySelector('label[for="mcpMockResponse"]');
    if (labelMcpMock) labelMcpMock.textContent = t.labels.mcpMockRes;

    const cancelMcpBtn = document.getElementById("cancelMcpBtn");
    if (cancelMcpBtn) cancelMcpBtn.textContent = t.buttons.cancel;

    const submitMcpBtn = document.querySelector('#mcpModal form button[type="submit"]');
    if (submitMcpBtn) submitMcpBtn.textContent = t.buttons.saveTool;

    // MCP Property Modal
    const mcpPropertyModalTitle = document.getElementById("mcpPropertyModalTitle");
    if (mcpPropertyModalTitle) mcpPropertyModalTitle.textContent = t.modals.mcpPropTitle;

    const labelPropName = document.querySelector('label[for="mcpPropertyName"]');
    if (labelPropName) labelPropName.textContent = t.labels.mcpPropName;

    const labelPropType = document.querySelector('label[for="mcpPropertyType"]');
    if (labelPropType) labelPropType.textContent = t.labels.mcpPropType;

    const labelPropMin = document.querySelector('label[for="mcpPropertyMinimum"]');
    if (labelPropMin) labelPropMin.textContent = t.labels.mcpPropMin;

    const labelPropMax = document.querySelector('label[for="mcpPropertyMaximum"]');
    if (labelPropMax) labelPropMax.textContent = t.labels.mcpPropMax;

    const labelPropDesc = document.querySelector('label[for="mcpPropertyDescription"]');
    if (labelPropDesc) labelPropDesc.textContent = t.labels.mcpPropDesc;

    const labelPropReq = document.querySelector('label[for="mcpPropertyRequired"]');
    if (labelPropReq) {
        // Keep checkbox input but update label text node
        labelPropReq.childNodes.forEach(node => {
            if (node.nodeType === Node.TEXT_NODE) {
                node.textContent = " " + t.labels.mcpPropReq;
            }
        });
    }

    const cancelMcpPropBtn = document.getElementById("cancelMcpPropertyBtn");
    if (cancelMcpPropBtn) cancelMcpPropBtn.textContent = t.buttons.cancel;

    const submitMcpPropBtn = document.querySelector('#mcpPropertyModal form button[type="submit"]');
    if (submitMcpPropBtn) submitMcpPropBtn.textContent = t.buttons.saveParam;
}
