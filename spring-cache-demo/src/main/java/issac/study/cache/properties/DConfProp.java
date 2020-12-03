package issac.study.cache.properties;

import issac.study.cache.core.config.ConfigKey;
import lombok.Data;

/**
 * @author issac.hu
 */
@Data
public class DConfProp {

    @ConfigKey("dConf.hello")
    private String hello = "hello world!";
}
