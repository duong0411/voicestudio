<template>
  <div class="welcome val-app-shell">
    <HeaderBar :devices="devices" @search="handleSearch" @search-reset="handleSearchReset" />
    <el-main class="studio-main">
      <div>
        <div class="studio-hero">
          <div class="studio-hero-content">
            <p class="studio-hero-label">{{ $t('brand.name') }}</p>
            <h1 class="studio-hero-title">{{ $t('home.greeting') }}</h1>
            <p class="studio-hero-sub">{{ $t('home.wish') }}</p>
            <div class="studio-stats">
              <div class="studio-stat">
                <span class="studio-stat-num">{{ devices.length }}</span>
                <span class="studio-stat-label">{{ $t('home.statAgents') }}</span>
              </div>
              <div class="studio-stat">
                <span class="studio-stat-num">{{ totalDevices }}</span>
                <span class="studio-stat-label">{{ $t('home.statDevices') }}</span>
              </div>
            </div>
            <button type="button" class="studio-cta" @click="showAddDialog">
              <i class="el-icon-plus" />
              {{ $t('home.addAgent') }}
              <i class="el-icon-arrow-right" />
            </button>
          </div>
          <div class="studio-hero-visual" aria-hidden="true">
            <div class="studio-ring studio-ring-1" />
            <div class="studio-ring studio-ring-2" />
            <i class="el-icon-microphone studio-mic-icon" />
          </div>
        </div>
        
        <div class="mobile-search-bar">
          <el-input v-model="searchKey" :placeholder="$t('header.searchPlaceholder')" class="custom-search-input"
            @keyup.enter.native="handleMobileSearch" @clear="handleMobileSearchClear" clearable>
            <i slot="suffix" class="el-icon-search search-icon" @click="handleMobileSearch"></i>
          </el-input>
        </div>

        <div class="device-list-container">
          <template v-if="isLoading">
            <div v-for="i in skeletonCount" :key="'skeleton-' + i" class="skeleton-item">
              <div class="skeleton-shimmer" />
            </div>
          </template>
          <template v-else>
            <DeviceItem
              v-for="(item, index) in devices"
              :key="index"
              :device="item"
              :feature-status="featureStatus"
              @configure="goToRoleConfig"
              @deviceManage="handleDeviceManage"
              @delete="handleDeleteAgent"
              @chat-history="handleShowChatHistory"
            />
          </template>
        </div>
      </div>
      <AddWisdomBodyDialog :visible.sync="addDeviceDialogVisible" @confirm="handleWisdomBodyAdded" />
    </el-main>
    <el-footer>
      <version-footer />
    </el-footer>
    <chat-history-dialog
      :visible.sync="showChatHistory"
      :agent-id="currentAgentId"
      :agent-name="currentAgentName"
    />
  </div>
</template>

<script>
import Api from '@/apis/api';
import AddWisdomBodyDialog from '@/components/AddWisdomBodyDialog.vue';
import ChatHistoryDialog from '@/components/ChatHistoryDialog.vue';
import DeviceItem from '@/components/DeviceItem.vue';
import HeaderBar from '@/components/HeaderBar.vue';
import VersionFooter from '@/components/VersionFooter.vue';
import featureManager from '@/utils/featureManager';

