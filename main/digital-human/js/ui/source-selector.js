/**
 * Input source: browser mic vs ESP32-only (hide browser mic).
 * Mic capture: push-to-talk vs continuous (always-on after connect).
 */
import { log } from '../utils/logger.js?v=0205';
import { getAudioRecorder } from '../core/audio/recorder.js?v=0205';

const LS_INPUT = 'xz_input_source';
const LS_CAPTURE = 'xz_mic_capture_mode';

export const INPUT_BROWSER_MIC = 'browser_mic';
export const INPUT_ESP32_ONLY = 'esp32_only';

export function getInputSource() {
    try {
        const v = localStorage.getItem(LS_INPUT);
        if (v === INPUT_ESP32_ONLY) return INPUT_ESP32_ONLY;
    } catch (e) { /* */ }
    return INPUT_BROWSER_MIC;
}

export function setInputSource(mode) {
    const m = mode === INPUT_ESP32_ONLY ? INPUT_ESP32_ONLY : INPUT_BROWSER_MIC;
    try {
        localStorage.setItem(LS_INPUT, m);
    } catch (e) { /* */ }
    window.__VOICE_INPUT_SOURCE = m;
    applyInputSourceToWindow();
}

export function getMicCaptureMode() {
    try {
        const v = localStorage.getItem(LS_CAPTURE);
        if (v === 'continuous') return 'continuous';
    } catch (e) { /* */ }
    return 'push-to-talk';
}

export function setMicCaptureMode(mode) {
    const m = mode === 'continuous' ? 'continuous' : 'push-to-talk';
    try {
        localStorage.setItem(LS_CAPTURE, m);
    } catch (e) { /* */ }
    getAudioRecorder().setCaptureMode(m);
}

export function applyInputSourceToWindow() {
    window.__VOICE_INPUT_SOURCE = getInputSource();
    window.__HIDE_BROWSER_MIC = window.__VOICE_INPUT_SOURCE === INPUT_ESP32_ONLY;
}

export function initSourceSelectorFromUrl() {
    const urlParams = new URLSearchParams(window.location.search);
    const src = urlParams.get('inputSource');
    if (src === 'esp32' || src === INPUT_ESP32_ONLY) {
        setInputSource(INPUT_ESP32_ONLY);
    } else if (src === 'browser' || src === INPUT_BROWSER_MIC) {
        setInputSource(INPUT_BROWSER_MIC);
    }
    const hide = urlParams.get('hideBrowserMic');
    if (hide === '1' || hide === 'true') {
        setInputSource(INPUT_ESP32_ONLY);
    }
    const cap = urlParams.get('micCaptureMode');
    if (cap === 'continuous') {
        setMicCaptureMode('continuous');
    } else if (cap === 'push-to-talk') {
        setMicCaptureMode('push-to-talk');
    }
    applyInputSourceToWindow();
    getAudioRecorder().setCaptureMode(getMicCaptureMode());
}

/**
 * Wire settings UI if present (#inputSourceSelect, #micCaptureModeSelect).
 */
export function initSourceSelectorUI() {
    const inputSel = document.getElementById('inputSourceSelect');
    const capSel = document.getElementById('micCaptureModeSelect');
    if (!inputSel || !capSel) {
        log('source-selector: controls not in DOM', 'debug');
        return;
    }
    inputSel.value = getInputSource();
    capSel.value = getMicCaptureMode();

    inputSel.addEventListener('change', () => {
        setInputSource(inputSel.value);
        log(`input source -> ${getInputSource()}`, 'info');
    });
    capSel.addEventListener('change', () => {
        setMicCaptureMode(capSel.value);
        log(`mic capture -> ${getMicCaptureMode()}`, 'info');
    });
}
