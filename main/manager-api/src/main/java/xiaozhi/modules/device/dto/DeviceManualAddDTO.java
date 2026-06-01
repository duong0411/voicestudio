package xiaozhi.modules.device.dto;

import lombok.Data;

@Data
public class DeviceManualAddDTO {
    private String agentId;
    private String board;
    private String appVersion;
    private String macAddress;
} 