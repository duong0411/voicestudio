package xiaozhi.modules.agent.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@TableName("ai_agent_tag_relation")
@Schema(description = "")
public class AgentTagRelationEntity {

    @TableId(type = IdType.ASSIGN_UUID)
    @Schema(description = "")
    private String id;

    @Schema(description = "ID")
    private String agentId;

    @Schema(description = "ID")
    private String tagId;

    @Schema(description = "")
    private Integer sort;

    @Schema(description = "")
    private Long creator;

    @Schema(description = "")
    private Date createdAt;

    @Schema(description = "")
    private Long updater;

    @Schema(description = "")
    private Date updatedAt;
}
