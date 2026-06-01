package xiaozhi.common.constant;

import lombok.Getter;


public interface Constant {
    
    int SUCCESS = 1;
    
    int FAIL = 0;
    /**
     * OK
     */
    String OK = "OK";
    
    String USER_KEY = "userId";
    
    Long MENU_ROOT = 0L;
    
    Long DEPT_ROOT = 0L;
    
    Long DICT_ROOT = 0L;
    
    String ASC = "asc";
    
    String DESC = "desc";
    
    String CREATE_DATE = "create_date";

    
    String ID = "id";

    
    String SQL_FILTER = "sqlFilter";

    
    String PAGE = "page";
    
    String LIMIT = "limit";
    
    String ORDER_FIELD = "orderField";
    
    String ORDER = "order";

    
    String AUTHORIZATION = "Authorization";

    
    String SERVER_SECRET = "server.secret";

    
    String SM2_PUBLIC_KEY = "server.public_key";

    
    String SM2_PRIVATE_KEY = "server.private_key";

    
    String SERVER_WEBSOCKET = "server.websocket";

    
    String SERVER_MQTT_GATEWAY = "server.mqtt_gateway";

    
    String SERVER_OTA = "server.ota";

    
    String SERVER_ALLOW_USER_REGISTER = "server.allow_user_register";

    
    String SERVER_FRONTED_URL = "server.fronted_url";

    
    String FILE_EXTENSION_SEG = ".";

    
    String SERVER_MCP_ENDPOINT = "server.mcp_endpoint";

    
    String SERVER_VOICE_PRINT = "server.voice_print";

    
    String SERVER_MQTT_SECRET = "server.mqtt_signature_key";

    
    String SERVER_AUTH_ENABLED = "server.auth.enabled";

    
    String MEMORY_NO_MEM = "Memory_nomem";

    
    String MEMORY_MEM_REPORT_ONLY = "Memory_mem_report_only";

    
    String MEMORY_MEM0AI = "Memory_mem0ai";

    
    String MEMORY_POWERMEM = "Memory_powermem";

    
    String VOICE_CLONE_HUOSHAN_DOUBLE_STREAM = "huoshan_double_stream";

    
    String RAG_CONFIG_TYPE = "RAG";

    enum SysBaseParam {
        
        BEIAN_ICP_NUM("server.beian_icp_num"),
        
        BEIAN_GA_NUM("server.beian_ga_num"),
        
        SERVER_NAME("server.name");

        private String value;

        SysBaseParam(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    
    enum TrainStatus {
        
        NOT_TRAINED(0),
        
        TRAINING(1),
        
        TRAINED(2),
        
        TRAIN_FAILED(3);

        private final int code;

        TrainStatus(int code) {
            this.code = code;
        }

        public int getCode() {
            return code;
        }
    }

    
    enum SysMSMParam {
        
        ALIYUN_SMS_ACCESS_KEY_ID("aliyun.sms.access_key_id"),
        
        ALIYUN_SMS_ACCESS_KEY_SECRET("aliyun.sms.access_key_secret"),
        
        ALIYUN_SMS_SIGN_NAME("aliyun.sms.sign_name"),
        
        ALIYUN_SMS_SMS_CODE_TEMPLATE_CODE("aliyun.sms.sms_code_template_code"),
        
        SERVER_SMS_MAX_SEND_COUNT("server.sms_max_send_count"),
        
        SERVER_ENABLE_MOBILE_REGISTER("server.enable_mobile_register");

        private String value;

        SysMSMParam(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    
    enum DataOperation {
        
        INSERT("I"),
        
        UPDATE("U"),
        
        DELETE("D");

        private String value;

        DataOperation(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    @Getter
    enum ChatHistoryConfEnum {
        IGNORE(0, ""),
        RECORD_TEXT(1, ""),
        RECORD_TEXT_AUDIO(2, "");

        private final int code;
        private final String name;

        ChatHistoryConfEnum(int code, String name) {
            this.code = code;
            this.name = name;
        }
    }

    
    public static final String VERSION = "0.9.3";

    
    String INVALID_FIRMWARE_URL = "http://xiaozhi.server.com:8002/xiaozhi/otaMag/download/NOT_ACTIVATED_FIRMWARE_THIS_IS_A_INVALID_URL";

    
    enum DictType {
        
        MOBILE_AREA("MOBILE_AREA");

        private String value;

        DictType(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }
}