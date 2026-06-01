<template>
  <div class="live-page val-app-shell">
    <HeaderBar />
    <el-main class="live-main">
      <!-- Cấu hình tác nhân và thiết bị -->
      <div class="control-panel">
        <div class="control-left">
          <div class="label-wrapper">
            <i class="el-icon-cpu agent-icon" />
            <span class="control-label">{{ $t('live.agentLabel') }}</span>
          </div>
          <el-select
            v-model="selectedAgentId"
            :placeholder="$t('live.selectPlaceholder')"
            class="custom-select"
            @change="handleAgentChange"
            :loading="loadingAgents"
          >
            <el-option
              v-for="item in agentsList"
              :key="item.id"
              :label="item.name"
              :value="item.id"
            />
          </el-select>
        </div>

        <div class="control-left character-control">
          <div class="label-wrapper">
            <i class="el-icon-user agent-icon" />
            <span class="control-label">{{ $t('live.characterLabel') }}</span>
          </div>
          <el-select
            v-model="selectedModel"
            :placeholder="$t('live.selectCharacterPlaceholder')"
            class="custom-select character-select"
            @change="handleModelChange"
          >
            <el-option :label="$t('live.model.hiyori')" value="hiyori_pro_zh" />
            <el-option :label="$t('live.model.natori')" value="natori_pro_zh" />
          </el-select>
        </div>


        <div class="control-right" v-if="webClientDevice">
          <div class="device-badge" :title="'MAC: ' + webClientDevice.macAddress">
            <span class="badge-dot" />
            <span class="badge-text desktop-badge">{{ $t('live.webClientMac', { mac: webClientDevice.macAddress }) }}</span>
            <span class="badge-text mobile-badge">{{ $t('live.webClient') }}</span>
          </div>
        </div>
        <div class="control-right" v-else-if="loadingDevice">
          <div class="device-badge loading">
            <i class="el-icon-loading" />
            <span class="badge-text">{{ $t('live.loadingDevice') }}</span>
          </div>
        </div>
        <div class="control-right" v-else>
          <el-button
            type="primary"
            size="small"
            icon="el-icon-plus"
            class="glow-btn"
            @click="autoCreateWebDevice"
            :loading="creatingDevice"
          >
            {{ $t('live.createWebClient') }}
          </el-button>
        </div>
      </div>

      <!-- Iframe hiển thị Digital Human Client -->
      <div class="iframe-container" v-loading="loadingDevice">
        <iframe
          v-if="iframeUrl"
          :src="iframeUrl"
          class="client-iframe"
          allow="microphone; camera; midi; encrypted-media;"
        />
        <div v-else class="empty-state">
          <div class="empty-content">
            <i class="el-icon-microphone empty-icon" />
            <h3>{{ $t('live.title') }}</h3>
            <p>{{ $t('live.description') }}</p>
          </div>
        </div>
      </div>
    </el-main>
  </div>
</template>

<script>
import Api from '@/apis/api';
import HeaderBar from '@/components/HeaderBar.vue';

