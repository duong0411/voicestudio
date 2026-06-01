  <el-dialog :visible.sync="dialogVisible" :close-on-click-modal="false" width="57%" center custom-class="custom-dialog"
    :show-close="true" class="center-dialog" append-to-body>
    <div style="margin: 0 18px; text-align: left; padding: 10px; border-radius: 10px">
      <div style="
          font-size: 24px;
          color: var(--val-text, #e2e8f0);
          margin-top: -10px;
          margin-bottom: 10px;
          text-align: center;
          font-weight: 700;
        ">
        {{
          modelData.duplicateMode
            ? $t("modelConfigDialog.duplicateModel")
            : $t("modelConfigDialog.editModel")
        }}
      </div>

      <div style="
          display: flex;
          justify-content: space-between;
          align-items: center;
          margin-bottom: 10px;
        ">
        <div style="font-size: 16px; font-weight: 600; color: var(--val-text, #e2e8f0)">
          {{ $t("modelConfigDialog.modelInfo") }}
        </div>
        <div style="display: flex; align-items: center; gap: 20px">
          <div style="display: flex; align-items: center">
            <span style="margin-right: 8px">{{ $t("modelConfigDialog.enable") }}</span>
            <el-switch v-model="form.isEnabled" :active-value="1" :inactive-value="0" class="custom-switch"></el-switch>
          </div>
          <div style="display: none; align-items: center">
            <span style="margin-right: 8px">{{
              $t("modelConfigDialog.setDefault")
              }}</span>
            <el-switch v-model="form.isDefault" :active-value="1" :inactive-value="0" class="custom-switch"></el-switch>
          </div>
        </div>
      </div>

      <div style="height: 1px; background: rgba(255,255,255,0.1); margin-bottom: 22px"></div>

      <el-form :model="form" ref="form" label-width="auto" label-position="left" class="custom-form">
        <div style="display: flex; gap: 20px; margin-bottom: 0">
          <el-form-item :label="$t('modelConfigDialog.modelName')" prop="name" style="flex: 1">
            <el-input v-model="form.modelName" :placeholder="$t('modelConfigDialog.enterModelName')"
              class="custom-input-bg"></el-input>
          </el-form-item>
          <el-form-item :label="$t('modelConfigDialog.modelCode')" prop="code" style="flex: 1">
            <el-input v-model="form.modelCode" :placeholder="$t('modelConfigDialog.enterModelCode')"
              class="custom-input-bg"></el-input>
          </el-form-item>
        </div>

        <div style="display: flex; gap: 20px; margin-bottom: 0">
          <el-form-item :label="$t('modelConfigDialog.supplier')" prop="supplier" style="flex: 1">
            <el-select v-model="form.configJson.type" :placeholder="$t('modelConfigDialog.selectSupplier')"
              class="custom-select custom-input-bg" style="width: 100%" @focus="loadProviders" filterable>
              <el-option v-for="item in providers" :key="item.value" :label="item.label" :value="item.value" />
            </el-select>
          </el-form-item>
          <el-form-item :label="$t('modelConfigDialog.sortOrder')" prop="sort" style="flex: 1">
            <el-input v-model.number="form.sort" type="number" :placeholder="$t('modelConfigDialog.enterSortOrder')"
              class="custom-input-bg"></el-input>
          </el-form-item>
        </div>

        <el-form-item :label="$t('modelConfigDialog.docLink')" prop="docUrl" style="margin-bottom: 27px">
          <el-input v-model="form.docLink" :placeholder="$t('modelConfigDialog.enterDocLink')"
            class="custom-input-bg"></el-input>
        </el-form-item>

        <el-form-item :label="$t('modelConfigDialog.remark')" prop="remark" class="prop-remark">
          <el-input v-model="form.remark" type="textarea" :rows="3" :placeholder="$t('modelConfigDialog.enterRemark')"
            :autosize="{ minRows: 3, maxRows: 5 }" class="custom-input-bg"></el-input>
        </el-form-item>
      </el-form>

      <div style="font-size: 16px; font-weight: 600; color: var(--val-text, #e2e8f0); margin-bottom: 15px">
        {{ $t("modelConfigDialog.callInfo") }}
      </div>
      <div style="height: 1px; background: rgba(255,255,255,0.1); margin-bottom: 22px"></div>

      <el-form :model="form.configJson" ref="callInfoForm" label-width="auto" class="custom-form">
        <template v-for="(row, rowIndex) in chunkedCallInfoFields">
          <div :key="rowIndex" style="display: flex; gap: 20px; margin-bottom: 0">
            <el-form-item v-for="field in row" :key="field.prop" :label="field.label" :prop="field.prop"
              style="flex: 1">
              <template v-if="field.type === 'json-textarea'">
                <el-input v-model="fieldJsonMap[field.prop]" type="textarea" :rows="3"
                  :placeholder="$t('modelConfigDialog.enterJsonExample')" class="custom-input-bg"
                  @change="(val) => handleJsonChange(field.prop, val)" @focus="
                    isSensitiveField(field.prop)
                      ? handleJsonInputFocus(field.prop, fieldJsonMap[field.prop])
                      : undefined
                    " @blur="
                    isSensitiveField(field.prop)
                      ? handleJsonInputBlur(field.prop)
                      : undefined
                    "></el-input>
              </template>

              <el-input v-else v-model="form.configJson[field.prop]" :placeholder="field.placeholder" :type="field.type"
                class="custom-input-bg" :show-password="field.type === 'password'" @focus="
                  isSensitiveField(field.prop)
                    ? handleInputFocus(field.prop, form.configJson[field.prop])
                    : undefined
                  " @blur="
                  isSensitiveField(field.prop) ? handleInputBlur(field.prop) : undefined
                  "></el-input>
            </el-form-item>
          </div>
        </template>
      </el-form>
    </div>

    <div style="display: flex; justify-content: center">
      <el-button type="primary" @click="handleSave" class="save-btn" :loading="saving" :disabled="saving">
        {{ $t("modelConfigDialog.save") }}
      </el-button>
    </div>
  </el-dialog>
</template>

<script>
import Api from "@/apis/api";

export default {
  name: "ModelEditDialog",
  props: {
    visible: { type: Boolean, default: false },
    modelData: {
      type: Object,
      default: () => ({}),
      validator: (value) => typeof value === "object" && !Array.isArray(value),
    },
    modelType: { type: String, required: true },
  },
  data() {
    return {
      dialogVisible: this.visible,
      providers: [],
      providersLoaded: false,
      saving: false,
      allProvidersData: null,
      pendingProviderType: null,
      pendingModelData: null,
      dynamicCallInfoFields: [],
      fieldJsonMap: {}, // 用于存储JSON字段的字符串形式
      sensitive_keys: [
        "api_key",
        "personal_access_token",
        "access_token",
        "token",
        "secret",
        "access_key_secret",
        "secret_key",
      ],
      originalValues: {}, // 存储原始值，用于失焦时恢复
      form: {
        id: "",
        modelType: "",
        modelCode: "",
        modelName: "",
        isDefault: false,
        isEnabled: false,
        docLink: "",
        remark: "",
        sort: 0,
        configJson: {},
      },
    };
  },
  computed: {
    chunkedCallInfoFields() {
      const chunkSize = 2;
      const result = [];
      for (let i = 0; i < this.dynamicCallInfoFields.length; i += chunkSize) {
        result.push(this.dynamicCallInfoFields.slice(i, i + chunkSize));
      }
      return result;
    },
  },
  watch: {
    modelType() {
      this.resetProviders();
      this.loadProviders();
    },
    dialogVisible(val) {
      this.$emit("update:visible", val);
      if (!val) {
        this.resetForm();
      } else if (val && this.modelData.id) {
        this.loadModelData();
      }
    },
    visible(val) {
      this.dialogVisible = val;
      if (val) {
        this.loadProviders();
      }
    },
    "form.configJson.type"(newVal) {
      if (newVal && this.providersLoaded) {
        this.loadProviderFields(newVal);
      }
    },
  },
  methods: {
    resetForm() {
      this.form = {
        id: "",
        modelType: "",
        modelCode: "",
        modelName: "",
        isDefault: false,
        isEnabled: false,
        docLink: "",
        remark: "",
        sort: 0,
        configJson: {},
      };
      this.fieldJsonMap = {};
    },
    resetProviders() {
      this.providers = [];
      this.providersLoaded = false;
    },
    loadModelData() {
      if (this.modelData.id) {
        Api.model.getModelConfig(this.modelData.id, ({ data }) => {
          if (data.code === 0 && data.data) {
            let model = data.data;

            if (this.modelData.duplicateMode) {
              model.modelName =
                this.modelData.modelName + this.$t("modelConfigDialog.copySuffix");
              model.modelCode =
                this.modelData.modelCode + this.$t("modelConfigDialog.copySuffix");

              // 处理敏感字段
              if (model.configJson) {
                Object.keys(model.configJson).forEach((key) => {
                  if (this.isSensitiveField(key) && model.configJson[key]) {
                    const sensitiveName = this.getSensitiveFieldName(key);
                    model.configJson[key] = `你的${sensitiveName}`;
                  }
                });
              }
            }
            this.pendingProviderType = model.configJson.type;
            this.pendingModelData = model;

            if (this.providersLoaded) {
              this.loadProviderFields(model.configJson.type);
            } else {
              this.loadProviders();
            }
          }
        });
      }
    },
    handleSave() {
      this.saving = true; // 开始保存加载

      // 处理所有JSON字段
      Object.keys(this.fieldJsonMap).forEach((key) => {
        const parsed = this.validateJson(this.fieldJsonMap[key]);
        if (parsed !== null) {
          this.form.configJson[key] = parsed;
        }
      });

      const formData = {
        id: this.modelData.id,
        modelCode: this.form.modelCode,
        modelName: this.form.modelName,
        isDefault: this.form.isDefault ? 1 : 0,
        isEnabled: this.form.isEnabled ? 1 : 0,
        docLink: this.form.docLink,
        remark: this.form.remark,
        sort: this.form.sort || 0,
        configJson: { ...this.form.configJson },
      };

      this.$emit("save", {
        provideCode: this.form.configJson.type,
        formData,
        done: () => {
          this.saving = false; // 保存完成后回调
        },
      });

      // 如果父组件不处理done回调，3秒后自动关闭加载状态
      setTimeout(() => {
        this.saving = false;
      }, 3000);
    },
    loadProviders() {
      if (this.providersLoaded) return;

      Api.model.getModelProviders(this.modelType, (data) => {
        this.providers = data.map((item) => ({
          label: item.name,
          value: String(item.providerCode),
        }));
        this.providersLoaded = true;
        this.allProvidersData = data;

        if (this.pendingProviderType) {
          this.loadProviderFields(this.pendingProviderType);
        }
      });
    },
    loadProviderFields(providerCode) {
      if (this.allProvidersData) {
        const provider = this.allProvidersData.find(
          (p) => p.providerCode === providerCode
        );
        if (provider) {
          this.dynamicCallInfoFields = JSON.parse(provider.fields || "[]").map((f) => ({
            label: f.label,
            prop: f.key,
            type:
              f.type === "dict"
                ? "json-textarea"
                : f.type === "password"
                  ? "password"
                  : "text",
            placeholder: f.key,
            label: this.translateFieldLabel(f.label || f.key),
          }));

          if (this.pendingModelData && this.pendingProviderType === providerCode) {
            this.processModelData(this.pendingModelData);
            this.pendingModelData = null;
            this.pendingProviderType = null;
          }
        }
      }
    },
    processModelData(model) {
      let configJson = model.configJson || {};
      this.dynamicCallInfoFields.forEach((field) => {
        if (!configJson.hasOwnProperty(field.prop)) {
          configJson[field.prop] = "";
        } else if (field.type === "json-textarea") {
          this.$set(
            this.fieldJsonMap,
            field.prop,
            this.formatJson(configJson[field.prop])
          );
          configJson[field.prop] = this.ensureObject(configJson[field.prop]);
        } else if (typeof configJson[field.prop] !== "string") {
          configJson[field.prop] = String(configJson[field.prop]);
        }
      });

      this.form = {
        id: model.id,
        modelType: model.modelType,
        modelCode: model.modelCode,
        modelName: model.modelName,
        isDefault: model.isDefault,
        isEnabled: model.isEnabled,
        docLink: model.docLink,
        remark: model.remark,
        sort: Number(model.sort) || 0,
        configJson: { ...configJson },
      };
    },
    handleJsonChange(field, value) {
      const parsed = this.validateJson(value);
      if (parsed !== null) {
        this.form.configJson[field] = parsed;
      }
    },
    validateJson(value) {
      try {
        const parsed = JSON.parse(value);
        if (typeof parsed === "object" && parsed !== null && !Array.isArray(parsed)) {
          return parsed;
        }
        this.$message.error({
          message: '必须输入字典格式（如 {"key":"value"}），保存则使用原数据',
          showClose: true,
        });
        return null;
      } catch (e) {
        this.$message.error({
          message: 'JSON格式错误（如 {"key":"value"}），保存则使用原数据',
          showClose: true,
        });
        return null;
      }
    },
    formatJson(obj) {
      try {
        return JSON.stringify(obj, null, 2);
      } catch {
        return "";
      }
    },
    ensureObject(value) {
      return typeof value === "object" ? value : {};
    },

    // 检测字段是否为敏感字段
    isSensitiveField(fieldName) {
      // 将字段名转换为小写进行比较
      const lowerFieldName = fieldName.toLowerCase();
      // 精确匹配keyMap中定义的7个敏感词
      return this.sensitive_keys.includes(lowerFieldName);
    },

    // 获取敏感字段对应的中文名称
    getSensitiveFieldName(fieldName) {
      const keyMap = {
        api_key: "API密钥",
        personal_access_token: "个人访问令牌",
        access_token: "访问令牌",
        token: "令牌",
        secret: "密钥",
        access_key_secret: "访问密钥",
        secret_key: "密钥",
      };

      for (const [key, value] of Object.entries(keyMap)) {
        if (fieldName.toLowerCase().includes(key)) {
          return value;
        }
      }
      return "敏感信息";
    },

    // 处理input聚焦事件
    handleInputFocus(field, value) {
      // 如果值包含星号，清空显示
      if (value && value.includes("*")) {
        // 存储原始值，用于失焦时恢复
        this.$set(this.originalValues, field, this.form.configJson[field]);
        this.$set(this.form.configJson, field, "");
      }
    },

    // 处理input失焦事件
    handleInputBlur(field) {
      // 检查是否为敏感字段
      if (this.isSensitiveField(field)) {
        // 如果值为空，恢复掩码值
        if (!this.form.configJson[field] || this.form.configJson[field].trim() === "") {
          // 如果有原始值，则恢复原始值；否则设置为掩码提示
          if (this.originalValues[field]) {
            this.$set(this.form.configJson, field, this.originalValues[field]);
          } else {
            const sensitiveName = this.getSensitiveFieldName(field);
            this.$set(this.form.configJson, field, `你的${sensitiveName}`);
          }
          // 清除临时存储的原始值
          this.$delete(this.originalValues, field);
        }
      }
    },

    // 处理JSON字段的聚焦事件
    handleJsonInputFocus(field, value) {
      if (value && value.includes("*")) {
        this.$set(this.fieldJsonMap, field, "");
      }
    },

    // 处理JSON字段的失焦事件
    handleJsonInputBlur(field) {
      // JSON field - no special handling needed
    },

    // Translate Chinese field labels to English
    translateFieldLabel(label) {
      const labelMap = {
        '模型名称': 'Model Name', '接口地址': 'Base URL', '密钥': 'API Key',
        '温度': 'Temperature', '最大token数': 'Max Tokens', '频率惩罚': 'Frequency Penalty',
        '存在惩罚': 'Presence Penalty', '顶部概率': 'Top P', '模型': 'Model',
        '应用ID': 'App ID', '访问令牌': 'Access Token', '机器人ID': 'Bot ID',
        '用户ID': 'User ID', '个人令牌': 'Personal Token', '集群': 'Cluster',
        '语速': 'Speed', '音量': 'Volume', '音调': 'Pitch', '声音': 'Voice',
        '服务器地址': 'Server Host', '端口': 'Port', '代理': 'Proxy',
        '最大重试次数': 'Max Retries', '超时时间': 'Timeout', '请求模式': 'Request Mode',
        '说话人': 'Speaker', '资源ID': 'Resource ID', '输出目录': 'Output Dir',
        '阈值': 'Threshold', '模型目录': 'Model Dir', '语言': 'Language',
        '描述': 'Description', '变量': 'Variables', '代理ID': 'Agent ID',
        '排序': 'Sort', '文档链接': 'Doc Link', '备注': 'Remark',
        '基础URL': 'Base URL', 'API密钥': 'API Key', '最大令牌数': 'Max Tokens',
        'top_p值': 'Top P', 'top_k值': 'Top K', '历史消息数': 'History Count',
        '系统提示词': 'System Prompt'
      };
      return labelMap[label] || label;
    },
  },
};
</script>

<style lang="scss" scoped>
.custom-dialog {
  position: relative;
  border-radius: 16px;
  overflow: hidden;
  background: var(--val-bg-card, rgba(18, 24, 41, 0.97)) !important;
  border: 1px solid rgba(255,255,255,0.1);
  padding-bottom: 17px;
  backdrop-filter: blur(16px);
}

.custom-dialog .el-dialog__header {
  padding: 0;
  border-bottom: none;
}

.center-dialog {
  display: flex;
  align-items: center;
  justify-content: center;
}

.custom-close-btn {
  position: absolute;
  top: 20px;
  right: 20px;
  width: 35px;
  height: 35px;
  border-radius: 50%;
  border: 2px solid #cfcfcf;
  background: none;
  font-size: 30px;
  font-weight: lighter;
  color: #cfcfcf;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1;
  padding: 0;
  outline: none;
}

.custom-close-btn:hover {
  color: #409eff;
  border-color: #409eff;
}

.custom-select .el-input__suffix {
  background: #e6e8ea;
  right: 6px;
  width: 20px;
  height: 20px;
  display: flex;
  justify-content: center;
  align-items: center;
  top: 9px;
}

.custom-select .el-input__suffix-inner {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 100%;
}

.custom-select .el-icon-arrow-up:before {
  content: "";
  display: inline-block;
  width: 0;
  height: 0;
  border-left: 5px solid transparent;
  border-right: 5px solid transparent;
  border-top: 7px solid #c0c4cc;
  position: relative;
  top: -2px;
  transform: rotate(180deg);
}

.custom-form .el-form-item {
  margin-bottom: 20px;
}

.custom-form .el-form-item__label {
  color: var(--val-text-muted, #94a3b8);
  font-weight: 500;
  text-align: right;
  padding-right: 20px;
}

.custom-form .el-form-item.prop-remark .el-form-item__label {
  margin-top: -4px;
}

.custom-input-bg .el-input__inner::-webkit-input-placeholder,
.custom-input-bg .el-textarea__inner::-webkit-input-placeholder {
  color: rgba(148, 163, 184, 0.6);
}

.custom-input-bg .el-input__inner,
.custom-input-bg .el-textarea__inner {
  background-color: rgba(255, 255, 255, 0.05) !important;
  border: 1px solid rgba(255,255,255,0.12) !important;
  color: var(--val-text, #e2e8f0) !important;
}

.save-btn {
  background: #e6f0fd;
  color: #237ff4;
  border: 1px solid #b3d1ff;
  width: 150px;
  height: 40px;
  font-size: 16px;
  transition: all 0.3s ease;
}

.save-btn:hover {
  background: linear-gradient(to right, #237ff4, #9c40d5);
  color: var(--val-text) ;
  border: none;
}

.custom-switch .el-switch__core {
  border-radius: 20px;
  height: 23px;
  background-color: #c0ccda;
  width: 35px;
  padding: 0 20px;
}

.custom-switch .el-switch__core:after {
  width: 15px;
  height: 15px;
  background-color: transparent ;
  top: 3px;
  left: 4px;
  transition: all 0.3s;
}

.custom-switch.is-checked .el-switch__core {
  border-color: #b5bcf0;
  background-color: #cfd7fa;
  padding: 0 20px;
}

.custom-switch.is-checked .el-switch__core:after {
  left: 100%;
  margin-left: -18px;
  background-color: #1b47ee;
}

[style*="display: flex"] {
  gap: 20px;
}

.custom-input-bg .el-input__inner {
  height: 32px;
}

</style>

<!-- Non-scoped: override Element UI dialog background -->
<style lang="scss">
.custom-dialog.el-dialog {
  background: rgba(18, 24, 41, 0.98) !important;
  border: 1px solid rgba(255, 255, 255, 0.12) !important;
  border-radius: 16px !important;
  backdrop-filter: blur(20px);
  box-shadow: 0 24px 64px rgba(0, 0, 0, 0.6) !important;
}

.custom-dialog.el-dialog .el-dialog__body {
  background: transparent !important;
  color: #e2e8f0 !important;
  padding: 20px 20px 0;
}

.custom-dialog.el-dialog .el-dialog__header {
  background: transparent !important;
  padding: 0;
}
</style>
