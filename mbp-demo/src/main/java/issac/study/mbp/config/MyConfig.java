package issac.study.mbp.config;

import issac.study.mbp.core.annotation.ConfigKey;
import issac.study.mbp.core.annotation.DConf;
import lombok.Data;

/**
 * @author issac.hu
 */
@Data
@DConf
public class MyConfig {

    @ConfigKey("dConf.hello")
    private String hello;

    @ConfigKey("password")
    private String password;
}