export default {
  name: 'LivePage',
  components: {
    HeaderBar
  },
  data() {
    return {
      agentsList: [],
      selectedAgentId: '',
      selectedModel: localStorage.getItem('live2dModel') || 'hiyori_pro_zh',
      webClientDevice: null,
      hardwareDevices: [],
      selectedHardwareId: '',
      voiceInputMode: 'browser',
      iframeUrl: '',
      loadingAgents: false,
      loadingDevice: false,
      creatingDevice: false
    };
  },
  watch: {
    '$i18n.locale'() {
      if (this.webClientDevice) {
        this.buildIframeUrl();
      }
    }
  },
  mounted() {
    this.fetchAgents();
  },
  methods: {
    hardwareDeviceKey(d) {
      if (!d) return '';
      return d.id != null ? String(d.id) : d.macAddress;
    },
    fetchAgents() {
      this.loadingAgents = true;
      Api.agent.getAgentList(
        ({ data }) => {
          this.loadingAgents = false;
          if (data && data.code === 0) {
            this.agentsList = data.data || [];
            if (this.agentsList.length > 0) {
              // Chọn tác nhân đầu tiên mặc định
              this.selectedAgentId = this.agentsList[0].id;
              this.handleAgentChange();
            }
          }
        },
        () => {
          this.loadingAgents = false;
          this.$message.error(this.$t('live.loadAgentsFailed'));
        }
      );
    },
    handleAgentChange() {
      if (!this.selectedAgentId) return;
      this.loadingDevice = true;
      this.webClientDevice = null;
      this.hardwareDevices = [];
      this.selectedHardwareId = '';
      this.iframeUrl = '';

      Api.device.getAgentBindDevices(
        this.selectedAgentId,
        ({ data }) => {
          if (data && data.code === 0) {
            const devices = data.data || [];
            this.hardwareDevices = devices.filter(
              (d) => d.board && d.board !== 'web-client'
            );
            if (this.hardwareDevices.length && !this.selectedHardwareId) {
              const first = this.hardwareDevices[0];
              this.selectedHardwareId = this.hardwareDeviceKey(first);
            }
            // Tìm thiết bị ảo loại 'web-client'
            const webDevice = devices.find((d) => d.board === 'web-client');
            if (webDevice) {
              this.webClientDevice = webDevice;
              this.buildIframeUrl();
              this.loadingDevice = false;
            } else {
              // Tự động tạo thiết bị web-client nếu chưa có
              this.autoCreateWebDevice();
            }
          } else {
            this.loadingDevice = false;
            this.$message.error(data.msg || this.$t('live.loadDevicesFailed'));
          }
        },
        () => {
          this.loadingDevice = false;
          this.$message.error(this.$t('live.connFailed'));
        }
      );
    },
    autoCreateWebDevice() {
      if (!this.selectedAgentId) return;
      this.creatingDevice = true;
      this.loadingDevice = true;
      
      const mac = this.generateRandomMac();
      const params = {
        agentId: this.selectedAgentId,
        board: 'web-client',
        appVersion: '1.0.0',
        macAddress: mac
      };

      Api.device.manualAddDevice(
        params,
        ({ data }) => {
          this.creatingDevice = false;
          if (data && data.code === 0) {
            this.$message.success(this.$t('live.createSuccess'));
            this.handleAgentChange();
          } else {
            this.loadingDevice = false;
            this.$message.error(data.msg || this.$t('live.createFailed'));
          }
        },
        () => {
          this.creatingDevice = false;
          this.loadingDevice = false;
          this.$message.error(this.$t('live.connError'));
        }
      );
    },
    handleModelChange() {
      localStorage.setItem('live2dModel', this.selectedModel);
      this.buildIframeUrl();
    },
    buildIframeUrl() {
      const web = this.webClientDevice;
      if (!web) {
        this.iframeUrl = '';
        return;
      }
      const protocol = window.location.protocol;
      const otaUrl = `${protocol}//${window.location.host}/xiaozhi/ota/`;
      const currentLang = (this.$i18n && this.$i18n.locale) || 'vi';

      const mac = web.macAddress;
      const board = web.board || 'web-client';

      this.iframeUrl = `/digital-human/index.html?mac=${encodeURIComponent(mac)}&board=${encodeURIComponent(
        board
      )}&otaUrl=${encodeURIComponent(otaUrl)}&clientId=${encodeURIComponent(mac)}&lang=${currentLang}&connect=true&inputSource=browser_mic&model=${encodeURIComponent(this.selectedModel)}`;
    },
    generateRandomMac() {
      const hexDigits = '0123456789ABCDEF';
      let mac = '';
      for (let i = 0; i < 6; i++) {
        mac += hexDigits.charAt(Math.floor(Math.random() * 16));
        mac += hexDigits.charAt(Math.floor(Math.random() * 16));
        if (i < 5) mac += ':';
      }
      return mac;
    }
  }
};
</script>

