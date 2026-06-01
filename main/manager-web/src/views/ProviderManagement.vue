<template>
  <div class="welcome val-app-shell">
    <HeaderBar />

    <div class="operation-bar">
      <h2 class="page-title">{{ $t('header.providerManagement') }}</h2>
      <div class="right-operations">
        <el-dropdown trigger="click" @command="handleSelectModelType" @visible-change="handleDropdownVisibleChange">
          <el-button class="category-btn">
            {{ $t('providerManagement.categoryFilter') }} {{ selectedModelTypeLabel }}<i class="el-icon-arrow-down el-icon--right"
              :class="{ 'rotate-down': DropdownVisible }"></i>
          </el-button>
          <el-dropdown-menu slot="dropdown">
            <el-dropdown-item command="">{{ $t('common.all') }}</el-dropdown-item>
            <el-dropdown-item v-for="item in translatedModelTypes" :key="item.value" :command="item.value">
              {{ item.label }}
            </el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>
        <el-input :placeholder="$t('providerManagement.searchPlaceholder')" v-model="searchName" class="search-input" @keyup.enter.native="handleSearch"
          clearable />
        <el-button class="btn-search" @click="handleSearch">{{ $t('common.search') }}</el-button>
      </div>
    </div>

    <div class="main-wrapper">
      <div class="content-panel">
        <div class="content-area">
          <el-card class="provider-card" shadow="never">
            <el-table ref="providersTable" :data="filteredProvidersList" class="transparent-table" v-loading="loading"
              :element-loading-text="$t('common.loading')" element-loading-spinner="el-icon-loading"
              element-loading-background="rgba(0, 0, 0, 0.7)" :header-cell-class-name="headerCellClassName">
              <el-table-column :label="$t('modelConfig.select')" align="center" width="120">
                <template slot-scope="scope">
                  <el-checkbox v-model="scope.row.selected"></el-checkbox>
                </template>
              </el-table-column>

              <el-table-column :label="$t('providerManagement.category')" prop="modelType" align="center" width="200">
                <template slot="header" slot-scope="scope">
                  <el-dropdown trigger="click" @command="handleSelectModelType"
                    @visible-change="isDropdownOpen = $event">
                    <span class="dropdown-trigger" :class="{ 'active': isDropdownOpen }">
                      {{ $t('providerManagement.category') }}{{ selectedModelTypeLabel }} <i class="dropdown-arrow"
                        :class="{ 'is-active': isDropdownOpen }"></i>
                    </span>
                    <el-dropdown-menu slot="dropdown">
                      <el-dropdown-item command="">{{ $t('common.all') }}</el-dropdown-item>
                      <el-dropdown-item v-for="item in translatedModelTypes" :key="item.value" :command="item.value">
                        {{ item.label }}
                      </el-dropdown-item>
                    </el-dropdown-menu>
                  </el-dropdown>
                </template>
                <template slot-scope="scope">
                  <el-tag :type="getModelTypeTag(scope.row.modelType)">
                    {{ getModelTypeLabel(scope.row.modelType) }}
                  </el-tag>
                </template>
              </el-table-column>
              <el-table-column :label="$t('providerManagement.providerCode')" prop="providerCode" align="center" width="150"></el-table-column>
              <el-table-column :label="$t('common.name')" prop="name" align="center"></el-table-column>
              <el-table-column :label="$t('providerManagement.fieldConfig')" align="center">
                <template slot-scope="scope">
                  <el-popover placement="top-start" width="400" trigger="hover">
                    <div v-for="field in scope.row.fields" :key="field.key" class="field-item">
                      <span class="field-label">{{ field.label }}:</span>
                      <span class="field-type">{{ field.type }}</span>
                      <span v-if="isSensitiveField(field.key)" class="sensitive-tag">{{ $t('common.sensitive') }}</span>
                    </div>
                    <el-button slot="reference" size="mini" type="text">{{ $t('providerManagement.viewFields') }}</el-button>
                  </el-popover>
                </template>
              </el-table-column>
              <el-table-column :label="$t('common.sort')" prop="sort" align="center" width="80"></el-table-column>
              <el-table-column :label="$t('common.action')" align="center" width="180">
                <template slot-scope="scope">
                  <el-button size="mini" type="text" @click="editProvider(scope.row)">{{ $t('common.edit') }}</el-button>
                  <el-button size="mini" type="text" @click="deleteProvider(scope.row)">{{ $t('common.delete') }}</el-button>
                </template>
              </el-table-column>
            </el-table>

            <div class="table_bottom">
              <div class="ctrl_btn">
                <el-button size="mini" type="primary" class="select-all-btn" @click="handleSelectAll">
                  {{ isAllSelected ? $t('common.deselectAll') : $t('common.selectAll') }}
                </el-button>
                <el-button size="mini" type="success" @click="showAddDialog">{{ $t('common.add') }}</el-button>
                <el-button size="mini" type="danger" icon="el-icon-delete" @click="deleteSelectedProviders">{{ $t('common.delete') }}
                </el-button>
              </div>
              <div class="custom-pagination">
                <el-select v-model="pageSize" @change="handlePageSizeChange" class="page-size-select">
                  <el-option v-for="item in pageSizeOptions" :key="item" :label="$t('common.perPage', { number: item })" :value="item">
                  </el-option>
                </el-select>
                <button class="pagination-btn" :disabled="currentPage === 1" @click="goFirst">
                  {{ $t('common.firstPage') }}
                </button>
                <button class="pagination-btn" :disabled="currentPage === 1" @click="goPrev">
                  {{ $t('common.prevPage') }}
                </button>
                <button v-for="page in visiblePages" :key="page" class="pagination-btn"
                  :class="{ active: page === currentPage }" @click="goToPage(page)">
                  {{ page }}
                </button>
                <button class="pagination-btn" :disabled="currentPage === pageCount" @click="goNext">
                  {{ $t('common.nextPage') }}
                </button>
                <span class="total-text">{{ $t('common.totalRecords', { number: total }) }}</span>
              </div>
            </div>
          </el-card>
        </div>
      </div>
    </div>

    <!-- 新增/编辑供应器对话框 -->
    <provider-dialog :title="dialogTitle" :visible.sync="dialogVisible" :form="providerForm" :model-types="modelTypes"
      @submit="handleSubmit" @cancel="dialogVisible = false" />

    <el-footer>
      <version-footer />
    </el-footer>
  </div>
