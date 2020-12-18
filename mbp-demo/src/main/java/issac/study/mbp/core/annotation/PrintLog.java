package issac.study.mbp.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 打印日志
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface PrintLog {
    /**
     * 接口名称
     *
     * @return
     */
    String value() default "";

    /**
     * 是否展示请求参数
     *
     * @return
     */
    boolean showArgs() default true;

    /**
     * 是否打印结果
     *
     * @return
     */
    boolean showResult() default false;
}
