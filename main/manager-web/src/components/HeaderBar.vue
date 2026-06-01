<template>
  <el-header class="header">
    <div class="header-container">
      <!-- 左侧元素 -->
      <div class="header-left" @click="handleRouter('home')">
        <BrandLogo compact size="sm" />
      </div>

      <!-- 中间导航菜单 -->
      <div class="header-center">
        <!-- Desktop layout -->
        <div class="nav-desktop">
          <div class="equipment-management" :class="{
            'active-tab':
              $route.path === '/home' ||
              $route.path === '/role-config' ||
              $route.path === '/device-management',
          }" @click="handleRouter('home')">
            <img loading="lazy" alt="" src="@/assets/header/robot.png" :style="{
              filter:
                $route.path === '/home' ||
                  $route.path === '/role-config' ||
                  $route.path === '/device-management'
                  ? 'brightness(0) invert(1)'
                  : 'None',
            }" />
            <span class="nav-text">{{ $t("header.smartManagement") }}</span>
          </div>

          <div class="equipment-management" :class="{
            'active-tab': $route.path === '/live',
          }" @click="handleRouter('live')">
            <img loading="lazy" alt="" src="@/assets/header/robot.png" :style="{
              filter:
                $route.path === '/live'
                  ? 'brightness(0) invert(1)'
                  : 'None',
            }" />
            <span class="nav-text">{{ $t("header.useWeb") }}</span>
          </div>
          <!-- Chỉ hiển thị Voice Clone -->
          <div v-if="featureStatus.voiceClone" class="equipment-management"
            :class="{ 'active-tab': $route.path === '/voice-clone-management' }" @click="handleRouter('voiceCloneManagement')">
            <img loading="lazy" alt="" src="@/assets/header/voice.png" :style="{
              filter:
                $route.path === '/voice-clone-management'
                  ? 'brightness(0) invert(1)'
                  : 'None',
            }" />
            <span class="nav-text">{{ $t("header.voiceCloneManagement") }}</span>
          </div>

          <div v-if="userInfo.superAdmin" class="equipment-management" :class="{ 'active-tab': $route.path === '/model-config' }"
            @click="handleRouter('modelConfig')">
            <img loading="lazy" alt="" src="@/assets/header/model_config.png" :style="{
              filter:
                $route.path === '/model-config' ? 'brightness(0) invert(1)' : 'None',
            }" />
            <span class="nav-text">{{ $t("header.modelConfig") }}</span>
          </div>
          <div v-if="featureStatus.knowledgeBase" class="equipment-management"
            :class="{ 'active-tab': $route.path === '/knowledge-base-management' || $route.path === '/knowledge-file-upload' }"
            @click="handleRouter('knowledgeBaseManagement')">
            <img loading="lazy" alt="" src="@/assets/header/knowledge_base.png" :style="{
              filter:
                $route.path === '/knowledge-base-management' || $route.path === '/knowledge-file-upload' ? 'brightness(0) invert(1)' : 'None',
            }" />
            <span class="nav-text">{{ $t("header.knowledgeBase") }}</span>
          </div>
          <el-dropdown v-if="userInfo.superAdmin" trigger="click" class="equipment-management more-dropdown" :class="{
            'active-tab':
              $route.path === '/dict-management' ||
              $route.path === '/params-management' ||
              $route.path === '/provider-management' ||
              $route.path === '/server-side-management' ||
              $route.path === '/agent-template-management' ||
              $route.path === '/ota-management' ||
              $route.path === '/user-management' ||
              $route.path === '/feature-management',
          }" @visible-change="handleParamDropdownVisibleChange">
            <span class="el-dropdown-link">
              <img loading="lazy" alt="" src="@/assets/header/param_management.png" :style="{
                filter:
                  $route.path === '/dict-management' ||
                    $route.path === '/params-management' ||
                    $route.path === '/provider-management' ||
                    $route.path === '/server-side-management' ||
                    $route.path === '/agent-template-management' ||
                    $route.path === '/ota-management' ||
                    $route.path === '/user-management' ||
                    $route.path === '/feature-management'
                    ? 'brightness(0) invert(1)'
                    : 'None',
              }" />
              <span class="nav-text">{{ $t("header.paramDictionary") }}</span>
              <i class="el-icon-arrow-down el-icon--right" :class="{ 'rotate-down': paramDropdownVisible }"></i>
            </span>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item @click.native="handleRouter('paramManagement')">
                {{ $t("header.paramManagement") }}
              </el-dropdown-item>
              <el-dropdown-item @click.native="handleRouter('userManagement')">
                {{ $t("header.userManagement") }}
              </el-dropdown-item>
              <el-dropdown-item @click.native="handleRouter('otaManagement')">
                {{ $t("header.otaManagement") }}
              </el-dropdown-item>
              <el-dropdown-item @click.native="handleRouter('dictManagement')">
                {{ $t("header.dictManagement") }}
              </el-dropdown-item>
              <el-dropdown-item @click.native="handleRouter('providerManagement')">
                {{ $t("header.providerManagement") }}
              </el-dropdown-item>
              <el-dropdown-item @click.native="handleRouter('agentTemplate')">
                {{ $t("header.agentTemplate") }}
              </el-dropdown-item>
              <el-dropdown-item @click.native="handleRouter('replacementWordManagement')">
                {{ $t("header.replacementWordManagement") }}
              </el-dropdown-item>
              <el-dropdown-item @click.native="handleRouter('serverSideManagement')">
                {{ $t("header.serverSideManagement") }}
              </el-dropdown-item>
              <el-dropdown-item @click.native="handleRouter('featureManagement')">
                  {{ $t("header.featureManagement") }}
                </el-dropdown-item>
              <el-dropdown-item v-if="featureStatus.voiceClone" @click.native="handleRouter('voiceResourceManagement')">
                  {{ $t("header.voiceResourceManagement") }}
                </el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </div>

        <!-- Mobile layout -->
        <el-dropdown trigger="click" class="nav-mobile-dropdown">
          <span class="mobile-menu-btn">
            <i class="el-icon-menu"></i>
            <span>{{ activeNavText }}</span>
          </span>
          <el-dropdown-menu slot="dropdown" class="mobile-menu-items">
            <el-dropdown-item @click.native="handleRouter('home')" :class="{ 'active-item': isHomeActive }">
              {{ $t("header.smartManagement") }}
            </el-dropdown-item>
            <el-dropdown-item @click.native="handleRouter('live')" :class="{ 'active-item': $route.path === '/live' }">
              {{ $t("header.useWeb") }}
            </el-dropdown-item>
            <el-dropdown-item v-if="featureStatus.voiceClone" @click.native="handleRouter('voiceCloneManagement')" :class="{ 'active-item': $route.path === '/voice-clone-management' }">
              {{ $t("header.voiceCloneManagement") }}
            </el-dropdown-item>
            <el-dropdown-item v-if="userInfo.superAdmin" @click.native="handleRouter('modelConfig')" :class="{ 'active-item': $route.path === '/model-config' }">
              {{ $t("header.modelConfig") }}
            </el-dropdown-item>
            <el-dropdown-item v-if="featureStatus.knowledgeBase" @click.native="handleRouter('knowledgeBaseManagement')" :class="{ 'active-item': $route.path === '/knowledge-base-management' || $route.path === '/knowledge-file-upload' }">
              {{ $t("header.knowledgeBase") }}
            </el-dropdown-item>
            
            <template v-if="userInfo.superAdmin">
              <el-dropdown-item divided class="menu-divider-label">{{ $t("header.paramDictionary") }}</el-dropdown-item>
              <el-dropdown-item @click.native="handleRouter('paramManagement')" :class="{ 'active-item': $route.path === '/params-management' }">{{ $t("header.paramManagement") }}</el-dropdown-item>
              <el-dropdown-item @click.native="handleRouter('userManagement')" :class="{ 'active-item': $route.path === '/user-management' }">{{ $t("header.userManagement") }}</el-dropdown-item>
              <el-dropdown-item @click.native="handleRouter('otaManagement')" :class="{ 'active-item': $route.path === '/ota-management' }">{{ $t("header.otaManagement") }}</el-dropdown-item>
              <el-dropdown-item @click.native="handleRouter('dictManagement')" :class="{ 'active-item': $route.path === '/dict-management' }">{{ $t("header.dictManagement") }}</el-dropdown-item>
              <el-dropdown-item @click.native="handleRouter('providerManagement')" :class="{ 'active-item': $route.path === '/provider-management' }">{{ $t("header.providerManagement") }}</el-dropdown-item>
              <el-dropdown-item @click.native="handleRouter('agentTemplate')" :class="{ 'active-item': $route.path === '/agent-template-management' }">{{ $t("header.agentTemplate") }}</el-dropdown-item>
              <el-dropdown-item @click.native="handleRouter('replacementWordManagement')" :class="{ 'active-item': $route.path === '/replacement-word-management' }">{{ $t("header.replacementWordManagement") }}</el-dropdown-item>
              <el-dropdown-item @click.native="handleRouter('serverSideManagement')" :class="{ 'active-item': $route.path === '/server-side-management' }">{{ $t("header.serverSideManagement") }}</el-dropdown-item>
              <el-dropdown-item @click.native="handleRouter('featureManagement')" :class="{ 'active-item': $route.path === '/feature-management' }">{{ $t("header.featureManagement") }}</el-dropdown-item>
              <el-dropdown-item v-if="featureStatus.voiceClone" @click.native="handleRouter('voiceResourceManagement')" :class="{ 'active-item': $route.path === '/voice-resource-management' }">{{ $t("header.voiceResourceManagement") }}</el-dropdown-item>
            </template>
          </el-dropdown-menu>
        </el-dropdown>
      </div>

      <!-- 右侧元素 -->
      <div class="header-right">
        <div class="search-container" v-if="$route.path === '/home' && !(userInfo.superAdmin && isSmallScreen)">
          <div class="search-wrapper">
            <el-input v-model="search" :placeholder="$t('header.searchPlaceholder')" class="custom-search-input"
              @keyup.enter.native="handleSearch" @focus="showSearchHistory" @blur="hideSearchHistory" clearable
              ref="searchInput">
              <i slot="suffix" class="el-icon-search search-icon" @click="handleSearch"></i>
            </el-input>
            <!-- 搜索历史下拉框 -->
            <div v-if="showHistory && searchHistory.length > 0" class="search-history-dropdown">
              <div class="search-history-header">
                <span>{{ $t("header.searchHistory") }}</span>
                <el-button type="text" size="small" class="clear-history-btn" @click="clearSearchHistory">
                  {{ $t("header.clearHistory") }}
                </el-button>
              </div>
              <div class="search-history-list">
                <div v-for="(item, index) in searchHistory" :key="index" class="search-history-item"
                  @click.stop="selectSearchHistory(item)">
                  <span class="history-text">{{ item }}</span>
                  <i class="el-icon-close clear-item-icon" @click.stop="removeSearchHistory(index)"></i>
                </div>
              </div>
            </div>
          </div>
        </div>

        <img loading="lazy" alt="" src="@/assets/home/avatar.png" class="avatar-img" @click="handleAvatarClick" />
        <span class="el-user-dropdown" @click="handleAvatarClick">
          <span class="user-name-text">{{ userInfo.username || $t('header.loading') }}</span>
          <i class="el-icon-arrow-down" :class="{ 'rotate-down': userMenuVisible }"></i>
        </span>
        <el-cascader :options="userMenuOptions" trigger="click" :props="cascaderProps"
          style="width: 0px; overflow: hidden" :show-all-levels="false" @change="handleCascaderChange"
          @visible-change="handleUserMenuVisibleChange" ref="userCascader">
          <template slot-scope="{ data }">
            <span>{{ data.label }}</span>
          </template>
        </el-cascader>
      </div>
    </div>

    <!-- 修改密码弹窗 -->
    <ChangePasswordDialog v-model="isChangePasswordDialogVisible" />
  </el-header>
