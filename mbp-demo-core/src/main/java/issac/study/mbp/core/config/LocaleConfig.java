package issac.study.mbp.core.config;

import issac.study.mbp.core.locale.MessageUtils;
import issac.study.mbp.core.locale.MyLocaleResolver;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.web.servlet.LocaleResolver;

/**
 * @author issac.hu
 */
@Configuration
public class LocaleConfig {

    @Bean
    public LocaleResolver localeResolver() {
        return new MyLocaleResolver();
    }

    @Bean
    MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        //这里是有读取顺序的,也即类路径下的messages最新读取，core最后读取
        messageSource.setBasenames("classpath:messages", "classpath:i18n/messages", "classpath:i18n/core/messages");
        messageSource.setDefaultEncoding("UTF-8");
        //不设置这个默认抛出异常，而是直接返回key的名称
        messageSource.setUseCodeAsDefaultMessage(true);
        return messageSource;
    }

    @Bean
    MessageUtils messageUtils(MessageSource messageSource) {
        return new MessageUtils(messageSource);
    }


}
