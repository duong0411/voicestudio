package xiaozhi.modules.sys.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import xiaozhi.common.dao.BaseDao;
import xiaozhi.modules.sys.entity.SysParamsEntity;


@Mapper
public interface SysParamsDao extends BaseDao<SysParamsEntity> {
    
    String getValueByCode(String paramCode);

    
    List<String> getParamCodeList(String[] ids);

    
    int updateValueByCode(@Param("paramCode") String paramCode, @Param("paramValue") String paramValue);
}
