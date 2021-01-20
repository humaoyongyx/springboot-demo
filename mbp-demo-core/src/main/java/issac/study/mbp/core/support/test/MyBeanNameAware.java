package issac.study.mbp.core.support.test;

import org.springframework.beans.factory.BeanNameAware;
import org.springframework.stereotype.Component;

/**
 * @author issac.hu
 */
@Component
public class MyBeanNameAware implements BeanNameAware {

    @Override
    public void setBeanName(String name) {
        System.out.println(name);
    }
}