</template>

<script>
import Api from "@/apis/api";
import HeaderBar from "@/components/HeaderBar.vue";
import ProviderDialog from "@/components/ProviderDialog.vue";
import VersionFooter from "@/components/VersionFooter.vue";

export default {
  components: { HeaderBar, ProviderDialog, VersionFooter },
  data() {
    return {
      searchName: "",
      searchModelType: "",
      providersList: [],
      modelTypes: [
        { value: "ASR", labelKey: 'providerManagement.modelType.ASR' },
        { value: "TTS", labelKey: 'providerManagement.modelType.TTS' },
        { value: "LLM", labelKey: 'providerManagement.modelType.LLM' },
        { value: "VLLM", labelKey: 'providerManagement.modelType.VLLM' },
        { value: "Intent", labelKey: 'providerManagement.modelType.Intent' },
        { value: "Memory", labelKey: 'providerManagement.modelType.Memory' },
        { value: "VAD", labelKey: 'providerManagement.modelType.VAD' },
        { value: "Plugin", labelKey: 'providerManagement.modelType.Plugin' },
        { value: "RAG", labelKey: 'providerManagement.modelType.RAG' }
      ],
      currentPage: 1,
      loading: false,
      pageSize: 10,
      pageSizeOptions: [10, 20, 50, 100],
      total: 0,
      dialogVisible: false,
      dialogTitle: "新增供应器",
      isAllSelected: false,
      isDropdownOpen: false,
      sensitive_keys: ["api_key", "personal_access_token", "access_token", "token", "secret", "access_key_secret", "secret_key"],
      providerForm: {
        id: null,
        modelType: "",
        providerCode: "",
        name: "",
        fields: [],
        sort: 0
      },
      DropdownVisible: false,
    };
  },
  created() {
    this.fetchProviders();
  },
  computed: {
    translatedModelTypes() {
      return this.modelTypes.map(type => ({
        value: type.value,
        label: this.$t(type.labelKey)
      }));
    },
    selectedModelTypeLabel() {
      if (!this.searchModelType) return `（${this.$t('providerManagement.all')}）`;
      const selectedType = this.modelTypes.find(item => item.value === this.searchModelType);
      return selectedType ? `（${this.$t(selectedType.labelKey)}）` : "";
    },
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
    filteredProvidersList() {
      return this.providersList;

      // let list = this.providersList.filter(item => {
      //   const nameMatch = item.name.toLowerCase().includes(this.searchName.toLowerCase());
      //   const typeMatch = !this.searchModelType || item.model_type === this.searchModelType;
      //   return nameMatch && typeMatch;
      // });

      // list.sort((a, b) => a.sort - b.sort);

      // // 分页处理
      // const start = (this.currentPage - 1) * this.pageSize;
      // return list.slice(start, start + this.pageSize);
    }
  },
  methods: {
    fetchProviders() {
      this.loading = true;

      Api.model.getModelProvidersPage(
        {
          page: this.currentPage,
          limit: this.pageSize,
          name: this.searchName,
          modelType: this.searchModelType
        },
        ({ data }) => {
          this.loading = false;
          if (data.code === 0) {
            this.providersList = data.data.list.map(item => {
              return {
                ...item,
                selected: false,
                fields: JSON.parse(item.fields)
              };
            });
            this.total = data.data.total;
          } else {
            this.$message.error({
              message: data.msg || '获取参数列表失败'
            });
          }
        }
      );
    },
    handleSearch() {
      this.currentPage = 1;
      this.fetchProviders();
    },
    handleSelectModelType(value) {
      this.isDropdownOpen = false;
      this.searchModelType = value;
      this.handleSearch();
    },
    handleSelectAll() {
      this.isAllSelected = !this.isAllSelected;
      this.providersList.forEach(row => {
        row.selected = this.isAllSelected;
      });
    },
    showAddDialog() {
      this.dialogTitle = this.$t('common.addProvider');
      this.providerForm = {
        id: null,
        modelType: "",
        providerCode: "",
        name: "",
        fields: [],
        sort: 0
      };
      this.dialogVisible = true;
    },
    editProvider(row) {
      this.dialogTitle = this.$t('common.editProvider');
      this.providerForm = {
        ...row,
        fields: JSON.parse(JSON.stringify(row.fields))
      };
      this.dialogVisible = true;
    },
    handleSubmit({ form, done }) {
      this.loading = true;
      if (form.id) {
        // 编辑
        Api.model.updateModelProvider(form, ({ data }) => {

          if (data.code === 0) {
            this.fetchProviders(); // 刷新表格
            this.$message.success({
            message: this.$t('common.updateSuccess'),
            showClose: true
          });
          }
        });
      } else {
        // 新增
        Api.model.addModelProvider(form, ({ data }) => {
          if (data.code === 0) {
            this.fetchProviders(); // 刷新表格
            this.$message.success({
            message: this.$t('common.addSuccess'),
            showClose: true
          });
            this.total += 1;
          }
        });
      }
      this.loading = false;
      this.dialogVisible = false;
      done && done();
    },
    deleteSelectedProviders() {
      const selectedRows = this.providersList.filter(row => row.selected);
      if (selectedRows.length === 0) {
        this.$message.warning({
          message: this.$t('providerManagement.selectToDelete'),
          showClose: true
        });
        return;
      }
      this.deleteProvider(selectedRows);
    },
    deleteProvider(row) {
      const providers = Array.isArray(row) ? row : [row];
      const providerCount = providers.length;

      this.$confirm(this.$t('providerManagement.confirmDelete', { count: providerCount }), this.$t('common.warning'), {
        confirmButtonText: this.$t('common.confirm'),
        cancelButtonText: this.$t('common.cancel'),
        type: 'warning',
      }).then(() => {
        const ids = providers.map(provider => provider.id);
        Api.model.deleteModelProviderByIds(ids, ({ data }) => {
          if (data.code === 0) {

            this.isAllSelected = false;
            this.fetchProviders(); // 刷新表格

            this.$message.success({
              message: this.$t('common.deleteSuccess'),
              showClose: true
            });
          } else {
            this.$message.error({
              message: data.msg || this.$t('common.deleteFailure'),
              showClose: true
            });
          }
        });
      }).catch(() => {
        this.$message({
          type: 'info',
          message: this.$t('common.deleteCancelled'),
          showClose: true,
          duration: 1000
        });
      });
    },
    getModelTypeTag(type) {
      const typeMap = {
        'ASR': 'success',
        'TTS': 'warning',
        'LLM': 'danger',
        'Intent': 'info',
        'Memory': '',
        'VAD': 'primary',
        'RAG': 'warning'
      };
      return typeMap[type] || '';
    },
    getModelTypeLabel(type) {
      const typeItem = this.modelTypes.find(item => item.value === type);
      return typeItem ? this.$t(typeItem.labelKey) : type;
    },
    isSensitiveField(fieldKey) {
      if (typeof fieldKey !== 'string') return false;
      return this.sensitive_keys.some(key =>
        fieldKey.toLowerCase().includes(key.toLowerCase())
      );
    },
    handlePageSizeChange(val) {
      this.pageSize = val;
      this.currentPage = 1;
      this.fetchProviders();
    },
    headerCellClassName({ columnIndex }) {
      if (columnIndex === 0) {
        return "custom-selection-header";
      }
      return "";
    },
    selectionCellClassName() {
      return "custom-selection-cell";
    },
    updateSelectionHeaderText() {
      // 确保表格已渲染
      this.$nextTick(() => {
        if (this.$refs.providersTable && this.$refs.providersTable.$el) {
          const headerCheckbox = this.$refs.providersTable.$el.querySelector('.custom-selection-header .el-checkbox .el-checkbox__label');
          if (headerCheckbox) {
            headerCheckbox.textContent = this.$t('modelConfig.select');
          }
        }
      });
    },
    mounted() {
      this.updateSelectionHeaderText();
    },
    updated() {
      this.updateSelectionHeaderText();
    },
    goFirst() {
      this.currentPage = 1;
      this.fetchProviders();
    },
    goPrev() {
      if (this.currentPage > 1) {
        this.currentPage--;
        this.fetchProviders();
      }
    },
    goNext() {
      if (this.currentPage < this.pageCount) {
        console.log("this.currentPage", this.currentPage);
        this.currentPage++;
        this.fetchProviders();
      }
    },
    goToPage(page) {
      this.currentPage = page;
      this.fetchProviders();
    },
    handleDropdownVisibleChange(visible) {
      this.DropdownVisible = visible;
    },
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
  // 顶部 63px 底部 35px 查询72px
  height: calc(100vh - 63px - 35px - 72px);
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

.btn-search:hover {
  opacity: 0.95;
  transform: translateY(-1px);
  box-shadow: 0 4px 12px rgba(124, 92, 255, 0.3) !important;
}

::v-deep .search-input .el-input__inner {
  border-radius: var(--val-radius-sm, 10px);
  border: 1px solid var(--val-border) !important;
  background-color: rgba(0, 0, 0, 0.04) !important;
  color: var(--val-text) !important;
  transition: var(--val-transition);
}

::v-deep .search-input .el-input__inner:focus {
  border-color: var(--val-primary) !important;
  box-shadow: 0 0 0 2px rgba(124, 92, 255, 0.2) !important;
  background-color: rgba(0, 0, 0, 0.06) !important;
  outline: none;
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

.el-card {
  border: none;
}

.provider-card {
  background: transparent !important;
  flex: 1;
  display: flex;
  flex-direction: column;
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
  background: transparent !important;
  border-top: 1px solid var(--val-border);
  padding-top: 12px;
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

  .el-button--success {
    background: var(--val-success) !important;
    color: var(--val-text) ;
  }

  .el-button--danger {
    background: var(--val-danger) !important;
    color: var(--val-text) ;
  }
}

.custom-pagination {
  display: flex;
  align-items: center;
  gap: 10px;

  .el-select {
    margin-right: 8px;
  }

  .pagination-btn:first-child,
  .pagination-btn:nth-child(2),
  .pagination-btn:nth-last-child(2),
  .pagination-btn:nth-child(3) {
    min-width: 60px;
    height: 32px;
    padding: 0 12px;
    border-radius: var(--val-radius-sm, 10px);
    border: 1px solid var(--val-border) !important;
    background: rgba(0, 0, 0, 0.04) !important;
    color: var(--val-text-muted) !important;
    font-size: 14px;
    cursor: pointer;
    transition: all 0.3s ease;

    &:hover:not(:disabled) {
      background: rgba(0, 0, 0, 0.08) !important;
      color: var(--val-text) !important;
    }

    &:disabled {
      opacity: 0.4;
      cursor: not-allowed;
    }
  }

  .pagination-btn:not(:first-child):not(:nth-child(3)):not(:nth-child(2)):not(:nth-last-child(2)) {
    min-width: 28px;
    height: 32px;
    padding: 0;
    border-radius: var(--val-radius-sm, 10px);
    border: 1px solid transparent;
    background: transparent;
    color: var(--val-text-muted) !important;
    font-size: 14px;
    cursor: pointer;
    transition: all 0.3s ease;

    &:hover {
      background: rgba(0, 0, 0, 0.06) !important;
      color: var(--val-text) !important;
    }
  }

  .pagination-btn.active {
    background: var(--val-primary) !important;
    color: var(--val-text) !important;
    border-color: var(--val-primary) !important;

    &:hover {
      background: var(--val-primary-hover) !important;
    }
  }

  .total-text {
    color: var(--val-text-dim);
    font-size: 14px;
    margin-left: 10px;
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
}

::v-deep .el-table .el-button--text:hover {
  color: var(--val-primary) !important;
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
  max-height: var(--table-max-height);

  .el-table__body-wrapper {
    max-height: calc(var(--table-max-height) - 40px);
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

.field-item {
  padding: 5px 0;
  border-bottom: 1px solid var(--val-border);
  display: flex;
  align-items: center;

  .field-label {
    flex: 1;
    font-weight: bold;
    color: var(--val-text);
  }

  .field-type {
    width: 80px;
    color: var(--val-text-muted);
  }

  .sensitive-tag {
    margin-left: 10px;
    color: var(--val-danger);
    font-size: 12px;
  }
}

.dropdown-trigger {
  font-size: 14px;
  color: var(--val-text);
  cursor: pointer;
  display: flex;
  align-items: center;

  &:hover {
    color: var(--val-primary);
  }
}

.dropdown-trigger.active {
  color: var(--val-primary);
}

.dropdown-arrow {
  display: inline-block;
  margin-left: 5px;
  width: 0;
  height: 0;
  border-left: 5px solid transparent;
  border-right: 5px solid transparent;
  border-top: 7px solid var(--val-text-muted);
  position: relative;
  transition: transform 0.3s ease;
  transform: rotate(0deg);

  &.is-active {
    transform: rotate(180deg);
    border-top-color: var(--val-primary);
  }
}

.rotate-down {
  transform: rotate(180deg);
  transition: transform 0.3s ease;
}

.el-icon-arrow-down {
  transition: transform 0.3s ease;
}

/* 确保选择列标题样式正确 */
.custom-selection-header {
  position: relative;
}

::v-deep .custom-selection-header .el-checkbox {
  display: flex !important;
  align-items: center;
  justify-content: center;
}

::v-deep .custom-selection-header .el-checkbox__label {
  position: relative;
  white-space: nowrap;
}

::v-deep .custom-selection-cell {
  display: flex;
  align-items: center;
  justify-content: center;
}
</style>