<template>
  <div class="welcome val-app-shell">
    <HeaderBar />

    <div class="operation-bar">
      <h2 class="page-title">{{ $t('serverSideManager.pageTitle') }}</h2>
    </div>

    <div class="main-wrapper">
      <div class="content-panel">
        <div class="content-area">
          <el-card class="params-card" shadow="never">
            <el-table ref="paramsTable" :data="paramsList" class="transparent-table" v-loading="loading"
              :element-loading-text="$t('serverSideManager.loading')" element-loading-spinner="el-icon-loading"
              element-loading-background="rgba(0, 0, 0, 0.7)" :header-cell-class-name="headerCellClassName">
              <el-table-column :label="$t('modelConfig.select')" align="center" width="120">
                <template slot-scope="scope">
                  <el-checkbox v-model="scope.row.selected"></el-checkbox>
                </template>
              </el-table-column>
              <el-table-column :label="$t('serverSideManager.wsAddress')" prop="address" align="center"></el-table-column>
              <el-table-column :label="$t('serverSideManager.operation')" prop="operator" align="center" show-overflow-tooltip>
                <template slot-scope="scope">
                  <el-button size="medium" type="text" @click="emitAction(scope.row, actionMap.restart)">{{ $t('serverSideManager.restart') }}</el-button>
                  <el-button size="medium" type="text"
                    @click="emitAction(scope.row, actionMap.update_config)">{{ $t('serverSideManager.updateConfig') }}</el-button>
                </template>
              </el-table-column>
            </el-table>
          </el-card>
        </div>
      </div>
    </div>


    <el-footer>
      <version-footer />
    </el-footer>
  </div>
</template>

<script>
import Api from "@/apis/api";
import HeaderBar from "@/components/HeaderBar.vue";
import ParamDialog from "@/components/ParamDialog.vue";
import VersionFooter from "@/components/VersionFooter.vue";
import i18n from '@/i18n';

export default {
  components: { HeaderBar, ParamDialog, VersionFooter },
  data() {
    return {
      paramsList: [],
      currentPage: 1,
      loading: false,
      pageSize: 10,
      pageSizeOptions: [10, 20, 50, 100],
      total: 0,
      dialogVisible: false,
      dialogTitle: '',
      isAllSelected: false,
      sensitive_keys: ["api_key", "personal_access_token", "access_token", "token", "secret", "access_key_secret", "secret_key"],
      paramForm: {
        id: null,
        paramCode: "",
        paramValue: "",
        remark: ""
      },
    };
  },
  created() {
    this.fetchParams();
  },
  mounted() {
    this.dialogTitle = this.$t('paramManagement.addParam');
  },

  computed: {
    pageCount() {
      return Math.ceil(this.total / this.pageSize);
    },
    visiblePages() {
      const pages = [];
      const maxVisible = 3;
      let start = Math.max(1, this.currentPage - 1);
      let end = Math.min(this.pageCount, start + maxVisible - 1);

      if (end - start + 1 < maxVisible) {
        start = Math.max(1, end - maxVisible + 1);
      }

      for (let i = start; i <= end; i++) {
        pages.push(i);
      }
      return pages;
    },
    actionMap() {
      return {
        restart: {
          value: 'restart',
          title: this.$t('serverSideManager.restartServer'),
          message: this.$t('serverSideManager.confirmRestart'),
          confirmText: this.$t('serverSideManager.restart'),
        },
        update_config: {
          value: 'update_config',
          title: this.$t('serverSideManager.updateConfigTitle'),
          message: this.$t('serverSideManager.confirmUpdateConfig'),
          confirmText: this.$t('serverSideManager.updateConfig'),
        }
      };
    }
  },
  methods: {
    handlePageSizeChange(val) {
      this.pageSize = val;
      this.currentPage = 1;
      this.fetchParams();
    },
    fetchParams() {
      this.loading = true;
      Api.admin.getWsServerList(
        {},
        ({ data }) => {
          this.loading = false;
          if (data.code === 0) {
            this.paramsList = data.data.map(item => ({ address: item }));
            this.total = data.data.length;
          } else {
            this.$message.error({
              message: data.msg || this.$t('serverSideManager.getServerListFailed'),
              showClose: true
            });
          }
        }
      );
    },
    emitAction(rowItem, actionItem) {
      if (actionItem === undefined || rowItem.address === undefined) {
        return;
      }
      // 弹开询问框
      this.$confirm(actionItem.message, actionItem.title, {
        confirmButtonText: actionItem.confirmText, // 确认按钮文本
        cancelButtonText: this.$t('common.cancel') // 取消按钮文本
      }).then(() => {
        // 用户点击了确认按钮
        Api.admin.sendWsServerAction({
          targetWs: rowItem.address,
          action: actionItem.value
        }, ({ data }) => {
          if (data.code !== 0) {
            this.$message.error({
              message: data.msg || this.$t('serverSideManager.operationFailed'),
              showClose: true
            });
            return;
          }
          this.$message.success({
            message: actionItem.value === 'restart' ? this.$t('serverSideManager.restartSuccess') : this.$t('serverSideManager.updateConfigSuccess'),
            showClose: true
          })
        })
      })
    },
    headerCellClassName({ columnIndex }) {
      if (columnIndex === 0) {
        return "custom-selection-header";
      }
      return "";
    }
  },
};
</script>

