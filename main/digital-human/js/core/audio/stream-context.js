import BlockingQueue from '../../utils/blocking-queue.js?v=0205';
import { log } from '../../utils/logger.js?v=0205';


export class StreamingContext {
    constructor(opusDecoder, audioContext, sampleRate, channels, minAudioDuration) {
        this.opusDecoder = opusDecoder;
        this.audioContext = audioContext;


        this.sampleRate = sampleRate;
        this.channels = channels;
        this.minAudioDuration = minAudioDuration;


        this.queue = [];
        this.activeQueue = new BlockingQueue();
        this.pendingAudioBufferQueue = [];
        this.audioBufferQueue = new BlockingQueue();
        this.playing = false;
        this.endOfStream = false;
        this.source = null;
        this.totalSamples = 0;
        this.lastPlayTime = 0;
        this.scheduledEndTime = 0;


        this.analyser = this.audioContext.createAnalyser();
        this.analyser.fftSize = 256;
    }


    pushAudioBuffer(item) {
        this.audioBufferQueue.enqueue(...item);
    }


    async getPendingAudioBufferQueue() {

        const data = await this.audioBufferQueue.dequeue();

        this.pendingAudioBufferQueue = data;
    }


    async getQueue(minSamples) {
        const num = minSamples - this.queue.length > 0 ? minSamples - this.queue.length : 1;


        const tempArray = await this.activeQueue.dequeue(num);
        this.queue.push(...tempArray);
    }


    convertInt16ToFloat32(int16Data) {
        const float32Data = new Float32Array(int16Data.length);
        for (let i = 0; i < int16Data.length; i++) {

            float32Data[i] = int16Data[i] / 32768.0;
        }
        return float32Data;
    }


    getPendingDecodeCount() {
        return this.audioBufferQueue.length + this.pendingAudioBufferQueue.length;
    }


    getPendingPlayCount() {

        const queuedSamples = this.activeQueue.length + this.queue.length;


        let scheduledSamples = 0;
        if (this.playing && this.scheduledEndTime) {
            const currentTime = this.audioContext.currentTime;
            const remainingTime = Math.max(0, this.scheduledEndTime - currentTime);
            scheduledSamples = Math.floor(remainingTime * this.sampleRate);
        }

        const totalSamples = queuedSamples + scheduledSamples;
        return Math.ceil(totalSamples / 960);
    }


    clearAllBuffers() {
        log('', 'info');


        this.audioBufferQueue.clear();
        this.pendingAudioBufferQueue = [];
        this.activeQueue.clear();
        this.queue = [];


        if (this.source) {
            try {
                this.source.stop();
                this.source.disconnect();
            } catch (e) {

            }
            this.source = null;
        }


        this.playing = false;
        this.scheduledEndTime = this.audioContext.currentTime;
        this.totalSamples = 0;

        log('', 'success');
    }


    getAnalyser() {
        return this.analyser;
    }


    async decodeOpusFrames() {
        if (!this.opusDecoder) {
            log('Opus，', 'error');
            return;
        } else {
            log('Opus', 'info');
        }

        while (true) {
            let decodedSamples = [];
            for (const frame of this.pendingAudioBufferQueue) {
                try {

                    const frameData = this.opusDecoder.decode(frame);
                    if (frameData && frameData.length > 0) {

                        const floatData = this.convertInt16ToFloat32(frameData);

                        for (let i = 0; i < floatData.length; i++) {
                            decodedSamples.push(floatData[i]);
                        }
                    }
                } catch (error) {
                    log("Opus: " + error.message, 'error');
                }
            }

            if (decodedSamples.length > 0) {

                for (let i = 0; i < decodedSamples.length; i++) {
                    this.activeQueue.enqueue(decodedSamples[i]);
                }
                this.totalSamples += decodedSamples.length;
            } else {
                log('', 'warning');
            }
            await this.getPendingAudioBufferQueue();
        }
    }


    async startPlaying() {
        this.scheduledEndTime = this.audioContext.currentTime;

        while (true) {

            const minSamples = this.sampleRate * this.minAudioDuration * 2;
            if (!this.playing && this.queue.length < minSamples) {
                await this.getQueue(minSamples);
            }
            this.playing = true;


            while (this.playing && this.queue.length > 0) {

                const playDuration = 0.12;
                const targetSamples = Math.floor(this.sampleRate * playDuration);
                const actualSamples = Math.min(this.queue.length, targetSamples);

                if (actualSamples === 0) break;

                const currentSamples = this.queue.splice(0, actualSamples);
                const audioBuffer = this.audioContext.createBuffer(this.channels, currentSamples.length, this.sampleRate);
                audioBuffer.copyToChannel(new Float32Array(currentSamples), 0);


                this.source = this.audioContext.createBufferSource();
                this.source.buffer = audioBuffer;


                const currentTime = this.audioContext.currentTime;
                const startTime = Math.max(this.scheduledEndTime, currentTime);


                this.source.connect(this.analyser);
                this.source.connect(this.audioContext.destination);

                log(` ${currentSamples.length} ， ${(currentSamples.length / this.sampleRate).toFixed(2)} `, 'debug');
                this.source.start(startTime);


                const duration = audioBuffer.duration;
                this.scheduledEndTime = startTime + duration;
                this.lastPlayTime = startTime;


                if (this.queue.length < targetSamples) {
                    break;
                }
            }


            await this.getQueue(minSamples);
        }
    }
}


export function createStreamingContext(opusDecoder, audioContext, sampleRate, channels, minAudioDuration) {
    return new StreamingContext(opusDecoder, audioContext, sampleRate, channels, minAudioDuration);
}