export default {
  name: 'HomePage',
  components: { DeviceItem, AddWisdomBodyDialog, HeaderBar, VersionFooter, ChatHistoryDialog },
  data() {
    return {
      addDeviceDialogVisible: false,
      devices: [],
      originalDevices: [],
      isSearching: false,
      searchRegex: null,
      isLoading: true,
      skeletonCount: localStorage.getItem('skeletonCount') || 8,
      showChatHistory: false,
      currentAgentId: '',
      currentAgentName: '',
      searchKey: '',
      featureStatus: {
        voiceprintRecognition: false,
        voiceClone: false,
        knowledgeBase: false,
      },
    };
  },
  computed: {
    totalDevices() {
      return this.devices.reduce((sum, d) => sum + (d.deviceCount || 0), 0);
    },
  },
  async mounted() {
    this.fetchAgentList();
    await this.loadFeatureStatus();
  },
  methods: {
    async loadFeatureStatus() {
      await featureManager.waitForInitialization();
      const config = featureManager.getConfig();
      this.featureStatus = {
        voiceprintRecognition: config.voiceprintRecognition,
        voiceClone: config.voiceClone,
        knowledgeBase: config.knowledgeBase,
      };
    },
    showAddDialog() {
      this.addDeviceDialogVisible = true;
    },
    goToRoleConfig() {
      this.$router.push('/role-config');
    },
    handleWisdomBodyAdded() {
      this.fetchAgentList();
      this.addDeviceDialogVisible = false;
    },
    handleDeviceManage() {
      this.$router.push('/device-management');
    },
    handleSearch(keyword) {
      this.isSearching = true;
      this.isLoading = true;
      const isMac = /^([0-9A-Fa-f]{2}:){5}[0-9A-Fa-f]{2}$/.test(keyword);
      const searchType = isMac ? 'mac' : 'name';
      Api.agent.searchAgent(
        keyword,
        searchType,
        ({ data }) => {
          if (data?.data) {
            this.devices = data.data.map((item) => ({ ...item, agentId: item.id }));
          }
          this.isLoading = false;
        },
        () => {
          this.isLoading = false;
          this.$message.error(this.$t('message.searchFailed'));
        }
      );
    },
    handleSearchReset() {
      this.isSearching = false;
      this.devices = [...this.originalDevices];
    },
    handleMobileSearch() {
      if (this.searchKey.trim()) {
        this.handleSearch(this.searchKey.trim());
      } else {
        this.handleMobileSearchClear();
      }
    },
    handleMobileSearchClear() {
      this.searchKey = '';
      this.handleSearchReset();
    },
    fetchAgentList() {
      this.isLoading = true;
      Api.agent.getAgentList(
        ({ data }) => {
          if (data?.data) {
            this.originalDevices = data.data.map((item) => ({ ...item, agentId: item.id }));
            this.skeletonCount = Math.min(Math.max(this.originalDevices.length, 3), 10);
            this.handleSearchReset();
          }
          this.isLoading = false;
        },
        () => {
          this.isLoading = false;
        }
      );
    },
    handleDeleteAgent(agentId) {
      this.$confirm(this.$t('home.confirmDeleteAgent'), this.$t('common.tip'), {
        confirmButtonText: this.$t('button.ok'),
        cancelButtonText: this.$t('button.cancel'),
        type: 'warning',
      }).then(() => {
        Api.agent.deleteAgent(agentId, (res) => {
          if (res.data.code === 0) {
            this.$message.success({ message: this.$t('home.deleteSuccess'), showClose: true });
            this.fetchAgentList();
          } else {
            this.$message.error({ message: res.data.msg || this.$t('home.deleteFailed'), showClose: true });
          }
        });
      }).catch(() => {});
    },
    handleShowChatHistory({ agentId, agentName }) {
      this.currentAgentId = agentId;
      this.currentAgentName = agentName;
      this.showChatHistory = true;
    },
  },
};
</script>

<style scoped lang="scss">
.studio-main {
  padding: 20px 24px 32px;
  display: flex;
  flex-direction: column;
}

.welcome {
  min-width: auto;
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}

.studio-hero {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 24px;
  padding: 32px 36px;
  border-radius: 20px;
  background: linear-gradient(135deg, rgba(124, 92, 255, 0.2) 0%, rgba(0, 229, 200, 0.08) 100%);
  border: 1px solid rgba(0, 0, 0, 0.1);
  margin-bottom: 28px;
  overflow: hidden;
  position: relative;
}

.studio-hero-content {
  flex: 1;
  z-index: 1;
}

