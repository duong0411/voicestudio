<template>
  <div id="app">
    <router-view />
    <cache-viewer v-if="isCDNEnabled" :visible.sync="showCacheViewer" />
  </div>
</template>

<style lang="scss">
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
}

nav {
  padding: 30px;

  a {
    font-weight: bold;
    color: #2c3e50;

    &.router-link-exact-active {
      color: #42b983;
    }
  }
}

.copyright {
  padding: 0 !important;
  color: rgb(0, 0, 0);
  font-size: 12px;
  font-weight: 400;
  margin-top: auto;
  width: 100%;
  height: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
}

.el-message {
  top: 70px !important;
}

.welcome {
  min-width: auto !important;
}

.content-area {
  min-width: auto !important;
}

@media (max-width: 900px) {
  /* Welcome container responsiveness */
  .welcome {
    min-width: auto !important;
    min-height: auto !important;
    height: auto !important;
    overflow-y: auto !important;
  }

  /* Layout wrappers responsiveness */
  .main-wrapper {
    height: auto !important;
    margin: 0 10px 20px 10px !important;
    border-radius: 12px !important;
    flex: none !important;
  }

  .content-panel {
    height: auto !important;
    overflow: visible !important;
  }

  .content-area {
    min-width: auto !important;
    height: auto !important;
    overflow-x: auto !important;
    -webkit-overflow-scrolling: touch;
  }

  /* Table responsiveness */
  .transparent-table {
    height: auto !important;
    max-height: none !important;
    
    .el-table__body-wrapper {
      max-height: none !important;
      overflow-y: visible !important;
    }
  }

  /* Operation bar vertical layout on mobile */
  .operation-bar {
    flex-direction: column !important;
    align-items: flex-start !important;
    gap: 12px !important;
    padding: 12px 16px !important;
  }
  
  .page-title {
    font-size: 20px !important;
  }
  
  .right-operations {
    width: 100% !important;
    display: flex !important;
    gap: 8px !important;
    margin-left: 0 !important;
    
    .search-input {
      flex: 1 !important;
      width: auto !important;
    }
  }

  /* Pagination & buttons vertical stacking on mobile */
  .table_bottom {
    flex-direction: column !important;
    align-items: center !important;
    gap: 16px !important;
    padding-bottom: 16px !important;
  }
  
  .ctrl_btn {
    padding-left: 0 !important;
    justify-content: center !important;
    flex-wrap: wrap !important;
    gap: 6px !important;
    width: 100% !important;
  }
  
  .custom-pagination {
    flex-wrap: wrap !important;
    justify-content: center !important;
    gap: 8px !important;
    width: 100% !important;
    
    .el-select {
      margin-right: 0 !important;
    }
    
    .total-text {
      margin-left: 0 !important;
      width: 100% !important;
      text-align: center !important;
    }
  }

  /* Override Element UI dialog width on mobile */
  .el-dialog {
    width: 92% !important;
    max-width: 92% !important;
    margin-top: 10vh !important;
  }
  
  /* Make all form items stack vertically on mobile */
  .el-form-item {
    margin-right: 0 !important;
    display: flex !important;
    flex-direction: column !important;
    align-items: stretch !important;
    
    .el-form-item__label {
      text-align: left !important;
      float: none !important;
      margin-bottom: 6px !important;
      line-height: 1.5 !important;
      width: auto !important;
    }
    
    .el-form-item__content {
      margin-left: 0 !important;
      width: 100% !important;
    }

    .el-select,
    .el-input,
    .el-textarea {
      width: 100% !important;
    }
  }

  /* Multi-column grid layout to single column on mobile */
  .form-grid {
    grid-template-columns: 1fr !important;
    gap: 12px !important;
  }

  /* Model rows styling on mobile */
  .model-row {
    flex-direction: column !important;
    gap: 12px !important;
    margin-bottom: 12px !important;
    
    .language-select-item {
      max-width: 100% !important;
      flex: none !important;
    }
  }

  /* Config header stacking on mobile */
  .config-header {
    flex-direction: column !important;
    align-items: stretch !important;
    gap: 12px !important;
    padding: 12px 10px !important;
  }
  
  .header-actions {
    margin-left: 0 !important;
    justify-content: flex-end !important;
    width: 100% !important;
    display: flex !important;
    gap: 8px !important;
    flex-wrap: wrap !important;
    
    .hint-text {
      width: 100% !important;
      margin-bottom: 4px !important;
    }
  }

  /* Feature Management page responsive fixes */
  .feature-groups-container {
    flex-direction: column !important;
    gap: 20px !important;
  }
  
  .feature-groups-container::before {
    display: none !important;
  }
  
  .feature-group {
    width: 100% !important;
  }
  
  .features-grid {
    grid-template-columns: repeat(auto-fill, minmax(280px, 1fr)) !important;
  }

  /* Model Config page horizontal tabs on mobile */
  .content-panel {
    flex-direction: column !important;
  }
  
  .nav-panel {
    width: 100% !important;
    min-width: auto !important;
    height: auto !important;
    flex-direction: row !important;
    overflow-x: auto !important;
    -webkit-overflow-scrolling: touch;
    flex-wrap: nowrap !important;
    border-right: none !important;
    border-bottom: 1px solid var(--val-border) !important;
    padding: 8px !important;
    flex-shrink: 0 !important;
    background: transparent !important;
  }
  
  .nav-panel .el-menu-item {
    margin: 0 4px !important;
    width: auto !important;
    height: 38px !important;
    line-height: 38px !important;
    border-radius: var(--val-radius-sm, 8px) !important;
    flex-shrink: 0 !important;
    justify-content: center !important;
    padding: 0 16px !important;
    background: rgba(255, 255, 255, 0.05) !important;
    border: 1px solid rgba(255, 255, 255, 0.08) !important;
  }
  
  .nav-panel .el-menu-item.is-active {
    padding-left: 16px !important;
    padding-right: 16px !important;
    box-shadow: none !important;
  }
  
  .nav-panel .el-menu-item.is-active::before {
    display: none !important;
  }
  
  .nav-panel .el-menu-item.is-active .menu-text {
    color: #fff !important;
  }
  
  .content-area {
    width: 100% !important;
    min-width: auto !important;
    flex: 1 !important;
  }
}
</style>
<script>
import CacheViewer from '@/components/CacheViewer.vue';
import { logCacheStatus } from '@/utils/cacheViewer';