</template>

<script>
import userApi from "@/apis/module/user";
import i18n, { changeLanguage } from "@/i18n";
import { mapActions, mapState } from "vuex";
import BrandLogo from './BrandLogo.vue';
import ChangePasswordDialog from "./ChangePasswordDialog.vue"; // 引入修改密码弹窗组件
import featureManager from "@/utils/featureManager"; // 引入功能管理工具类

export default {
  name: "HeaderBar",
  components: {
    BrandLogo,
    ChangePasswordDialog,
  },
  props: ["devices"], // 接收父组件设备列表
  data() {
    return {
      search: "",
      isChangePasswordDialogVisible: false, // 控制修改密码弹窗的显示
      paramDropdownVisible: false,
      voiceCloneDropdownVisible: false,
      userMenuVisible: false, // 添加用户菜单可见状态
      menuVisibleTimer: null, // 菜单显示定时器，防止够快触发
      isSmallScreen: false,
      // 搜索历史相关
      searchHistory: [],
      showHistory: false,
      SEARCH_HISTORY_KEY: "xiaozhi_search_history",
      MAX_HISTORY_COUNT: 3,
      // Cascader 配置
      cascaderProps: {
        expandTrigger: "click",
        value: "value",
        label: "label",
        children: "children",
      },
      // 跳转页面配置
      routerPaths: {
        home: "/home",
        modelConfig: "/model-config",
        knowledgeBaseManagement: "/knowledge-base-management",
        voiceCloneManagement: "/voice-clone-management",
        voiceResourceManagement: "/voice-resource-management",
        paramManagement: "/params-management",
        userManagement: "/user-management",
        otaManagement: "/ota-management",
        dictManagement: "/dict-management",
        providerManagement: "/provider-management",
        agentTemplate: "/agent-template-management",
        replacementWordManagement: "/replacement-word-management",
        serverSideManagement: "/server-side-management",
        featureManagement: "/feature-management",
        live: "/live",
      }
    };
  },
  computed: {
    ...mapState({
      featureStatus: (state) => ({
        voiceClone: state.pubConfig.systemWebMenu?.features?.voiceClone?.enabled, // 音色克隆功能状态
        knowledgeBase: state.pubConfig.systemWebMenu?.features?.knowledgeBase?.enabled, // 知识库功能状态
      }),
      userInfo: (state) => state.userInfo,
    }),
    isHomeActive() {
      const path = this.$route.path;
      return path === '/home' || path === '/role-config' || path === '/device-management';
    },
    activeNavText() {
      const path = this.$route.path;
      if (path === '/home' || path === '/role-config' || path === '/device-management') {
        return this.$t('header.smartManagement');
      }
      if (path === '/live') {
        return this.$t('header.useWeb');
      }
      if (path === '/voice-clone-management') {
        return this.$t('header.voiceCloneManagement');
      }
      if (path === '/model-config') {
        return this.$t('header.modelConfig');
      }
      if (path === '/knowledge-base-management' || path === '/knowledge-file-upload') {
        return this.$t('header.knowledgeBase');
      }
      return this.$t('header.paramDictionary');
    },
    // 获取当前语言
    currentLanguage() {
      return i18n.locale || "vi";
    },
    currentLanguageText() {
      return this.currentLanguage === "en"
        ? this.$t("language.en")
        : this.$t("language.vi");
    },
    xiaozhiAiIcon() {
      return this.currentLanguage === "en"
        ? require("@/assets/xiaozhi-ai_en.png")
        : require("@/assets/xiaozhi-ai_vi.png");
    },
    // 用户菜单选项
    userMenuOptions() {
      return [
        {
          label: this.currentLanguageText,
          value: "language",
          children: [
            {
              label: this.$t("language.vi"),
              value: "vi",
            },
            {
              label: this.$t("language.en"),
              value: "en",
            },
          ],
        },
        {
          label: this.$t("header.changePassword"),
          value: "changePassword",
        },
        {
          label: this.$t("header.logout"),
          value: "logout",
        },
      ];
    },
  },
  async mounted() {
    this.checkScreenSize();
    window.addEventListener("resize", this.checkScreenSize);
    // 从localStorage加载搜索历史
    this.loadSearchHistory();
    // 等待featureManager初始化完成后再加载功能状态
    await this.loadFeatureStatus();
  },
  //移除事件监听器
  beforeDestroy() {
    window.removeEventListener("resize", this.checkScreenSize);
  },
  methods: {
    handleRouter(type) {
      this.$router.push(this.routerPaths[type]);
    },
    // 加载功能状态
    async loadFeatureStatus() {
      // 等待featureManager初始化完成
      await featureManager.waitForInitialization();
    },
    checkScreenSize() {
      this.isSmallScreen = window.innerWidth <= 1386;
    },
    // 处理搜索
    handleSearch() {
      const searchValue = this.search.trim();

      // 如果搜索内容为空，触发重置事件
      if (!searchValue) {
        this.$emit("search-reset");
        return;
      }

      // 保存搜索历史
      this.saveSearchHistory(searchValue);

      // 触发搜索事件，将搜索关键词传递给父组件
      this.$emit("search", searchValue);

      // 搜索完成后让输入框失去焦点，从而触发blur事件隐藏搜索历史
      if (this.$refs.searchInput) {
        this.$refs.searchInput.blur();
      }
    },

    // 显示搜索历史
    showSearchHistory() {
      this.showHistory = true;
    },

    // 隐藏搜索历史
    hideSearchHistory() {
      // 延迟隐藏，以便点击事件能够执行
      setTimeout(() => {
        this.showHistory = false;
      }, 200);
    },

    // 加载搜索历史
    loadSearchHistory() {
      try {
        const history = localStorage.getItem(this.SEARCH_HISTORY_KEY);
        if (history) {
          this.searchHistory = JSON.parse(history);
        }
      } catch (error) {
        console.error("加载搜索历史失败:", error);
        this.searchHistory = [];
      }
    },

    // 保存搜索历史
    saveSearchHistory(keyword) {
      if (!keyword || this.searchHistory.includes(keyword)) {
        return;
      }

      // 添加到历史记录开头
      this.searchHistory.unshift(keyword);

      // 限制历史记录数量
      if (this.searchHistory.length > this.MAX_HISTORY_COUNT) {
        this.searchHistory = this.searchHistory.slice(0, this.MAX_HISTORY_COUNT);
      }

      // 保存到localStorage
      try {
        localStorage.setItem(this.SEARCH_HISTORY_KEY, JSON.stringify(this.searchHistory));
      } catch (error) {
        console.error("保存搜索历史失败:", error);
      }
    },

    // 选择搜索历史项
    selectSearchHistory(keyword) {
      this.search = keyword;
      this.handleSearch();
    },

    // 移除单个搜索历史项
    removeSearchHistory(index) {
      this.searchHistory.splice(index, 1);
      try {
        localStorage.setItem(this.SEARCH_HISTORY_KEY, JSON.stringify(this.searchHistory));
      } catch (error) {
        console.error("更新搜索历史失败:", error);
      }
    },

    // 清空所有搜索历史
    clearSearchHistory() {
      this.searchHistory = [];
      try {
        localStorage.removeItem(this.SEARCH_HISTORY_KEY);
      } catch (error) {
        console.error("清空搜索历史失败:", error);
      }
    },
    // 显示修改密码弹窗
    showChangePasswordDialog() {
      this.isChangePasswordDialogVisible = true;
      // 添加：显示修改密码弹窗后重置用户菜单可见状态
      this.userMenuVisible = false;
    },
    // 退出登录
    async handleLogout() {
      try {
        // 调用 Vuex 的 logout action
        await this.logout();
        this.$message.success({
          message: this.$t("message.success"),
          showClose: true,
        });
      } catch (error) {
        console.error("退出登录失败:", error);
        this.$message.error({
          message: this.$t("message.error"),
          showClose: true,
        });
      }
    },
    // 监听参数字典下拉菜单的可见状态变化
    handleParamDropdownVisibleChange(visible) {
      this.paramDropdownVisible = visible;
    },

    // 监听音色克隆下拉菜单的可见状态变化
    handleVoiceCloneDropdownVisibleChange(visible) {
      this.voiceCloneDropdownVisible = visible;
    },
    // 在data中添加一个key用于强制重新渲染组件
    // 处理 Cascader 选择变化
    handleCascaderChange(value) {
      if (!value || value.length === 0) {
        return;
      }

      const action = value[value.length - 1];

      // 处理语言切换
      if (value.length === 2 && value[0] === "language") {
        this.changeLanguage(action);
      } else {
        // 处理其他操作
        switch (action) {
          case "changePassword":
            this.showChangePasswordDialog();
            break;
          case "logout":
            this.handleLogout();
            break;
        }
      }

      // 操作完成后立即清空选择
      setTimeout(() => {
        this.completeResetCascader();
      }, 300);
    },

    // 切换语言
    changeLanguage(lang) {
      changeLanguage(lang);
      this.$message.success({
        message: this.$t("message.success"),
        showClose: true,
      });
      // 添加：切换语言后重置用户菜单可见状态
      this.userMenuVisible = false;
    },

    // 完全重置级联选择器
    completeResetCascader() {
      if (this.$refs.userCascader) {
        try {
          // 尝试所有可能的方法来清空选择
          // 1. 尝试使用组件提供的clearValue方法
          if (this.$refs.userCascader.clearValue) {
            this.$refs.userCascader.clearValue();
          }

          // 2. 直接清空内部属性
          if (this.$refs.userCascader.$data) {
            this.$refs.userCascader.$data.selectedPaths = [];
            this.$refs.userCascader.$data.displayLabels = [];
            this.$refs.userCascader.$data.inputValue = "";
            this.$refs.userCascader.$data.checkedValue = [];
            this.$refs.userCascader.$data.showAllLevels = false;
          }

          // 3. 操作DOM清除选中状态
          const menuElement = this.$refs.userCascader.$refs.menu;
          if (menuElement && menuElement.$el) {
            const activeItems = menuElement.$el.querySelectorAll(
              ".el-cascader-node.is-active"
            );
            activeItems.forEach((item) => item.classList.remove("is-active"));

            const checkedItems = menuElement.$el.querySelectorAll(
              ".el-cascader-node.is-checked"
            );
            checkedItems.forEach((item) => item.classList.remove("is-checked"));
          }

          console.log("Cascader values cleared");
        } catch (error) {
          console.error("清空选择值失败:", error);
        }
      }
    },

    // 点击头像触发cascader下拉菜单
    handleAvatarClick() {
      if (this.$refs.userCascader) {
        // 切换菜单可见状态
        this.userMenuVisible = !this.userMenuVisible;

        // 菜单收起时清空选择值
        if (!this.userMenuVisible) {
          this.completeResetCascader();
        }

        // 直接设置菜单的显隐状态
        try {
          // 尝试使用toggleDropDownVisible方法
          this.$refs.userCascader.toggleDropDownVisible(this.userMenuVisible);
        } catch (error) {
          // 如果toggle方法失败，尝试直接设置属性
          if (this.$refs.userCascader.$refs.menu) {
            this.$refs.userCascader.$refs.menu.showMenu(this.userMenuVisible);
          } else {
            console.error("Cannot access menu component");
          }
        }
      }
    },

    // 处理用户菜单可见性变化
    handleUserMenuVisibleChange(visible) {
      if (this.menuVisibleTimer) return;
      this.menuVisibleTimer = setTimeout(() => {
        this.userMenuVisible = visible;
        clearTimeout(this.menuVisibleTimer);
        this.menuVisibleTimer = null;
      }, 100);

      // 如果菜单关闭了，也要清空选择值
      if (!visible) {
        this.completeResetCascader();
      }
    },

    // 使用 mapActions 引入 Vuex 的 logout action
    ...mapActions(["logout"]),
  },
};
</script>

