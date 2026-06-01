package xiaozhi.modules.device.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import lombok.Data;


@Data
@Schema(description = "DTO")
public class DevicePageUserDTO {

    @Schema(description = "")
    private String keywords;

    @Schema(description = "")
    @Min(value = 0, message = "{page.number}")
    private String page;

    @Schema(description = "")
    @Min(value = 0, message = "{limit.number}")
    private String limit;
}
