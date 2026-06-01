package xiaozhi.modules.model.dto;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import xiaozhi.common.validator.group.UpdateGroup;

@Data
@Schema(description = "/")
public class ModelProviderDTO implements Serializable {
    @Schema(description = "")
    @NotBlank(message = "id", groups = UpdateGroup.class)
    private String id;

    @Schema(description = "(Memory/ASR/VAD/LLM/TTS)")
    @NotBlank(message = "modelType")
    private String modelType;

    @Schema(description = "")
    @NotBlank(message = "providerCode")
    private String providerCode;

    @Schema(description = "")
    @NotBlank(message = "name")
    private String name;

    @Schema(description = "(JSON)")
    @TableField(typeHandler = JacksonTypeHandler.class)
    @NotBlank(message = "fields(JSON)")
    private String fields;

    @Schema(description = "")
    @NotNull(message = "sort")
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