<style lang="scss" scoped>
.header {
  background: rgba(18, 24, 41, 0.85);
  border-bottom: 1px solid var(--val-border, rgba(0, 0, 0, 0.1));
  backdrop-filter: blur(16px);
  height: 64px !important;
  min-width: 900px;
  overflow: visible;
}

.header-container {
  display: flex;
  justify-content: space-between;
  align-items: center;
  height: 100%;
  padding: 0 10px;
}

.header-left {
  display: flex;
  align-items: center;
  gap: 10px;
  min-width: 120px;
  cursor: pointer;
}

.logo-img {
  width: 42px;
  height: 42px;
}

.brand-img {
  height: 20px;
}

.header-center {
  display: flex;
  align-items: center;
  gap: 25px;
  position: absolute;
  left: 50%;
  transform: translateX(-50%);
}

.header-right {
  display: flex;
  align-items: center;
  gap: 7px;
  min-width: 200px;
  justify-content: flex-end;
}

.equipment-management {
  height: 34px;
  border-radius: 17px;
  background: rgba(0, 0, 0, 0.06);
  border: 1px solid rgba(0, 0, 0, 0.08);
  display: flex;
  justify-content: center;
  font-size: 13px;
  font-weight: 600;
  gap: 7px;
  color: #94a3b8;
  margin-left: 1px;
  align-items: center;
  transition: all 0.25s ease;
  cursor: pointer;
  flex-shrink: 0;
  padding: 0 15px;
  position: relative;

  &:hover {
    color: #e2e8f0;
    border-color: rgba(124, 92, 255, 0.4);
    transform: translateY(-1px);
  }
}

