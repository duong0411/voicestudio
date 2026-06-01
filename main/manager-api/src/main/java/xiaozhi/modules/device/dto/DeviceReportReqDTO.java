package xiaozhi.modules.device.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Setter
@Getter
@Schema(description = "")
public class DeviceReportReqDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @Schema(description = "")
    private Integer version;

    @Schema(description = "（：）")
    @JsonProperty("flash_size")
    private Integer flashSize;

    @Schema(description = "（）")
    @JsonProperty("minimum_free_heap_size")
    private Integer minimumFreeHeapSize;

    @Schema(description = " MAC ")
    @JsonProperty("mac_address")
    private String macAddress;

    @Schema(description = " UUID")
    private String uuid;

    @Schema(description = "")
    @JsonProperty("chip_model_name")
    private String chipModelName;

    @Schema(description = "")
    @JsonProperty("chip_info")
    private ChipInfo chipInfo;

    @Schema(description = "")
    private Application application;

    @Schema(description = "")
    @JsonProperty("partition_table")
    private List<Partition> partitionTable;

    @Schema(description = " OTA ")
    private OtaInfo ota;

    @Schema(description = "")
    private BoardInfo board;

    // endregion

    @Getter
    @Setter
    @Schema(description = "")
    public static class ChipInfo {
        @Schema(description = "")
        private Integer model;

        @Schema(description = "")
        private Integer cores;

        @Schema(description = "")
        private Integer revision;

        @Schema(description = "")
        private Integer features;
    }

    @Getter
    @Setter
    @Schema(description = "")
    public static class Application {
        @Schema(description = "")
        private String name;

        @Schema(description = "")
        private String version;

        @Schema(description = "（UTC ISO）")
        @JsonProperty("compile_time")
        private String compileTime;

        @Schema(description = "ESP-IDF ")
        @JsonProperty("idf_version")
        private String idfVersion;

        @Schema(description = "ELF  SHA256 ")
        @JsonProperty("elf_sha256")
        private String elfSha256;
    }

    @Getter
    @Setter
    @Schema(description = "")
    public static class Partition {
        @Schema(description = "")
        private String label;

        @Schema(description = "")
        private Integer type;

        @Schema(description = "")
        private Integer subtype;

        @Schema(description = "")
        private Integer address;

        @Schema(description = "")
        private Integer size;
    }

    @Getter
    @Setter
    @Schema(description = "OTA")
    public static class OtaInfo {
        @Schema(description = "OTA")
        private String label;
    }

    @Getter
    @Setter
    @Schema(description = "")
    public static class BoardInfo {
        @Schema(description = "")
        private String type;

        @Schema(description = " Wi-Fi SSID")
        private String ssid;

        @Schema(description = "Wi-Fi （RSSI）")
        private Integer rssi;

        @Schema(description = "Wi-Fi ")
        private Integer channel;

        @Schema(description = "IP ")
        private String ip;

        @Schema(description = "MAC ")
        private String mac;
    }
}
