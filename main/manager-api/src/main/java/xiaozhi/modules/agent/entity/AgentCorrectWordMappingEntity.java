package xiaozhi.modules.agent.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@TableName("ai_agent_correct_word_mapping")
@Schema(description = "")
public class AgentCorrectWordMappingEntity {

    @TableId(type = IdType.ASSIGN_UUID)
    @Schema(description = "")
    private String id;

    @Schema(description = "ID")
    private String agentId;

    @Schema(description = "ID")
    private String fileId;

    @Schema(description = "")
    private Long creator;

    @Schema(description = "")
    private Date createdAt;

    @Schema(description = "")
    private Long updater;

    @Schema(description = "")
    private Date updatedAt;
}
