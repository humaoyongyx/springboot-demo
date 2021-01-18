package issac.study.mbp.core.annotation;

import issac.study.mbp.core.support.DynamicConfigRegistrar;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author issac.hu
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Inherited
@Import(DynamicConfigRegistrar.class)
public @interface EnableDConf {
    /**
     * Alias for the {@link #basePackages()} attribute. Allows for more concise annotation declarations e.g.:
     * {@code @EnableJpaRepositories("org.my.pkg")} instead of {@code @EnableJpaRepositories(basePackages="org.my.pkg")}.
     */
    String[] value() default {};

    /**
     * 扫描路径
     *
     * @return
     */
    String[] basePackages() default {};
}
