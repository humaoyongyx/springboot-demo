package issac.study.mbp.test;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author issac.hu
 */
@Configuration
public class TestConfiguration {

    @Bean(initMethod = "initMethod",destroyMethod = "destroyMethod")
    public TestService testService() {
        return new TestService();
    }
}
