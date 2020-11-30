package issac.study.mybatisjpa.core.generator;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentityGenerator;

import java.io.Serializable;

/**
 * 自定义id生成器
 * <p>
 * 如果设置了id子则不再自动生成，否则使用自动生成策略
 */
public class IdGenerator extends IdentityGenerator {
    @Override
    public Serializable generate(SharedSessionContractImplementor s, Object obj) throws HibernateException {
        Serializable id = s.getEntityPersister(null, obj).getClassMetadata().getIdentifier(obj, s);
        if (id != null) {
            return id;
        } else {
            return super.generate(s, obj);
        }
    }
}