package xiaozhi.modules.sys.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import lombok.Data;


@Data
@Schema(description = "DTO")
public class AdminPageUserDTO {

    @Schema(description = "")
    private String mobile;

    @Schema(description = "")
    @Min(value = 0, message = "{sort.number}")
    private String page;

    @Schema(description = "")
    @Min(value = 0, message = "{sort.number}")
    private String limit;
}
