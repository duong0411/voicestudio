package xiaozhi.modules.device.dto;

import java.io.Serializable;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;


@Data
@Schema(description = "")
public class DeviceUnBindDTO implements Serializable {

    @Schema(description = "ID")
    @NotBlank(message = "ID")
    private String deviceId;

}