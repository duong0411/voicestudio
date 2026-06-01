package xiaozhi.modules.device.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("ai_ota")
@Schema(description = "")
public class OtaEntity {

    @TableId(type = IdType.ASSIGN_UUID)
    @Schema(description = "ID")
    private String id;

    @Schema(description = "")
    private String firmwareName;

    @Schema(description = "")
    private String type;

    @Schema(description = "")
    private String version;

    @Schema(description = "()")
    private Long size;

    @Schema(description = "/")
    private String remark;

    @Schema(description = "")
    private String firmwarePath;

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