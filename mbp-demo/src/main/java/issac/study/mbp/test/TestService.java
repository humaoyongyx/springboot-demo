package issac.study.mbp.test;

import issac.study.mbp.core.support.MyAware;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * @author issac.hu
 */
public class TestService implements MyAware, InitializingBean {

    private BeanFactory beanFactory;

    public TestService() {
        System.out.println("constructor");
    }

    @Autowired
    public void setBeanFactory(BeanFactory beanFactory) {
        System.out.println("property(populate bean)");
        this.beanFactory = beanFactory;
    }

    public void initMethod() {
        System.out.println("initMethod");
    }

    public void destroyMethod() {
        System.out.println("destroyMethod");
    }

    @PostConstruct
    public void init() {
        System.out.println("@PostConstruct");
    }

    @PreDestroy
    public void destroy() {
        System.out.println("@PreDestroy");
    }

    @Override
    public void setString(String value) {
        System.out.println(value);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("afterPropertiesSet");
    }
}
