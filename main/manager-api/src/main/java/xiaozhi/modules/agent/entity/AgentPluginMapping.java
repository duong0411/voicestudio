package xiaozhi.modules.agent.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;


@Data
@TableName(value = "ai_agent_plugin_mapping")
@Schema(description = "Agent")
public class AgentPluginMapping implements Serializable {
    
    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description = "ID")
    private Long id;

    
    @Schema(description = "ID")
    private String agentId;

    
    @Schema(description = "ID")
    private String pluginId;

    
    @Schema(description = "(Json)")
    private String paramInfo;


    @TableField(exist = false)
    @Schema(description = "provider_code, ai_model_provider")
    private String providerCode;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}