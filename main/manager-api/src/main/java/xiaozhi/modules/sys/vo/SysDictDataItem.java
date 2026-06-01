package xiaozhi.modules.sys.vo;

import java.io.Serializable;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;


@Data
@Schema(description = "")
public class SysDictDataItem implements Serializable {

    @Schema(description = "")
    private String name;

    @Schema(description = "")
    private String key;
}
