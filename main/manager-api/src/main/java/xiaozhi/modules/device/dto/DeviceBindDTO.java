package xiaozhi.modules.device.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
@Schema(description = "")
public class DeviceBindDTO {

    @Schema(description = "mac")
    private String macAddress;

    @Schema(description = "id")
    private Long userId;

    @Schema(description = "id")
    private String agentId;

}