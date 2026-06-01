package xiaozhi.modules.device.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import xiaozhi.common.page.PageData;
import xiaozhi.common.service.BaseService;
import xiaozhi.modules.device.dto.DeviceManualAddDTO;
import xiaozhi.modules.device.dto.DevicePageUserDTO;
import xiaozhi.modules.device.dto.DeviceReportReqDTO;
import xiaozhi.modules.device.dto.DeviceReportRespDTO;
import xiaozhi.modules.device.entity.DeviceEntity;
import xiaozhi.modules.device.vo.UserShowDeviceListVO;

public interface DeviceService extends BaseService<DeviceEntity> {
    
    String getDeviceOnlineData(String agentId);

    
    DeviceReportRespDTO checkDeviceActive(String macAddress, String clientId,
            DeviceReportReqDTO deviceReport);

    
    List<DeviceEntity> getUserDevices(Long userId, String agentId);

    
    void unbindDevice(Long userId, String deviceId);

    
    Boolean deviceActivation(String agentId, String activationCode);

    
    void deleteByUserId(Long userId);

    
    void deleteByAgentId(String agentId);

    
    Long selectCountByUserId(Long userId);

    
    PageData<UserShowDeviceListVO> page(DevicePageUserDTO dto);

    
    DeviceEntity getDeviceByMacAddress(String macAddress);

    
    String geCodeByDeviceId(String deviceId);

    
    Date getLatestLastConnectionTime(String agentId);

    
    void manualAddDevice(Long userId, DeviceManualAddDTO dto);

    
    void updateDeviceConnectionInfo(String agentId, String deviceId, String appVersion);

    
    String generateWebSocketToken(String clientId, String username) throws Exception;

    
    List<DeviceEntity> searchDevicesByMacAddress(String macAddress, Long userId);

    
    Object getDeviceTools(String deviceId);

    
    Object callDeviceTool(String deviceId, String toolName, Map<String, Object> arguments);

}