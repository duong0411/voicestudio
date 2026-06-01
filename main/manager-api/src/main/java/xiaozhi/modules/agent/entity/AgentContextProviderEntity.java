package xiaozhi.modules.agent.entity;

import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import xiaozhi.modules.agent.dto.ContextProviderDTO;

@Data
@TableName(value = "ai_agent_context_provider", autoResultMap = true)
@Schema(description = "")
public class AgentContextProviderEntity {

    @TableId(type = IdType.ASSIGN_UUID)
    @Schema(description = "")
    private String id;

    @Schema(description = "ID")
    private String agentId;

    @Schema(description = "")
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<ContextProviderDTO> contextProviders;

    @Schema(description = "")
    private Long creator;

    @Schema(description = "")
    private Date createdAt;

    @Schema(description = "")
    private Long updater;

    @Schema(description = "")
    private Date updatedAt;
}
