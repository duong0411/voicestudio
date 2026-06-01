<template>
  <div class="device-item">
    <div style="display: flex;justify-content: space-between;">
    <el-tooltip :content="displayAgentName" placement="top" effect="light">
      <div class="device-item-title">
        {{ displayAgentName }}
      </div>
    </el-tooltip>
      <div>
        <img src="@/assets/home/delete.png" alt="" style="width: 18px;height: 18px;"
          @click.stop="handleDelete" />
      </div>
    </div>
    <div class="device-name">
      {{ $t('home.languageModel') }}：{{ displayLlmModelName }}
    </div>
    <div class="device-name">
      {{ $t('home.voiceModel') }}：{{ displayTtsModelName }} ({{ displayTtsVoiceName }})
    </div>
    <div class="device-actions">
      <div class="settings-btn" @click="handleConfigure">
        {{ $t('home.configureRole') }}
      </div>
      <div v-if="featureStatus.voiceprintRecognition" class="settings-btn" @click="handleVoicePrint">
        {{ $t('home.voiceprintRecognition') }}
      </div>
      <div class="settings-btn" @click="handleDeviceManage">
        {{ $t('home.deviceManagement') }}({{ device.deviceCount }})
      </div>
      <div :class="['settings-btn', { 'disabled-btn': device.memModelId === 'Memory_nomem' }]"
        @click="handleChatHistory">
        <el-tooltip effect="light" v-if="device.memModelId === 'Memory_nomem'" :content="$t('home.enableMemory')" placement="top">
          <span>{{ $t('home.chatHistory') }}</span>
        </el-tooltip>
        <span v-else>{{ $t('home.chatHistory') }}</span>
      </div>
    </div>
    <div class="version-info">
      <div>{{ $t('home.lastConversation') }}：{{ formattedLastConnectedTime }}</div>
      <el-tooltip :content="localizedTags.join(', ')" placement="top" effect="light">
        <div class="version-info-scroll">
          {{ localizedTags.join(', ') }}
        </div>
      </el-tooltip>
    </div>
  </div>
</template>

<script>
export default {
  name: 'DeviceItem',
  props: {
    device: { type: Object, required: true },
    featureStatus: { 
      type: Object, 
      default: () => ({
        voiceprintRecognition: false,
        voiceClone: false,
        knowledgeBase: false
      })
    }
  },
  data() {
    return { switchValue: false }
  },
  computed: {
    displayAgentName() {
      return this.$dbLabel(this.device.agentName);
    },
    displayLlmModelName() {
      return this.$dbLabel(this.device.llmModelName);
    },
    displayTtsModelName() {
      return this.$dbLabel(this.device.ttsModelName);
    },
    displayTtsVoiceName() {
      return this.$dbLabel(this.device.ttsVoiceName);
    },
    displaySystemPrompt() {
      return this.$dbSystemPrompt(this.device.systemPrompt, this.device.agentName);
    },
    formattedLastConnectedTime() {
      if (!this.device.lastConnectedAt) return this.$t('home.noConversation');

      const lastTime = new Date(this.device.lastConnectedAt);
      const now = new Date();
      const diffMinutes = Math.floor((now - lastTime) / (1000 * 60));

      if (diffMinutes <= 1) {
        return this.$t('home.justNow');
      } else if (diffMinutes < 60) {
        return this.$t('home.minutesAgo', { minutes: diffMinutes });
      } else if (diffMinutes < 24 * 60) {
        const hours = Math.floor(diffMinutes / 60);
        const minutes = diffMinutes % 60;
        return this.$t('home.hoursAgo', { hours, minutes });
      } else {
        return this.device.lastConnectedAt;
      }
    },
    tags() {
      if (!this.device.tags) return [];
      return this.device.tags.map((tag) => tag.tagName);
    },
    localizedTags() {
      return this.tags.map((name) => this.$dbLabel(name));
    },
  },
  methods: {
    handleDelete() {
      this.$emit('delete', this.device.agentId)
    },
    handleConfigure() {
      this.$router.push({ path: '/role-config', query: { agentId: this.device.agentId } });
    },
    handleVoicePrint() {
      this.$router.push({ path: '/voice-print', query: { agentId: this.device.agentId } });
    },
    handleDeviceManage() {
      this.$router.push({ path: '/device-management', query: { agentId: this.device.agentId } });
    },
    handleChatHistory() {
      if (this.device.memModelId === 'Memory_nomem') {
        return
      }
      this.$emit('chat-history', { agentId: this.device.agentId, agentName: this.device.agentName })
    }
  },
}
</script>
<style lang="scss" scoped>
.device-item {
  width: 100%;
  border-radius: 16px;
  background: rgba(22, 30, 52, 0.85);
  border: 1px solid rgba(0, 0, 0, 0.08);
  padding: 22px 22px 14px;
  box-sizing: border-box;
  transition: transform 0.25s ease, border-color 0.25s ease, box-shadow 0.25s ease;

  &:hover {
    transform: translateY(-4px);
    border-color: rgba(124, 92, 255, 0.45);
    box-shadow: 0 16px 40px rgba(0, 0, 0, 0.35);
  }

  &-title {
    flex: 1;
    font-weight: 700;
    font-size: 18px;
    color: #f1f5f9;
    text-align: left;
    text-overflow: ellipsis;
    white-space: nowrap;
    overflow: hidden;
  }
}

.device-name {
  margin: 7px 0 10px;
  font-weight: 400;
  font-size: 11px;
  color: #94a3b8;
  text-align: left;
}

.device-actions {
  display: flex;
  gap: 10px;
  align-items: center;
  flex-wrap: wrap;
}

.settings-btn {
  font-weight: 600;
  font-size: 12px;
  color: var(--val-accent, #00e5c8);
  background: rgba(0, 229, 200, 0.12);
  width: auto;
  padding: 0 12px;
  height: 21px;
  line-height: 21px;
  cursor: pointer;
  border-radius: 14px;
}

.settings-btn:hover {
  background: rgba(124, 92, 255, 0.25);
  color: var(--val-text) ;
}

.version-info {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: 15px;
  font-size: 12px;
  color: #64748b;
  font-weight: 400;
  &-scroll {
    margin-left: 20px;
    flex: 1;
    overflow: hidden;
    text-overflow: ellipsis;
    text-wrap: nowrap;
    text-align: right;
  }
}

.more-tag {
  cursor: pointer;
  flex-shrink: 0;
}

.all-tags-popover {
  display: flex;
  flex-wrap: wrap;
  gap: 4px;
}

.disabled-btn {
  background: #e6e6e6;
  color: #999;
  cursor: not-allowed;
}
</style>

<style>
.device-item-tooltip {
  max-height: 60vh !important;
  max-width: 400px !important;
  overflow-y: auto !important;
  scrollbar-width: thin;
  word-break: break-word;
}

.device-item-tooltip .popper__arrow {
  display: none !important;
}

.device-item-tooltip[x-placement^="top"] .popper__arrow {
  border-top-color: transparent !important;
}

.device-item-tooltip[x-placement^="bottom"] .popper__arrow {
  border-bottom-color: transparent !important;
}
</style>