.equipment-management.active-tab {
  background: linear-gradient(135deg, #7c5cff, #5b3fd9) !important;
  color: var(--val-text) !important;
  border-color: transparent !important;
  box-shadow: 0 8px 24px rgba(124, 92, 255, 0.35);
}

.equipment-management img {
  width: 15px;
  height: 13px;
}

.search-container {
  margin-right: 5px;
  flex: 0.9;
  min-width: 60px;
  max-width: none;
}

.search-wrapper {
  position: relative;
}

.search-history-dropdown {
  position: absolute;
  top: 100%;
  left: 0;
  right: 0;
  background: transparent ;
  border: 1px solid #e4e6ef;
  border-radius: 4px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  z-index: 1000;
  margin-top: 2px;
}

.search-history-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 8px 12px;
  border-bottom: 1px solid #f0f0f0;
  font-size: 12px;
  color: #909399;
}

.clear-history-btn {
  color: #909399;
  font-size: 11px;
  padding: 0;
  height: auto;
}

.clear-history-btn:hover {
  color: #606266;
}

.search-history-list {
  max-height: 200px;
  overflow-y: auto;
}

.search-history-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 8px 12px;
  cursor: pointer;
  font-size: 12px;
  color: #606266;
}

.search-history-item:hover {
  background-color: #f5f7fa;
}

