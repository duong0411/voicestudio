package xiaozhi.modules.model.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;

import cn.hutool.json.JSONObject;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@TableName(value = "ai_model_config", autoResultMap = true)
@Schema(description = "")
public class ModelConfigEntity {

    @Schema(description = "")
    private String id;

    @Schema(description = "(Memory/ASR/VAD/LLM/TTS)")
    private String modelType;

    @Schema(description = "(AliLLM、DoubaoTTS)")
    private String modelCode;

    @Schema(description = "")
    private String modelName;

    @Schema(description = "(0 1)")
    private Integer isDefault;

    @Schema(description = "")
    private Integer isEnabled;

    @TableField(typeHandler = JacksonTypeHandler.class)
    @Schema(description = "(JSON)")
    private JSONObject configJson;

    @Schema(description = "")
    private String docLink;

    @Schema(description = "")
    private String remark;

    @Schema(description = "")
    private Integer sort;

    @Schema(description = "")
    @TableField(fill = FieldFill.UPDATE)
    private Long updater;

    @Schema(description = "")
    @TableField(fill = FieldFill.UPDATE)
    private Date updateDate;

    @Schema(description = "")
    @TableField(fill = FieldFill.INSERT)
    private Long creator;

    @Schema(description = "")
    @TableField(fill = FieldFill.INSERT)
    private Date createDate;
}
