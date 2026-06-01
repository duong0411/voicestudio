package xiaozhi.common.redis;


public class RedisKeys {
    
    public static String getSysParamsKey() {
        return "sys:params";
    }

    
    public static String getCaptchaKey(String uuid) {
        return "sys:captcha:" + uuid;
    }

    
    public static String getDeviceCaptchaKey(String captcha) {
        return "sys:device:captcha:" + captcha;
    }

    
    public static String getUserIdKey(Long userid) {
        return "sys:username:id:" + userid;
    }

    
    public static String getModelNameById(String id) {
        return "model:name:" + id;
    }

    
    public static String getModelConfigById(String id) {
        return "model:data:" + id;
    }

    
    public static String getTimbreNameById(String id) {
        return "timbre:name:" + id;
    }

    
    public static String getAgentDeviceCountById(String id) {
        return "agent:device:count:" + id;
    }

    
    public static String getAgentDeviceLastConnectedAtById(String id) {
        return "agent:device:lastConnected:" + id;
    }

    
    public static String getServerConfigKey() {
        return "server:config";
    }

    
    public static String getTimbreDetailsKey(String id) {
        return "timbre:details:" + id;
    }

    
    public static String getVersionKey() {
        return "sys:version";
    }

    
    public static String getOtaIdKey(String uuid) {
        return "ota:id:" + uuid;
    }

    
    public static String getOtaDownloadCountKey(String uuid) {
        return "ota:download:count:" + uuid;
    }

    
    public static String getDictDataByTypeKey(String dictType) {
        return "sys:dict:data:" + dictType;
    }

    
    public static String getAgentAudioIdKey(String uuid) {
        return "agent:audio:id:" + uuid;
    }

    
    public static String getSMSValidateCodeKey(String phone) {
        return "sms:Validate:Code:" + phone;
    }

    
    public static String getSMSLastSendTimeKey(String phone) {
        return "sms:Validate:Code:" + phone + ":last_send_time";
    }

    
    public static String getSMSTodayCountKey(String phone) {
        return "sms:Validate:Code:" + phone + ":today_count";
    }

    
    public static String getChatHistoryKey(String uuid) {
        return "agent:chat:history:" + uuid;
    }

    
    public static String getVoiceCloneAudioIdKey(String uuid) {
        return "voiceClone:audio:id:" + uuid;
    }

    
    public static String getKnowledgeBaseCacheKey(String datasetId) {
        return "knowledge:base:" + datasetId;
    }

    
    public static String getTmpRegisterMacKey(String deviceId) {
        return "tmp_register_mac:" + deviceId;
    }

    
    public static String getOtaActivationCode(String activationCode) {
        return "ota:activation:code:" + activationCode;
    }

    
    public static String getOtaDeviceActivationInfo(String deviceId) {
        return "ota:activation:data:" + deviceId;
    }

    
    public static String getOtaUploadCountKey(Long username) {
        return "ota:upload:count:" + username;
    }

}
