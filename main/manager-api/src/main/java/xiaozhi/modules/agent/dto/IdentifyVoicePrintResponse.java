package xiaozhi.modules.agent.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;


@Data
public class IdentifyVoicePrintResponse {
    
    @JsonProperty("speaker_id")
    private String speakerId;
    
    private Double score;
}
