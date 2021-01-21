package issac.study.mbp.config;

import issac.study.mbp.constant.MyEnum;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author issac.hu
 */
@Component
@ConfigurationProperties(prefix = "my.config")
@Data
public class MyConfigProperties {
    private String name;
    private Map<String, String> map;
    private MyEnum myEnum;

}
