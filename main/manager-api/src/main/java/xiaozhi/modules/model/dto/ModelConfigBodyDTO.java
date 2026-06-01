package xiaozhi.modules.model.dto;

import java.io.Serial;

import cn.hutool.json.JSONObject;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "/")
public class ModelConfigBodyDTO {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "ID,")
    private String id;

    @Schema(description = "(AliLLM、DoubaoTTS)")
    private String modelCode;

    @Schema(description = "")
    private String modelName;

    @Schema(description = "(0 1)")
    private Integer isDefault;

    @Schema(description = "")
    private Integer isEnabled;

    @Schema(description = "(JSON)")
    private JSONObject configJson;

    @Schema(description = "")
    private String docLink;

    @Schema(description = "")
    private String remark;

    @Schema(description = "")
    private Integer sort;
}
