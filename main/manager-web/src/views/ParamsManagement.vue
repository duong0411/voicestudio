<template>
    <div class="welcome val-app-shell">
        <HeaderBar />

        <div class="operation-bar">
            <h2 class="page-title">{{ $t('paramManagement.pageTitle') }}</h2>
            <div class="right-operations">
                <el-input :placeholder="$t('paramManagement.searchPlaceholder')" v-model="searchCode"
                    class="search-input" @keyup.enter.native="handleSearch" clearable />
                <el-button class="btn-search" @click="handleSearch">{{ $t('paramManagement.search') }}</el-button>
            </div>
        </div>

        <div class="main-wrapper">
            <div class="content-panel">
                <div class="content-area">
                    <el-card class="params-card" shadow="never">
                        <el-table ref="paramsTable" :data="paramsList" class="transparent-table" v-loading="loading"
                            element-loading-text="Loading" element-loading-spinner="el-icon-loading"
                            element-loading-background="rgba(0, 0, 0, 0.7)"
                            :header-cell-class-name="headerCellClassName">
                            <el-table-column :label="$t('modelConfig.select')" align="center" width="120">
                                <template slot-scope="scope">
                                    <el-checkbox v-model="scope.row.selected"></el-checkbox>
                                </template>
                            </el-table-column>
                            <el-table-column :label="$t('paramManagement.paramCode')" prop="paramCode"
                                align="center"></el-table-column>
                            <el-table-column :label="$t('paramManagement.paramValue')" prop="paramValue" align="center"
                                show-overflow-tooltip>
                                <template slot-scope="scope">
                                    <div v-if="isSensitiveParam(scope.row.paramCode)">
                                        <span v-if="!scope.row.showValue">
                                            {{ maskSensitiveValue(scope.row.paramValue) }}
                                        </span>
                                        <span v-else>{{ scope.row.paramValue }}</span>
                                        <el-button size="mini" type="text" @click="toggleSensitiveValue(scope.row)">
                                            {{ scope.row.showValue ? $t('paramManagement.hide') :
                                                $t('paramManagement.view') }}
                                        </el-button>
                                    </div>
                                    <span v-else>{{ scope.row.paramValue }}</span>
                                </template>
                            </el-table-column>
                            <el-table-column :label="$t('paramManagement.remark')" prop="remark"
                                align="center"></el-table-column>
                            <el-table-column :label="$t('paramManagement.operation')" align="center">
                                <template slot-scope="scope">
                                    <el-button size="mini" type="text" @click="editParam(scope.row)">{{
                                        $t('paramManagement.edit') }}</el-button>
                                    <el-button size="mini" type="text" @click="deleteParam(scope.row)">{{
                                        $t('paramManagement.delete') }}</el-button>
                                </template>
                            </el-table-column>
                        </el-table>

                        <div class="table_bottom">
                            <div class="ctrl_btn">
                                <el-button size="mini" type="primary" class="select-all-btn" @click="handleSelectAll">
                                    {{ isAllSelected ? $t('paramManagement.deselectAll') :
                                        $t('paramManagement.selectAll') }}
                                </el-button>
                                <el-button size="mini" type="success" @click="showAddDialog">{{
                                    $t('paramManagement.add') }}</el-button>
                                <el-button size="mini" type="danger" icon="el-icon-delete"
                                    @click="deleteSelectedParams">{{
                                        $t('paramManagement.delete') }}</el-button>
                            </div>
                            <div class="custom-pagination">
                                <el-select v-model="pageSize" @change="handlePageSizeChange" class="page-size-select">
                                    <el-option v-for="item in pageSizeOptions" :key="item"
                                        :label="`${item}${$t('paramManagement.itemsPerPage')}`" :value="item">
                                    </el-option>
                                </el-select>
                                <button class="pagination-btn" :disabled="currentPage === 1" @click="goFirst">
                                    {{ $t('paramManagement.firstPage') }}
                                </button>
                                <button class="pagination-btn" :disabled="currentPage === 1" @click="goPrev">
                                    {{ $t('paramManagement.prevPage') }}
                                </button>
                                <button v-for="page in visiblePages" :key="page" class="pagination-btn"
                                    :class="{ active: page === currentPage }" @click="goToPage(page)">
                                    {{ page }}
                                </button>
                                <button class="pagination-btn" :disabled="currentPage === pageCount" @click="goNext">
                                    {{ $t('paramManagement.nextPage') }}
                                </button>
                                <span class="total-text">{{ $t('paramManagement.totalRecords', { total }) }}</span>
                            </div>
                        </div>
                    </el-card>
                </div>
            </div>
        </div>

        <!-- 新增/编辑参数对话框 -->
        <param-dialog ref="paramDialog" :title="dialogTitle" :visible.sync="dialogVisible" :form="paramForm"
            @submit="handleSubmit" @cancel="dialogVisible = false" />
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
export default {
    components: { HeaderBar, ParamDialog, VersionFooter },
    data() {
        return {
            searchCode: "",
            paramsList: [],
            currentPage: 1,
            loading: false,
            pageSize: 10,
            pageSizeOptions: [10, 20, 50, 100],
            total: 0,
            dialogVisible: false,
            dialogTitle: "新增参数",
            isAllSelected: false,
            sensitive_keys: ["api_key", "personal_access_token", "access_token", "token", "secret", "access_key_secret", "secret_key", "password", "mqtt_signature_key", "private_key"],
            paramForm: {
                id: null,
                paramCode: "",
                paramValue: "",
                valueType: "string",
                remark: ""
            },
        };
    },
    created() {
        this.fetchParams();

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
    },
    methods: {
        handlePageSizeChange(val) {
            this.pageSize = val;
            this.currentPage = 1;
            this.fetchParams();
        },
        fetchParams() {
            this.loading = true;
            Api.admin.getParamsList(
                {
                    page: this.currentPage,
                    limit: this.pageSize,
                    paramCode: this.searchCode,
                },
                ({ data }) => {
                    this.loading = false;
                    if (data.code === 0) {
                        this.paramsList = data.data.list.map(item => ({
                            ...item,
                            valueType: item.valueType || "string",
                            selected: false,
                            showValue: false
                        }));
                        this.total = data.data.total;
                    } else {
                        this.$message.error({
                            message: data.msg || this.$t('paramManagement.getParamsListFailed'),
                            showClose: true
                        });
                    }
                }
            );
        },
        handleSearch() {
            this.currentPage = 1;
            this.fetchParams();
        },
        handleSelectAll() {
            this.isAllSelected = !this.isAllSelected;
            this.paramsList.forEach(row => {
                row.selected = this.isAllSelected;
            });
        },
        showAddDialog() {
            this.dialogTitle = this.$t('paramManagement.addParam');
            this.paramForm = {
                id: null,
                paramCode: "",
                paramValue: "",
                valueType: "string", // 默认值
                remark: ""
            };
            this.dialogVisible = true;
        },
        editParam(row) {
            this.dialogTitle = this.$t('paramManagement.editParam');
            this.paramForm = {
                id: row.id,
                paramCode: row.paramCode,
                paramValue: row.paramValue,
                valueType: row.valueType || "string", // 确保有值
                remark: row.remark
            };
            this.dialogVisible = true;
        },
        handleSubmit(form) {
            if (form.id) {
                // 更新参数
                Api.admin.updateParam(form, ({ data }) => {
                    this.dialogVisible = false;
                    this.fetchParams();
                    this.$message.success({
                        message: this.$t('paramManagement.updateSuccess'),
                        showClose: true
                    });
                }, ({ data }) => {
                    this.$message.error({
                        message: data.msg || this.$t('paramManagement.updateFailed'),
                        showClose: true
                    });
                    // 调用ParamDialog的resetSaving方法重置保存状态
                    if (this.$refs.paramDialog && typeof this.$refs.paramDialog.resetSaving === 'function') {
                        this.$refs.paramDialog.resetSaving();
                    }
                });
            } else {
                // 新增参数
                Api.admin.addParam(form, ({ data }) => {
                    if (data.code === 0) {
                        this.dialogVisible = false;
                        this.fetchParams();
                        this.$message.success({
                            message: this.$t('paramManagement.addSuccess'),
                            showClose: true
                        });
                    } else {
                        this.$message.error({
                            message: data.msg || this.$t('paramManagement.addFailed'),
                            showClose: true
                        });
                        // 调用ParamDialog的resetSaving方法重置保存状态
                        if (this.$refs.paramDialog && typeof this.$refs.paramDialog.resetSaving === 'function') {
                            this.$refs.paramDialog.resetSaving();
                        }
                    }
                });
            }
        },
        deleteSelectedParams() {
            const selectedParams = this.paramsList.filter(row => row.selected);
            if (selectedParams.length === 0) {
                this.$message.warning({
                    message: this.$t('paramManagement.selectParamsFirst'),
                    showClose: true
                });
                return;
            }
            this.deleteParams(selectedParams);
        },
        deleteParam(row) {
            if (!row.id) {
                this.$message.warning({
                    message: this.$t('paramManagement.selectParamsFirst'),
                    showClose: true
                });
                return;
            }
            this.deleteParams([row]);
        },
        deleteParams(params) {
            const paramCount = params.length;
            const paramIds = params.map(param => param.id).filter(id => id);
            if (paramIds.length === 0) {
                this.$message.error({
                    message: this.$t('paramManagement.invalidParamId'),
                    showClose: true
                });
                return;
            }
            this.$confirm(this.$t('paramManagement.confirmBatchDelete', { paramCount }), this.$t('message.warning'), {
                confirmButtonText: this.$t('button.ok'),
                cancelButtonText: this.$t('button.cancel'),
                type: 'warning'
            }).then(() => {
                Api.admin.deleteParam(paramIds, ({ data }) => {
                    if (data.code === 0) {
                        this.fetchParams();
                        this.$message.success({
                            message: this.$t('paramManagement.batchDeleteSuccess', { paramCount }),
                            showClose: true
                        });
                    } else {
                        this.$message.error({
                            message: data.msg || this.$t('paramManagement.deleteFailed'),
                            showClose: true
                        });
                    }
                });
            }).catch(() => {
                this.$message({
                    type: 'info',
                    message: this.$t('paramManagement.operationCancelled'),
                    duration: 1000
                });
            });
        },
        goToPage(page) {
            if (page !== this.currentPage) {
                this.currentPage = page;
                this.fetchParams();
            }
        },
        goFirst() {
            if (this.currentPage !== 1) {
                this.currentPage = 1;
                this.fetchParams();
            }
        },
        goPrev() {
            if (this.currentPage > 1) {
                this.currentPage--;
                this.fetchParams();
            }
        },
        goNext() {
            if (this.currentPage < this.pageCount) {
                this.currentPage++;
                this.fetchParams();
            }
        },
        isSensitiveParam(paramCode) {
            return this.sensitive_keys.some(key => paramCode.toLowerCase().includes(key));
        },
        maskSensitiveValue(value) {
            if (value.length <= 4) {
                return '****';
            }
            return value.substring(0, 2) + '****' + value.substring(value.length - 2);
        },
        toggleSensitiveValue(row) {
            row.showValue = !row.showValue;
        },
        headerCellClassName() {
            return 'header-cell';
        }
    }
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
</style>
