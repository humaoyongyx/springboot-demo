package issac.study.mbp.dconf;

import issac.study.mbp.config.MyConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author issac.hu
 */
@SpringBootTest(args = "--dConf.hello=你好世界")
@RunWith(SpringRunner.class)
public class MyConfigTest {

    //MyConfig myConfig = DConfBuilder.get(MyConfig.class);
    @Autowired
    MyConfig myConfig;

    @Test
    public void test() {
        String password = myConfig.getPassword();
        System.out.println(password);
        System.out.println(!"admin1!A".matches(password));
    }
}