<style scoped lang="scss">
.live-page {
  min-width: auto;
  height: 100vh;
  height: 100dvh;
  display: flex;
  flex-direction: column;
  background: radial-gradient(circle at top right, rgba(124, 92, 255, 0.08), transparent), 
              radial-gradient(circle at bottom left, rgba(0, 229, 200, 0.05), transparent), 
              #0a0f1d;
  color: #f1f5f9;
  overflow: hidden;
}

.live-main {
  flex: 1;
  padding: 16px 24px;
  display: flex;
  flex-direction: column;
  gap: 16px;
  height: calc(100vh - 64px);
  height: calc(100dvh - 64px);
  box-sizing: border-box;
}

.voice-input-panel {
  flex-direction: column;
  align-items: flex-start;
  gap: 10px;

  .voice-input-left {
    flex-wrap: wrap;
    gap: 12px;
  }

  .hardware-select {
    min-width: 260px;
  }
}

.esp32-hint {
  margin: 0;
  font-size: 12px;
  color: #94a3b8;
  line-height: 1.4;
}

.control-panel {
  display: flex;
  align-items: center;
  justify-content: space-between;
  background: rgba(18, 24, 41, 0.6);
  border: 1px solid rgba(255, 255, 255, 0.08);
  border-radius: 12px;
  padding: 12px 20px;
  backdrop-filter: blur(12px);
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.15);
}

.control-left {
  display: flex;
  align-items: center;
  gap: 12px;
}

.label-wrapper {
  display: flex;
  align-items: center;
  gap: 6px;
}

.agent-icon {
  font-size: 18px;
  color: #7c5cff;
}

.control-label {
  font-size: 14px;
  font-weight: 500;
  color: #cbd5e1;
}

.custom-select ::v-deep {
  .el-input__inner {
    background-color: rgba(0, 0, 0, 0.3) !important;
    border: 1px solid rgba(255, 255, 255, 0.12) !important;
    color: #e2e8f0 !important;
    border-radius: 8px;
    height: 36px;
    line-height: 36px;
    transition: all 0.3s ease;
    width: 220px;
    
    &:focus, &:hover {
      border-color: rgba(124, 92, 255, 0.6) !important;
      box-shadow: 0 0 8px rgba(124, 92, 255, 0.2);
    }
  }
}

.character-select ::v-deep {
  .el-input__inner {
    width: 160px !important;
  }
}

.character-control {
  margin-left: 20px;
}

.label-wrapper {
  flex-shrink: 0;
}

.desktop-badge {
  display: inline;
}

.mobile-badge {
  display: none;
}

.device-badge {
  display: flex;
  align-items: center;
  gap: 8px;
  background: rgba(0, 229, 200, 0.1);
  border: 1px solid rgba(0, 229, 200, 0.3);
  padding: 6px 12px;
  border-radius: 20px;
  
  .badge-dot {
    width: 8px;
    height: 8px;
    border-radius: 50%;
    background-color: #00e5c8;
    box-shadow: 0 0 8px #00e5c8;
  }
  
  .badge-text {
    font-size: 13px;
    color: #00e5c8;
    font-weight: 600;
  }
  
  &.loading {
    background: rgba(124, 92, 255, 0.1);
    border-color: rgba(124, 92, 255, 0.3);
    .badge-text {
      color: #9c8cff;
    }
  }
}

