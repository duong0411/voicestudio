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
@TableName("ai_device")
@Schema(description = "")
public class DeviceEntity {

    @TableId(type = IdType.ASSIGN_UUID)
    @Schema(description = "ID")
    private String id;

    @Schema(description = "ID")
    private Long userId;

    @Schema(description = "MAC")
    private String macAddress;

    @Schema(description = "")
    private Date lastConnectedAt;

    @Schema(description = "(0/1)")
    private Integer autoUpdate;

    @Schema(description = "")
    private String board;

    @Schema(description = "")
    private String alias;

    @Schema(description = "ID")
    private String agentId;

    @Schema(description = "")
    private String appVersion;

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