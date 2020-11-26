package issac.study.mybatisjpa.core.jpa;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 同步时间的注解
 * 此注解可以注解在字符串上，然后会按照 yyyy-MM-dd或者yyyy-MM-dd HH:mm:ss 解析
 *
 * @author issac.hu
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface SyncDateTime {
    String value() default "updateDateTime";
}
