package issac.study.cache.config;

import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
    /**
     * 可以通过变量设置swagger-ui是否显示，比如测试环境可以暴露api文档，生产环境我们就关闭
     */
    @Value("${swagger.enable:true}")
    private boolean enableSwagger;

    @Bean
    public Docket webApiConfig() {

        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("api")
                .enable(enableSwagger)
                .apiInfo(webApiInfo())
                .select()
                .apis(RequestHandlerSelectors.withClassAnnotation(Api.class))
                .paths(PathSelectors.any())
                .build();

    }

    private ApiInfo webApiInfo() {

        return new ApiInfoBuilder()
                .contact(new Contact("issac.hu", "", ""))
                .title("SpringBoot演示")
                .description("SpringBoot演示接口")
                .version("1.0")
                .build();
    }
}