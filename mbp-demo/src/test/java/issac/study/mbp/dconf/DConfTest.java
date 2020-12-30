package issac.study.mbp.dconf;

import issac.study.mbp.config.DConf;
import issac.study.mbp.core.config.DynamicConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author issac.hu
 */
@SpringBootTest(args = "--dConf.hello=你好世界")
@RunWith(SpringRunner.class)
public class DConfTest {

    DConf dConf = DynamicConfig.get(DConf.class);

    @Test
    public void test(){
        System.out.println(dConf.getHello());
    }
}
