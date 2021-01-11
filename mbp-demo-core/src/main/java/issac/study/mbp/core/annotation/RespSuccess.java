package issac.study.mbp.core.annotation;

import java.lang.annotation.*;

/**
 * 注解此类的返回值会自动注入 ResponseVo
 *
 * @author issac
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RespSuccess {
}
