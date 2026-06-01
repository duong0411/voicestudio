
import Api from "@/apis/api";
import store from "@/store";

class FeatureManager {
    constructor() {
        this.defaultFeatures = {
            voiceprintRecognition: {
                name: 'feature.voiceprintRecognition.name',
                enabled: false,
                description: 'feature.voiceprintRecognition.description'
            },
            voiceClone: {
                name: 'feature.voiceClone.name',
                enabled: false,
                description: 'feature.voiceClone.description'
            },
            knowledgeBase: {
                name: 'feature.knowledgeBase.name',
                enabled: false,
                description: 'feature.knowledgeBase.description'
            },
            mcpAccessPoint: {
                name: 'feature.mcpAccessPoint.name',
                enabled: false,
                description: 'feature.mcpAccessPoint.description'
            },
            vad: {
                name: 'feature.vad.name',
                enabled: false,
                description: 'feature.vad.description'
            },
            asr: {
                name: 'feature.asr.name',
                enabled: false,
                description: 'feature.asr.description'
            }
        };
        this.currentFeatures = { ...this.defaultFeatures };
        this.initialized = false;
        this.initPromise = null;
    }

    
    async waitForInitialization() {
        if (!this.initPromise) {
            this.initPromise = this.init();
        }
        await this.initPromise;
        return this.initialized;
    }

    
    async init() {
        try {

            const config = await this.getConfigFromPubConfig();
            if (config) {
                this.currentFeatures = { ...config };
                this.initialized = true;
                return;
            }
        } catch (error) {
            console.warn('pub-config:', error);
        }


        this.currentFeatures = { ...this.defaultFeatures };
        this.initialized = true;
    }

    
    updateConfigCache(config) {
        store.commit('setPubConfig', config);
        localStorage.setItem('pubConfig', JSON.stringify(config));
    }

    
    async getConfigFromPubConfig() {
        return new Promise((resolve) => {

            Api.user.getPubConfig((result) => {

                if (result && result.status === 200) {

                    if (result.data) {
                        const configCache = result.data.data || {};

                        if (result.data.code !== undefined) {
                            if (result.data.code === 0 && result.data.data && result.data.data.systemWebMenu) {
                                try {
                                    let config;
                                    if (typeof result.data.data.systemWebMenu === 'string') {

                                        config = JSON.parse(result.data.data.systemWebMenu);
                                    } else {

                                        config = result.data.data.systemWebMenu;
                                    }


                                    if (config && config.features) {

                                        if (!config.features.knowledgeBase) {
                                            console.warn('knowledgeBase，');
                                            config.features = { ...this.defaultFeatures, ...config.features };
                                        }
                                        resolve(config.features);
                                    } else {
                                        console.warn('features，');
                                        resolve(this.defaultFeatures);
                                    }
                                    configCache.systemWebMenu = config;
                                } catch (error) {
                                    console.warn('systemWebMenu:', error);
                                    resolve(null);
                                }
                            } else {
                                console.warn('code0，');
                                resolve(null);
                            }
                        } else {

                            if (result.data && result.data.systemWebMenu) {
                                try {
                                    let config;
                                    if (typeof result.data.systemWebMenu === 'string') {

                                        config = JSON.parse(result.data.systemWebMenu);
                                    } else {

                                        config = result.data.systemWebMenu;
                                    }


                                    if (config && config.features) {

                                        if (!config.features.knowledgeBase) {
                                            console.warn('knowledgeBase，');
                                            config.features = { ...this.defaultFeatures, ...config.features };
                                        }
                                        resolve(config.features);
                                    } else {
                                        console.warn('features，');
                                        resolve(this.defaultFeatures);
                                    }
                                    configCache.systemWebMenu = config;
                                } catch (error) {
                                    console.warn('systemWebMenu:', error);
                                    resolve(null);
                                }
                            } else {
                                console.warn('systemWebMenu，');
                                resolve(null);
                            }
                        }
                        this.updateConfigCache(configCache)
                    } else {
                        console.warn('data，');
                        resolve(null);
                    }
                } else {
                    console.warn('pub-config，');
                    resolve(null);
                }
            });
        });
    }

    
    getCurrentConfig() {

        return this.currentFeatures;
    }

    
    async saveConfig(config) {
        try {

            this.currentFeatures = { ...config };


            this.saveConfigToAPI(config).catch(error => {
                console.warn('API:', error);
            }).finally(() => {
                this.init()
            });


            window.dispatchEvent(new CustomEvent('featureConfigChanged', {
                detail: config
            }));
        } catch (error) {
            console.error(':', error);
        }
    }

    
    async saveConfigToAPI(config) {
        return new Promise((resolve) => {

            Api.admin.updateParam(
                {
                    id: 600,
                    paramCode: 'system-web.menu',
                    paramValue: JSON.stringify({
                        features: config,
                        groups: {
                            featureManagement: ["voiceprintRecognition", "voiceClone", "knowledgeBase", "mcpAccessPoint"],
                            voiceManagement: ["vad", "asr"]
                        }
                    }),
                    valueType: 'json',
                    remark: ''
                },
                (updateResult) => {
                    if (updateResult.code === 0) {
                        resolve();
                    } else {

                        console.warn(':', updateResult.msg);
                        resolve();
                    }
                },
                (error) => {
                    console.warn(':', error);
                    resolve();
                }
            );
        });
    }



    
    getAllFeatures() {
        return this.getCurrentConfig();
    }

    
    getConfig() {
        const features = this.getAllFeatures();
        return {
            voiceprintRecognition: features.voiceprintRecognition?.enabled || false,
            voiceClone: features.voiceClone?.enabled || false,
            knowledgeBase: features.knowledgeBase?.enabled || false,
            mcpAccessPoint: features.mcpAccessPoint?.enabled || false,
            vad: features.vad?.enabled || false,
            asr: features.asr?.enabled || false
        };
    }

    
    getFeatureStatus(featureKey) {
        const features = this.getAllFeatures();
        return features[featureKey]?.enabled || false;
    }

    
    setFeatureStatus(featureKey, enabled) {
        const features = this.getAllFeatures();
        if (features[featureKey]) {
            features[featureKey].enabled = enabled;
            this.saveConfig(features);
            return true;
        }
        return false;
    }

    
    enableFeature(featureKey) {
        return this.setFeatureStatus(featureKey, true);
    }

    
    disableFeature(featureKey) {
        return this.setFeatureStatus(featureKey, false);
    }

    
    toggleFeature(featureKey) {
        const currentStatus = this.getFeatureStatus(featureKey);
        return this.setFeatureStatus(featureKey, !currentStatus);
    }

    
    resetToDefault() {
        this.saveConfig(this.defaultFeatures);
    }

    
    updateFeatures(featureUpdates) {
        const features = this.getAllFeatures();
        Object.keys(featureUpdates).forEach(featureKey => {
            if (features[featureKey]) {
                features[featureKey].enabled = featureUpdates[featureKey];
            }
        });
        this.saveConfig(features);
    }

    
    getEnabledFeatures() {
        const features = this.getAllFeatures();
        return Object.keys(features).filter(key => features[key].enabled);
    }

    
    isFeatureEnabled(featureKey) {
        return this.getFeatureStatus(featureKey);
    }
}


const featureManager = new FeatureManager();

export default featureManager;