package issac.study.mbp.core.builder;

import lombok.Data;
import org.springframework.beans.factory.FactoryBean;

/**
 * @author issac.hu
 */
@Data
public class DConfFactory<T> implements FactoryBean<T> {

    private Class<T> targetClass;

    @Override
    public T getObject() throws Exception {
        return DConfBuilder.getFromDb(targetClass);
    }

    @Override
    public Class<?> getObjectType() {
        return targetClass;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}
