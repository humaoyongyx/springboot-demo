package issac.study.mbp;


import issac.study.mbp.base.BaseServiceTest;
import issac.study.mbp.config.DConf;
import issac.study.mbp.core.config.DynamicConfig;
import org.junit.Test;

/**
 * @author issac.hu
 */
public class SimpleTest extends BaseServiceTest {

    DConf dConf = DynamicConfig.get(DConf.class);

    @Test
    public void test() {
        System.out.println(dConf.getHello());
    }

}
