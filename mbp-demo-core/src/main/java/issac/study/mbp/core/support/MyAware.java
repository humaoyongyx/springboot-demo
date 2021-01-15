package issac.study.mbp.core.support;

import org.springframework.beans.factory.Aware;

/**
 * @author issac.hu
 */
public interface MyAware extends Aware {
    /**
     * 测试
     *
     * @param value
     */
    void setString(String value);
}
