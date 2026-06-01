package xiaozhi.modules.sys.vo;

import java.util.Date;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;


@Data
public class AdminPageUserVO {

    @Schema(description = "")
    private String deviceCount;

    @Schema(description = "")
    private String mobile;

    @Schema(description = "")
    private Integer status;

    @Schema(description = "id")
    private String userid;

    @Schema(description = "")
    private Date createDate;
}