<style lang="scss" scoped>
.welcome {
  min-width: 900px;
  min-height: 506px;
  height: 100vh;
  display: flex;
  position: relative;
  flex-direction: column;
  overflow: hidden;
}

.main-wrapper {
  // 顶部 63px 底部 35px 查询58px
  height: calc(100vh - 63px - 35px - 58px);
  margin: 0 22px;
  border-radius: var(--val-radius-lg, 20px);
  box-shadow: var(--val-shadow);
  position: relative;
  background: var(--val-bg-card, rgba(22, 30, 52, 0.72));
  border: 1px solid var(--val-border, rgba(0, 0, 0, 0.1));
  backdrop-filter: blur(8px);
  display: flex;
  flex-direction: column;
}

.operation-bar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16px 24px;
}

.page-title {
  font-size: 24px;
  margin: 0;
  color: var(--val-text);
  font-weight: 700;
  text-shadow: 0 0 20px rgba(124, 92, 255, 0.15);
}

.right-operations {
  display: flex;
  gap: 10px;
  margin-left: auto;
}

.search-input {
  width: 240px;
}

.btn-search {
  background: linear-gradient(135deg, var(--val-primary), var(--val-primary-dark)) !important;
  border: none !important;
  color: var(--val-text) !important;
  border-radius: var(--val-radius-sm, 10px);
  transition: var(--val-transition);
}

.content-panel {
  flex: 1;
  display: flex;
  overflow: hidden;
  height: 100%;
  border-radius: var(--val-radius-lg, 20px);
  background: transparent;
  border: none;
}

.content-area {
  flex: 1;
  height: 100%;
  min-width: 600px;
  overflow: auto;
  background-color: transparent !important;
  display: flex;
  flex-direction: column;
}

.params-card {
  background: transparent !important;
  flex: 1;
  display: flex;
  flex-direction: column;
  border: none;
  box-shadow: none;
  overflow: hidden;

  ::v-deep .el-card__body {
    padding: 15px;
    display: flex;
    flex-direction: column;
    flex: 1;
    overflow: hidden;
  }
}

.table_bottom {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: 10px;
  padding-bottom: 10px;
}

.ctrl_btn {
  display: flex;
  gap: 8px;
  padding-left: 26px;

  .el-button {
    min-width: 72px;
    height: 32px;
    padding: 7px 12px 7px 10px;
    font-size: 12px;
    border-radius: var(--val-radius-sm, 10px);
    line-height: 1;
    font-weight: 500;
    border: none;
    transition: all 0.3s ease;
    box-shadow: 0 2px 6px rgba(0, 0, 0, 0.1);

    &:hover {
      transform: translateY(-1px);
      box-shadow: 0 4px 8px rgba(0, 0, 0, 0.15);
    }
  }

  .el-button--primary {
    background: var(--val-primary) !important;
    color: var(--val-text) ;
  }

  .el-button--danger {
    background: var(--val-danger) !important;
    color: var(--val-text) ;
  }
}

