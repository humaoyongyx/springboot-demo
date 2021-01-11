package issac.study.mbp.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author issac.hu
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ConfigKey {
    /**
     * key 值
     *
     * @return
     */
    String value();

    /**
     * 描述
     *
     * @return
     */
    String desc() default "";
}
