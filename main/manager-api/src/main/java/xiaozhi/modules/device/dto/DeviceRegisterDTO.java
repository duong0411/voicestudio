package xiaozhi.modules.device.dto;

import java.io.Serializable;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
@Schema(description = "")
public class DeviceRegisterDTO implements Serializable {

    @Schema(description = "mac")
    private String macAddress;

}