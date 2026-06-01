import { log } from '../../utils/logger.js?v=0205';



export function checkOpusLoaded() {
    try {

        if (typeof Module === 'undefined') {
            throw new Error('Opus，Module');
        }


        if (typeof Module.instance !== 'undefined' && typeof Module.instance._opus_decoder_get_size === 'function') {

            window.ModuleInstance = Module.instance;
            log('Opus（Module.instance）', 'success');


            const statusElement = document.getElementById('scriptStatus');
            if (statusElement) statusElement.style.display = 'none';
            return;
        }


        if (typeof Module._opus_decoder_get_size === 'function') {
            window.ModuleInstance = Module;
            log('Opus（Module）', 'success');


            const statusElement = document.getElementById('scriptStatus');
            if (statusElement) statusElement.style.display = 'none';
            return;
        }

        throw new Error('Opus，Module');
    } catch (err) {
        log(`Opus，libopus.js: ${err.message}`, 'error');
    }
}



let opusEncoder = null;
export function initOpusEncoder() {
    try {
        if (opusEncoder) {
            return opusEncoder;
        }

        if (!window.ModuleInstance) {
            log('Opus：ModuleInstance', 'error');
            return;
        }


        const mod = window.ModuleInstance;
        const sampleRate = 16000;
        const channels = 1;
        const application = 2048; // OPUS_APPLICATION_VOIP = 2048


        opusEncoder = {
            channels: channels,
            sampleRate: sampleRate,
            frameSize: 960, // 60ms @ 16kHz = 60 * 16 = 960 samples
            maxPacketSize: 4000,
            module: mod,


            init: function () {
                try {

                    const encoderSize = mod._opus_encoder_get_size(this.channels);
                    log(`Opus: ${encoderSize}`, 'info');


                    this.encoderPtr = mod._malloc(encoderSize);
                    if (!this.encoderPtr) {
                        throw new Error("");
                    }


                    const err = mod._opus_encoder_init(
                        this.encoderPtr,
                        this.sampleRate,
                        this.channels,
                        application
                    );

                    if (err < 0) {
                        throw new Error(`Opus: ${err}`);
                    }


                    mod._opus_encoder_ctl(this.encoderPtr, 4002, 16000); // OPUS_SET_BITRATE


                    mod._opus_encoder_ctl(this.encoderPtr, 4010, 5);     // OPUS_SET_COMPLEXITY


                    mod._opus_encoder_ctl(this.encoderPtr, 4016, 1);     // OPUS_SET_DTX

                    log("Opus", 'success');
                    return true;
                } catch (error) {
                    if (this.encoderPtr) {
                        mod._free(this.encoderPtr);
                        this.encoderPtr = null;
                    }
                    log(`Opus: ${error.message}`, 'error');
                    return false;
                }
            },


            encode: function (pcmData) {
                if (!this.encoderPtr) {
                    if (!this.init()) {
                        return null;
                    }
                }

                try {
                    const mod = this.module;


                    const pcmPtr = mod._malloc(pcmData.length * 2);


                    for (let i = 0; i < pcmData.length; i++) {
                        mod.HEAP16[(pcmPtr >> 1) + i] = pcmData[i];
                    }


                    const outPtr = mod._malloc(this.maxPacketSize);


                    const encodedLen = mod._opus_encode(
                        this.encoderPtr,
                        pcmPtr,
                        this.frameSize,
                        outPtr,
                        this.maxPacketSize
                    );

                    if (encodedLen < 0) {
                        throw new Error(`Opus: ${encodedLen}`);
                    }


                    const opusData = new Uint8Array(encodedLen);
                    for (let i = 0; i < encodedLen; i++) {
                        opusData[i] = mod.HEAPU8[outPtr + i];
                    }


                    mod._free(pcmPtr);
                    mod._free(outPtr);

                    return opusData;
                } catch (error) {
                    log(`Opus: ${error.message}`, 'error');
                    return null;
                }
            },


            destroy: function () {
                if (this.encoderPtr) {
                    this.module._free(this.encoderPtr);
                    this.encoderPtr = null;
                }
            }
        };

        opusEncoder.init();
        return opusEncoder;
    } catch (error) {
        log(`Opus: ${error.message}`, 'error');
        return false;
    }
}