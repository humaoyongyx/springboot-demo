package issac.study.mbp;


import issac.study.mbp.base.BaseServiceTest;
import issac.study.mbp.config.MyConfig;
import issac.study.mbp.core.builder.DConfBuilder;
import org.junit.Test;

/**
 * @author issac.hu
 */
public class SimpleTest extends BaseServiceTest {

    MyConfig myConfig = DConfBuilder.get(MyConfig.class);

    @Test
    public void test() {
        System.out.println(myConfig.getHello());
    }

}
