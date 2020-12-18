package issac.study.mbp.config;

import issac.study.mbp.core.annotation.ConfigKey;
import lombok.Data;

/**
 * @author issac.hu
 */
@Data
public class DConf {

    @ConfigKey("dConf.hello")
    private String hello;
}