export default {
  name: 'App',
  components: {
    CacheViewer
  },
  data() {
    return {
      showCacheViewer: false,
      isCDNEnabled: process.env.VUE_APP_USE_CDN === 'true'
    };
  },
  created() {
    // 挂载 store 状态
    try {
      const userInfoStr = localStorage.getItem('userInfo');
      if (userInfoStr && userInfoStr !== 'undefined' && userInfoStr !== 'null') {
        this.$store.commit('setUserInfo', JSON.parse(userInfoStr));
      } else {
        this.$store.commit('setUserInfo', {});
      }
    } catch (e) {
      console.error('Failed to parse userInfo:', e);
      this.$store.commit('setUserInfo', {});
    }

    try {
      const pubConfigStr = localStorage.getItem('pubConfig');
      if (pubConfigStr && pubConfigStr !== 'undefined' && pubConfigStr !== 'null') {
        this.$store.commit('setPubConfig', JSON.parse(pubConfigStr));
      } else {
        this.$store.commit('setPubConfig', {});
      }
    } catch (e) {
      console.error('Failed to parse pubConfig:', e);
      this.$store.commit('setPubConfig', {});
    }
  },
  mounted() {
    // 检测是否为移动设备且VUE_APP_H5_URL不为空，如果两个条件都满足则跳转到H5页面
    if (this.isMobileDevice() && process.env.VUE_APP_H5_URL) {
      window.location.href = process.env.VUE_APP_H5_URL;
      return;
    }
    
    // 只有在启用CDN时才添加相关事件和功能
    if (this.isCDNEnabled) {
      // 添加全局快捷键Alt+C用于显示缓存查看器
      document.addEventListener('keydown', this.handleKeyDown);

      // 在全局对象上添加缓存检查方法，便于调试
      window.checkCDNCacheStatus = () => {
        this.showCacheViewer = true;
      };

      // 在控制台输出提示信息
      console.info(
        '%c[' + this.$t('system.name') + '] ' + this.$t('cache.cdnEnabled'),
        'color: #409EFF; font-weight: bold;'
      );
      console.info(
        '按下 Alt+C 组合键或在控制台运行 checkCDNCacheStatus() 可以查看CDN缓存状态'
      );

      // 检查Service Worker状态
      this.checkServiceWorkerStatus();
    } else {
      console.info(
        '%c[' + this.$t('system.name') + '] ' + this.$t('cache.cdnDisabled'),
        'color: #67C23A; font-weight: bold;'
      );
    }
  },
  beforeDestroy() {
    // 只有在启用CDN时才需要移除事件监听
    if (this.isCDNEnabled) {
      document.removeEventListener('keydown', this.handleKeyDown);
    }
  },
  methods: {
    handleKeyDown(e) {
      // Alt+C 快捷键
      if (e.altKey && e.key === 'c') {
        this.showCacheViewer = true;
      }
    },
    isMobileDevice() {
      // 检测是否为移动设备的函数
      return /Android|webOS|iPhone|iPad|iPod|BlackBerry|IEMobile|Opera Mini/i.test(navigator.userAgent);
    },
    
    async checkServiceWorkerStatus() {
      // 检查Service Worker是否已注册
      if ('serviceWorker' in navigator) {
        try {
          const registrations = await navigator.serviceWorker.getRegistrations();
          if (registrations.length > 0) {
            console.info(
              '%c[' + this.$t('system.name') + '] ' + this.$t('cache.serviceWorkerRegistered'),
              'color: #67C23A; font-weight: bold;'
            );

            // 输出缓存状态到控制台
            setTimeout(async () => {
              const hasCaches = await logCacheStatus();
              if (!hasCaches) {
                console.info(
                '%c[' + this.$t('system.name') + '] ' + this.$t('cache.noCacheDetected'),
                'color: #E6A23C; font-weight: bold;'
              );

              // 开发环境下提供额外提示
              if (process.env.NODE_ENV === 'development') {
                console.info(
                  '%c[' + this.$t('system.name') + '] ' + this.$t('cache.swDevEnvWarning'),
                  'color: #E6A23C; font-weight: bold;'
                );
                console.info(this.$t('cache.swCheckMethods'));
                console.info('1. ' + this.$t('cache.swCheckMethod1'));
                console.info('2. ' + this.$t('cache.swCheckMethod2'));
                console.info('3. ' + this.$t('cache.swCheckMethod3'));
              }
              }
            }, 2000);
          } else {
            console.info(
                  '%c[' + this.$t('system.name') + '] ' + this.$t('cache.serviceWorkerNotRegistered'),
                  'color: #F56C6C; font-weight: bold;'
                );

                if (process.env.NODE_ENV === 'development') {
                  console.info(
                    '%c[' + this.$t('system.name') + '] ' + this.$t('cache.swDevEnvNormal'),
                    'color: #E6A23C; font-weight: bold;'
                  );
                  console.info(this.$t('cache.swProdOnly'));
                  console.info(this.$t('cache.swTestingTitle'));
                  console.info('1. ' + this.$t('cache.swTestingStep1'));
                  console.info('2. ' + this.$t('cache.swTestingStep2'));
                }
          }
        } catch (error) {
          console.error('检查Service Worker状态失败:', error);
        }
      } else {
          console.warn(this.$t('cache.swNotSupported'));
        }
    }
  }
};
</script>