::v-deep .transparent-table {
  background: transparent !important;
  flex: 1;
  width: 100%;
  display: flex;
  flex-direction: column;

  .el-table__body-wrapper {
    flex: 1;
    overflow-y: auto;
    max-height: none !important;
  }

  .el-table__header-wrapper {
    flex-shrink: 0;
  }

  .el-table__header th {
    background: rgba(0, 0, 0, 0.02) !important;
    color: var(--val-text) !important;
    font-weight: 600;
    height: 40px;
    padding: 8px 0;
    font-size: 14px;
    border-bottom: 1px solid var(--val-border) !important;
  }

  .el-table__body tr {
    background-color: transparent !important;

    td {
      border-top: 1px solid rgba(0, 0, 0, 0.03) !important;
      border-bottom: 1px solid rgba(0, 0, 0, 0.03) !important;
      padding: 8px 0;
      height: 40px;
      color: var(--val-text-muted) !important;
      font-size: 14px;
      background-color: transparent !important;
    }
  }

  .el-table__row:hover>td {
    background-color: rgba(0, 0, 0, 0.04) !important;
    color: var(--val-text) !important;
  }

  &::before {
    display: none;
  }
}


::v-deep .el-checkbox__inner {
  background-color: rgba(0, 0, 0, 0.04) !important;
  border-color: var(--val-border) !important;
  transition: all 0.2s ease-in-out;
}

::v-deep .el-checkbox__inner:hover {
  border-color: var(--val-primary) !important;
}

::v-deep .el-checkbox__input.is-checked .el-checkbox__inner {
  background-color: var(--val-primary) !important;
  border-color: var(--val-primary) !important;
}

@media (min-width: 1144px) {
  .table_bottom {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-top: 40px;
  }

  ::v-deep .transparent-table {
    .el-table__body tr {
      td {
        padding-top: 16px;
        padding-bottom: 16px;
      }

      &+tr {
        margin-top: 10px;
      }
    }
  }
}

::v-deep .el-table .el-button--text {
  color: var(--val-primary-hover) !important;
  font-weight: 600;
  transition: all 0.3s ease;
}

::v-deep .el-table .el-button--text:hover {
  color: var(--val-primary) !important;
  transform: translateY(-1px);
}

::v-deep .el-table .el-button--text:last-child {
  color: var(--val-danger) !important;
}

::v-deep .el-table .el-button--text:last-child:hover {
  color: #ff8fa3 !important;
}

.el-button--success {
  background: var(--val-success) !important;
  color: var(--val-text) ;
}

::v-deep .el-table .cell {
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.page-size-select {
  width: 100px;
  margin-right: 10px;

  ::v-deep .el-input__inner {
    height: 32px;
    line-height: 32px;
    border-radius: var(--val-radius-sm, 10px);
    border: 1px solid var(--val-border) !important;
    background: rgba(0, 0, 0, 0.04) !important;
    color: var(--val-text) !important;
    font-size: 14px;
  }

  ::v-deep .el-input__suffix {
    right: 6px;
    width: 15px;
    height: 20px;
    display: flex;
    justify-content: center;
    align-items: center;
    top: 6px;
    border-radius: 4px;
  }

  ::v-deep .el-input__suffix-inner {
    display: flex;
    align-items: center;
    justify-content: center;
    width: 100%;
  }

  ::v-deep .el-icon-arrow-up:before {
    content: "";
    display: inline-block;
    border-left: 6px solid transparent;
    border-right: 6px solid transparent;
    border-top: 9px solid var(--val-text-muted);
    position: relative;
    transform: rotate(0deg);
    transition: transform 0.3s;
  }
}

::v-deep .el-table {
  .el-table__body-wrapper {
    transition: height 0.3s ease;
  }
}

.el-table {
  --table-max-height: calc(100vh - 40vh);
  max-height: var(--table-max-height);

  .el-table__body-wrapper {
    max-height: calc(var(--table-max-height) - 40px);
    overflow-y: auto;
  }
}

::v-deep .el-loading-mask {
  background-color: rgba(11, 15, 26, 0.7) !important;
  backdrop-filter: blur(2px);
}

::v-deep .el-loading-spinner .circular {
  width: 28px;
  height: 28px;
}

::v-deep .el-loading-spinner .path {
  stroke: var(--val-primary);
}

::v-deep .el-loading-text {
  color: var(--val-primary) !important;
  font-size: 14px;
  margin-top: 8px;
}
</style>