.studio-hero-label {
  font-size: 0.75rem;
  font-weight: 700;
  letter-spacing: 0.2em;
  text-transform: uppercase;
  color: var(--val-accent, #00e5c8);
  margin: 0 0 8px;
}

.studio-hero-title {
  margin: 0;
  font-size: 2rem;
  font-weight: 800;
  letter-spacing: -0.03em;
  color: #f1f5f9;
}

.studio-hero-sub {
  margin: 8px 0 20px;
  color: #94a3b8;
  font-size: 1rem;
}

.studio-stats {
  display: flex;
  gap: 24px;
  margin-bottom: 24px;
}

.studio-stat-num {
  display: block;
  font-size: 1.75rem;
  font-weight: 800;
  color: var(--val-text) ;
}

.studio-stat-label {
  font-size: 0.8rem;
  color: #64748b;
}

.studio-cta {
  display: inline-flex;
  align-items: center;
  gap: 10px;
  padding: 12px 24px;
  border: none;
  border-radius: 12px;
  background: linear-gradient(135deg, #7c5cff, #5b3fd9);
  color: var(--val-text) ;
  font-weight: 700;
  font-size: 0.95rem;
  cursor: pointer;
  box-shadow: 0 8px 28px rgba(124, 92, 255, 0.4);
  transition: transform 0.2s ease, box-shadow 0.2s ease;

  &:hover {
    transform: translateY(-2px);
    box-shadow: 0 12px 36px rgba(124, 92, 255, 0.5);
  }
}

.studio-hero-visual {
  position: relative;
  width: 140px;
  height: 140px;
  flex-shrink: 0;
}

.studio-ring {
  position: absolute;
  border-radius: 50%;
  border: 2px solid rgba(124, 92, 255, 0.35);
}

.studio-ring-1 {
  inset: 0;
  animation: pulse-ring 2.5s ease-out infinite;
}

.studio-ring-2 {
  inset: 18px;
  border-color: rgba(0, 229, 200, 0.35);
  animation: pulse-ring 2.5s ease-out infinite 0.6s;
}

.studio-mic-icon {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  font-size: 48px;
  color: var(--val-primary, #7c5cff);
}

@keyframes pulse-ring {
  0% {
    transform: scale(0.9);
    opacity: 0.8;
  }
  100% {
    transform: scale(1.15);
    opacity: 0;
  }
}

.device-list-container {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(360px, 1fr));
  gap: 20px;
}

.skeleton-item {
  height: 160px;
  border-radius: 16px;
  background: rgba(0, 0, 0, 0.04);
  border: 1px solid rgba(0, 0, 0, 0.06);
  position: relative;
  overflow: hidden;
}

.skeleton-shimmer {
  position: absolute;
  inset: 0;
  background: linear-gradient(
    90deg,
    transparent,
    rgba(0, 0, 0, 0.06),
    transparent
  );
  animation: shimmer 1.5s infinite;
}

@keyframes shimmer {
  100% {
    transform: translateX(100%);
  }
}

@media (max-width: 900px) {
  .studio-main {
    padding: 16px 16px 24px;
  }
  
  .studio-hero {
    flex-direction: column;
    align-items: stretch;
    padding: 24px 24px;
    gap: 16px;
    text-align: center;
  }

  .studio-hero-content {
    display: flex;
    flex-direction: column;
    align-items: center;
  }
  
  .studio-hero-title {
    font-size: 1.5rem;
  }

  .studio-stats {
    justify-content: center;
    gap: 16px;
  }
  
  .studio-hero-visual {
    display: none;
  }

  .device-list-container {
    grid-template-columns: 1fr;
    gap: 16px;
  }
}

.mobile-search-bar {
  display: none;
  margin-bottom: 20px;
  width: 100%;
}

.mobile-search-bar ::v-deep .el-input__inner {
  height: 42px;
  line-height: 42px;
  border-radius: 21px;
  background-color: rgba(22, 30, 52, 0.85) !important;
  border: 1px solid rgba(124, 92, 255, 0.2) !important;
  color: #e2e8f0 !important;
  padding-left: 20px;
  font-size: 14px;
}

.mobile-search-bar ::v-deep .el-input__inner:focus {
  border-color: rgba(124, 92, 255, 0.6) !important;
  box-shadow: 0 0 10px rgba(124, 92, 255, 0.2);
}

.mobile-search-bar .search-icon {
  cursor: pointer;
  color: var(--val-accent, #00e5c8);
  font-size: 16px;
  line-height: 42px;
  margin-right: 10px;
  transition: color 0.3s ease;
}

.mobile-search-bar .search-icon:hover {
  color: #fff;
}

@media (max-width: 900px) {
  .mobile-search-bar {
    display: block;
  }
}
</style>
