package issac.study.mbp.core.support;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

/**
 * @author issac.hu
 */
@Component
public class MyAwareProcessor implements BeanPostProcessor {

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof MyAware) {
            ((MyAware) bean).setString("aware(BeanPostProcessor) before");
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof MyAware) {
            ((MyAware) bean).setString("aware(BeanPostProcessor) after");
        }
        return bean;
    }
}
