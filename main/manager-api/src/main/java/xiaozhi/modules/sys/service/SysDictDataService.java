package xiaozhi.modules.sys.service;

import java.util.List;
import java.util.Map;

import xiaozhi.common.page.PageData;
import xiaozhi.common.service.BaseService;
import xiaozhi.modules.sys.dto.SysDictDataDTO;
import xiaozhi.modules.sys.entity.SysDictDataEntity;
import xiaozhi.modules.sys.vo.SysDictDataItem;
import xiaozhi.modules.sys.vo.SysDictDataVO;


public interface SysDictDataService extends BaseService<SysDictDataEntity> {

    
    PageData<SysDictDataVO> page(Map<String, Object> params);

    
    SysDictDataVO get(Long id);

    
    void save(SysDictDataDTO dto);

    
    void update(SysDictDataDTO dto);

    
    void delete(Long[] ids);

    
    void deleteByTypeId(Long dictTypeId);

    
    List<SysDictDataItem> getDictDataByType(String dictType);

}