.clear-item-icon {
  font-size: 10px;
  color: #909399;
  visibility: hidden;
}
.more-dropdown {
  padding: 0;
}
.more-dropdown .el-dropdown-link {
  display: flex;
  align-items: center;
  gap: 7px;
  height: 100%;
  padding: 0 15px;
}

.search-history-item:hover .clear-item-icon {
  visibility: visible;
}

.clear-item-icon:hover {
  color: #ff4949;
}

.custom-search-input>>>.el-input__inner {
  height: 32px;
  border-radius: 16px;
  background-color: rgba(0, 0, 0, 0.25);
  border: 1px solid rgba(0, 0, 0, 0.12);
  color: #e2e8f0;
  padding-left: 12px;
  font-size: 12px;
  width: 100%;
}

.search-icon {
  cursor: pointer;
  color: #909399;
  margin-right: 3px;
  font-size: 9px;
  line-height: 18px;
}

.custom-search-input::v-deep .el-input__suffix-inner {
  display: flex;
  align-items: center;
  height: 100%;
}

.avatar-img {
  width: 21px;
  height: 21px;
  flex-shrink: 0;
  cursor: pointer;
}
.el-user-dropdown {
  cursor: pointer;
  color: #cbd5e1;
  font-size: 13px;
  font-weight: 500;
  display: flex;
  align-items: center;
  gap: 4px;
}

