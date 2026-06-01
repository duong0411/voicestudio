package xiaozhi.modules.sys.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;
import lombok.EqualsAndHashCode;
import xiaozhi.common.entity.BaseEntity;


@Data
@EqualsAndHashCode(callSuper = false)
@TableName("sys_params")
public class SysParamsEntity extends BaseEntity {
    
    private String paramCode;
    
    private String paramValue;
    
    private String valueType;
    
    private Integer paramType;
    
    private String remark;
    
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updater;
    
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateDate;

}