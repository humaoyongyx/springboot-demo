package issac.study.mbp.core.annotation;

import java.lang.annotation.*;

/**
 * @author issac.hu
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface DConf {
    String value() default "";
}