.user-name-text {
  display: inline-block;
  max-width: 130px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  vertical-align: middle;
}

/* 导航文本样式 - 支持中英文换行 */
.nav-text {
  white-space: normal;
  text-align: center;
  max-width: 80px;
  line-height: 1.2;
}

/* 响应式调整 */
@media (max-width: 1400px) {
  .header-center {
    position: static !important;
    transform: none !important;
    margin: 0 auto;
    gap: 14px;
  }

  .equipment-management {
    min-width: 80px;
    font-size: 10px;
  }
}

.equipment-management.more-dropdown {
  position: relative;
}

.equipment-management.more-dropdown .el-dropdown-menu {
  position: absolute;
  right: 0;
  min-width: 120px;
  margin-top: 5px;
}

.el-dropdown-menu__item {
  min-width: 60px;
  padding: 8px 20px;
  font-size: 14px;
  color: #606266;
  white-space: nowrap;
}

/* 添加倒三角旋转样式 */
.rotate-down {
  transform: rotate(180deg);
  transition: transform 0.3s ease;
}

.el-icon-arrow-down {
  transition: transform 0.3s ease;
}

@media (max-width: 900px) {
  .header {
    min-width: auto !important;
    width: 100%;
    padding: 0 12px;
  }
  
  .header-left {
    min-width: auto;
  }
  
  .brand-img {
    display: none;
  }

  .nav-text {
    font-size: 13px;
  }

  .equipment-management {
    padding: 0 10px !important;
    gap: 4px !important;
    img {
      width: 18px;
      height: 18px;
    }
  }

  .header-container {
    display: flex !important;
    justify-content: space-between !important;
    gap: 10px !important;
  }

  .header-center {
    position: static !important;
    transform: none !important;
    margin: 0 !important;
    flex: 1 !important;
    display: flex !important;
    justify-content: center !important;
    gap: 8px !important;
  }

  .header-right {
    min-width: auto !important;
    gap: 12px !important;
  }
}