.glow-btn {
  background: linear-gradient(135deg, #7c5cff, #5b3fd9) !important;
  border: none !important;
  box-shadow: 0 4px 14px rgba(124, 92, 255, 0.3);
  border-radius: 8px;
  font-weight: 600;
  transition: all 0.3s ease;
  
  &:hover {
    transform: translateY(-1px);
    box-shadow: 0 6px 20px rgba(124, 92, 255, 0.45);
  }
}

.iframe-container {
  flex: 1;
  border: 1px solid rgba(255, 255, 255, 0.08);
  border-radius: 16px;
  overflow: hidden;
  background: #000;
  box-shadow: 0 20px 40px rgba(0, 0, 0, 0.4), 
              0 0 30px rgba(124, 92, 255, 0.03);
  display: flex;
  flex-direction: column;
}

.client-iframe {
  width: 100%;
  height: 100%;
  border: none;
  background: transparent;
}

.empty-state {
  display: flex;
  align-items: center;
  justify-content: center;
  height: 100%;
  background: rgba(18, 24, 41, 0.4);
  
  .empty-content {
    text-align: center;
    max-width: 400px;
    padding: 40px;
    
    .empty-icon {
      font-size: 64px;
      color: #3b4252;
      margin-bottom: 20px;
      animation: pulse 2s infinite;
    }
    
    h3 {
      font-size: 20px;
      font-weight: 700;
      color: #f1f5f9;
      margin: 0 0 10px 0;
    }
    
    p {
      font-size: 14px;
      color: #64748b;
      line-height: 1.5;
      margin: 0;
    }
  }
}

@keyframes pulse {
  0% {
    transform: scale(1);
    opacity: 0.5;
  }
  50% {
    transform: scale(1.05);
    opacity: 0.8;
  }
  100% {
    transform: scale(1);
    opacity: 0.5;
  }
}

/* Responsive Styles for Phones & Tablets */
@media (max-width: 768px) {
  .live-main {
    padding: 10px;
    gap: 10px;
    height: calc(100vh - 56px);
    height: calc(100dvh - 56px);
  }

  .control-panel {
    display: flex;
    flex-wrap: wrap;
    align-items: center;
    justify-content: flex-start;
    gap: 12px 16px;
    padding: 10px 16px;
    border-radius: 10px;
  }

  .control-left {
    display: flex;
    align-items: center;
    gap: 8px;
    flex: 1 1 200px;
    min-width: 0;
  }

  .character-control {
    margin-left: 0 !important;
  }

  .label-wrapper {
    display: flex;
    align-items: center;
    flex-shrink: 0;
    .control-label {
      display: none; /* Hide textual label on mobile/tablet to save space */
    }
    .agent-icon {
      font-size: 18px;
      color: #7c5cff;
      margin-right: 2px;
    }
  }

  .custom-select {
    flex: 1;
    width: 100%;
  }

  .custom-select ::v-deep {
    .el-input__inner {
      width: 100% !important;
      min-width: 120px;
      height: 32px;
      line-height: 32px;
      font-size: 13px;
    }
  }

  .desktop-badge {
    display: none;
  }

  .mobile-badge {
    display: inline;
  }

  .control-right {
    display: flex;
    align-items: center;
    justify-content: flex-end;
    flex-shrink: 0;
    
    .device-badge {
      padding: 4px 8px;
      border-radius: 12px;
      .badge-text {
        font-size: 11px;
      }
      .badge-dot {
        width: 6px;
        height: 6px;
      }
    }
    
    .glow-btn {
      padding: 6px 12px;
      font-size: 12px;
      height: 32px;
      line-height: 32px;
      display: flex;
      align-items: center;
      justify-content: center;
    }
  }

  .iframe-container {
    border-radius: 12px;
  }

  .empty-state {
    .empty-content {
      padding: 20px;
      .empty-icon {
        font-size: 48px;
        margin-bottom: 12px;
      }
      h3 {
        font-size: 16px;
      }
      p {
        font-size: 12px;
      }
    }
  }
}

/* Extra responsive styling for mobile phones */
@media (max-width: 576px) {
  .control-panel {
    flex-direction: column;
    align-items: stretch;
    gap: 10px;
    padding: 10px;
  }

  .control-left {
    width: 100%;
    flex: none;
    justify-content: space-between;
  }

  .control-right {
    width: 100%;
    flex: none;
    display: flex;
    justify-content: center;
    margin-top: 4px;

    .device-badge {
      width: 100%;
      justify-content: center;
    }
  }
}

/* Extra small devices (phones under 400px) */
@media (max-width: 380px) {
  .custom-select ::v-deep {
    .el-input__inner {
      min-width: 110px;
      font-size: 12px;
    }
  }
}
</style>
