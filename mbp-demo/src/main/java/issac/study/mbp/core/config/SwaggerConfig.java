package issac.study.mbp.core.config;

import com.fasterxml.classmate.TypeResolver;
import issac.study.mbp.core.response.ResponseResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

@Configuration
@EnableSwagger2WebMvc
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
                .additionalModels(new TypeResolver().resolve(ResponseResult.class))
                .apiInfo(webApiInfo())
                //分组名称
                .groupName("api v1")
                .select()
                //这里指定Controller扫描包路径
                .apis(RequestHandlerSelectors.any())
                .paths(path -> path != null && (path.startsWith("/api/")))
                .build();

    }

    private ApiInfo webApiInfo() {
        return new ApiInfoBuilder()
                .contact(new Contact("issac.hu", "", ""))
                .title("Swagger演示")
                .description("Swagger演示")
                .version("1.0")
                .build();
    }
}