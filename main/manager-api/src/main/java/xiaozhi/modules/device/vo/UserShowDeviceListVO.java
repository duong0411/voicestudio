package xiaozhi.modules.device.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "VO")
public class UserShowDeviceListVO {

    @Schema(description = "app")
    private String appVersion;

    @Schema(description = "")
    private String bindUserName;

    @Schema(description = "")
    private String deviceType;

    @Schema(description = "")
    private String id;

    @Schema(description = "mac")
    private String macAddress;

    @Schema(description = "OTA")
    private Integer otaUpgrade;

    @Schema(description = "")
    private String recentChatTime;

}