package xiaozhi.common.xss;

import java.util.Collections;
import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;


@Data
@ConfigurationProperties(prefix = "renren.xss")
public class XssProperties {
    
    private boolean enabled;
    
    private List<String> excludeUrls = Collections.emptyList();
}
