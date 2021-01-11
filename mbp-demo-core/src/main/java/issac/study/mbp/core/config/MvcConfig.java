package issac.study.mbp.core.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * MvcConfig
 *
 * @author issac.hu
 */
@Configuration
public class MvcConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("doc.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
        registry.addResourceHandler("/favicon.ico").addResourceLocations("classpath:/META-INF/resources/");
    }

    /**
     * 跨域问题 参考文章：<a>https://blog.csdn.net/java_green_hand0909/article/details/78740765</a>
     * <p>
     * 配置参考spring 官方文档 <a>https://docs.spring.io/spring-framework/docs/current/reference/html/web.html#mvc-cors-global</a>
     * By default, global configuration enables the following:
     * <p>
     * All origins.
     * <p>
     * All headers.
     * <p>
     * GET, HEAD, and POST methods.
     * <p>
     * allowCredentials is not enabled by default, since that establishes a trust level that exposes sensitive user-specific information (such as cookies and CSRF tokens) and should only be used where appropriate. When it is enabled either allowOrigins must be set to one or more specific domain (but not the special value "*") or alternatively the allowOriginPatterns property may be used to match to a dynamic set of origins.
     * <p>
     * maxAge is set to 30 minutes.
     *
     * @param registry
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {

        registry.addMapping("/api/**")
                //允许所有方法
                .allowedMethods(CorsConfiguration.ALL)
                //允许获取cookie
                .allowCredentials(true);
    }
}
