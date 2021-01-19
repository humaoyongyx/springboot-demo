package issac.study.mbp.core.support.test;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author issac.hu
 */
@Configuration
@Import({MyImportBeanDefinitionRegistrar.class, MyImportSelector.class})
public class TestConfig {
}
