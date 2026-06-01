package xiaozhi.modules.device.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Schema(description = "OTA，")
public class DeviceReportRespDTO {
    @Schema(description = "")
    private ServerTime server_time;

    @Schema(description = "")
    private Activation activation;

    @Schema(description = "")
    private String error;

    @Schema(description = "")
    private Firmware firmware;

    @Schema(description = "WebSocket")
    private Websocket websocket;

    @Schema(description = "MQTT Gateway")
    private MQTT mqtt;

    @Getter
    @Setter
    public static class Firmware {
        @Schema(description = "")
        private String version;
        @Schema(description = "")
        private String url;
    }

    public static DeviceReportRespDTO createError(String message) {
        DeviceReportRespDTO resp = new DeviceReportRespDTO();
        resp.setError(message);
        return resp;
    }

    @Setter
    @Getter
    public static class Activation {
        @Schema(description = "")
        private String code;

        @Schema(description = ": ")
        private String message;

        @Schema(description = "")
        private String challenge;
    }

    @Getter
    @Setter
    public static class ServerTime {
        @Schema(description = "")
        private Long timestamp;

        @Schema(description = "")
        private String timeZone;

        @Schema(description = "，")
        private Integer timezone_offset;
    }

    @Getter
    @Setter
    public static class Websocket {
        @Schema(description = "WebSocket")
        private String url;
        @Schema(description = "WebSocket  token")
        private String token;
    }

    @Getter
    @Setter
    public static class MQTT {
        @Schema(description = "MQTT ")
        private String endpoint;
        @Schema(description = "MQTT ")
        private String client_id;
        @Schema(description = "MQTT ")
        private String username;
        @Schema(description = "MQTT ")
        private String password;
        @Schema(description = "ESP32 ")
        private String publish_topic;
        @Schema(description = "ESP32 ")
        private String subscribe_topic;
    }
}