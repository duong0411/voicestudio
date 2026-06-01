package xiaozhi.modules.sys.service;

import java.util.List;
import java.util.Map;

import xiaozhi.common.page.PageData;
import xiaozhi.common.service.BaseService;
import xiaozhi.modules.sys.dto.SysDictTypeDTO;
import xiaozhi.modules.sys.entity.SysDictTypeEntity;
import xiaozhi.modules.sys.vo.SysDictTypeVO;


public interface SysDictTypeService extends BaseService<SysDictTypeEntity> {

    
    PageData<SysDictTypeVO> page(Map<String, Object> params);

    
    SysDictTypeVO get(Long id);

    
    void save(SysDictTypeDTO dto);

    
    void update(SysDictTypeDTO dto);

    
    void delete(Long[] ids);

    
    List<SysDictTypeVO> list(Map<String, Object> params);
}