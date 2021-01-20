package issac.study.mbp.core.support.test;

import issac.study.mbp.core.support.MyAware;
import org.springframework.beans.factory.support.MergedBeanDefinitionPostProcessor;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.stereotype.Component;

/**
 * @author issac.hu
 */
@Component
public class MyMergedBeanDefinitionPostProcessor implements MergedBeanDefinitionPostProcessor {

    @Override
    public void postProcessMergedBeanDefinition(RootBeanDefinition beanDefinition, Class<?> beanType, String beanName) {
        if (MyAware.class.isAssignableFrom(beanType)) {
            System.out.println("MergedBeanDefinitionPostProcessor");
        }
    }
}