@media (max-width: 900px) {
  .header-center {
    .equipment-management img {
      display: none;
    }
  }
  
  .el-user-dropdown {
    display: none !important;
  }

  .nav-desktop {
    display: none !important;
  }
  
  .nav-mobile-dropdown {
    display: block !important;
  }
  
  .search-container {
    display: none !important;
  }
  
  .header-left .brand-text {
    display: none !important;
  }
}

.nav-desktop {
  display: flex;
  align-items: center;
  gap: 25px;
}

.nav-mobile-dropdown {
  display: none;
}

.mobile-menu-btn {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 8px 16px;
  background: rgba(124, 92, 255, 0.15);
  border: 1px solid rgba(124, 92, 255, 0.3);
  border-radius: 20px;
  color: #fff;
  font-size: 13px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
  box-shadow: 0 0 15px rgba(124, 92, 255, 0.1);
  
  &:hover {
    background: rgba(124, 92, 255, 0.25);
    border-color: rgba(124, 92, 255, 0.5);
  }
  
  i {
    font-size: 16px;
    color: var(--val-accent, #00e5c8);
  }
}

.mobile-menu-items.el-dropdown-menu {
  background: rgba(18, 24, 41, 0.95) !important;
  border: 1px solid rgba(124, 92, 255, 0.2) !important;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.5) !important;
  backdrop-filter: blur(12px) !important;
  max-height: 75vh !important;
  overflow-y: auto !important;
  
  /* Custom slim scrollbar for dark mode */
  &::-webkit-scrollbar {
    width: 5px;
  }
  &::-webkit-scrollbar-track {
    background: transparent;
  }
  &::-webkit-scrollbar-thumb {
    background: rgba(124, 92, 255, 0.3);
    border-radius: 3px;
  }
  &::-webkit-scrollbar-thumb:hover {
    background: rgba(124, 92, 255, 0.5);
  }
  
  .el-dropdown-menu__item {
    color: #94a3b8 !important;
    padding: 10px 20px !important;
    font-weight: 500;
    
    &:hover, &.active-item {
      background: linear-gradient(135deg, rgba(124, 92, 255, 0.2), rgba(91, 63, 217, 0.2)) !important;
      color: #fff !important;
    }
  }
  
  .menu-divider-label {
    font-size: 11px !important;
    text-transform: uppercase !important;
    letter-spacing: 0.1em !important;
    color: var(--val-accent, #00e5c8) !important;
    padding: 12px 20px 6px !important;
    font-weight: 700 !important;
    pointer-events: none !important;
    background: transparent !important;
  }
}
</style>
