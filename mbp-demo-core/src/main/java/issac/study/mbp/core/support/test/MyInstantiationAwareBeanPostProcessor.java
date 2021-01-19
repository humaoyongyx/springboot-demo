package issac.study.mbp.core.support.test;

import issac.study.mbp.core.support.MyAware;
import org.springframework.beans.BeansException;
import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor;
import org.springframework.stereotype.Component;

/**
 * 不建议实现此接口
 *
 * @author issac.hu
 */
@Component
public class MyInstantiationAwareBeanPostProcessor implements InstantiationAwareBeanPostProcessor {

    @Override
    public Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName) throws BeansException {
        if (MyAware.class.isAssignableFrom(beanClass)) {
            System.out.println("InstantiationAwareBeanPostProcessor before");
        }
        return null;
    }

    @Override
    public boolean postProcessAfterInstantiation(Object bean, String beanName) throws BeansException {
        if (bean instanceof MyAware) {
            System.out.println("InstantiationAwareBeanPostProcessor after");
            // ((MyAware) bean).setString("InstantiationAwareBeanPostProcessor after"); 与上面等同，仔细分析这个语句，可以得出xxxAware的原理
        }
        return true;
    }

    @Override
    public PropertyValues postProcessProperties(PropertyValues pvs, Object bean, String beanName) throws BeansException {
        if (bean instanceof MyAware) {
            System.out.println("InstantiationAwareBeanPostProcessor Properties");
        }
        return null;
    }
}
