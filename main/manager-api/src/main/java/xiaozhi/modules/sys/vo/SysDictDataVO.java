package xiaozhi.modules.sys.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;


@Data
@Schema(description = "VO")
public class SysDictDataVO implements Serializable {
    @Schema(description = "")
    private Long id;

    @Schema(description = "ID")
    private Long dictTypeId;

    @Schema(description = "")
    private String dictLabel;

    @Schema(description = "")
    private String dictValue;

    @Schema(description = "")
    private String remark;

    @Schema(description = "")
    private Integer sort;

    @Schema(description = "")
    private Long creator;

    @Schema(description = "")
    private String creatorName;

    @Schema(description = "")
    private Date createDate;

    @Schema(description = "")
    private Long updater;

    @Schema(description = "")
    private String updaterName;

    @Schema(description = "")
    private Date updateDate;
}
