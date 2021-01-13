package issac.study.mbp.core.config;

import issac.study.mbp.core.locale.MessageUtils;
import issac.study.mbp.core.locale.MyLocaleResolver;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.LocaleResolver;

import javax.validation.Validator;

/**
 * @author issac.hu
 */
@Configuration
public class LocaleConfig {

    @Bean
    public LocaleResolver localeResolver() {
        return new MyLocaleResolver();
    }

    /**
     * 查找messages的顺序是
     * messages_language_country
     * messages_language
     * messages
     *
     * @return
     */
    @Bean
    MessageSource messageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        //这里是有读取顺序的,也即类路径下的ValidationMessages最新读取，i18n/core/messages最后读取
        //ValidationMessages.properties是Validator默认的basename
        messageSource.setBasenames("ValidationMessages", "messages", "i18n/messages", "i18n/core/messages");
        //如果不设置文件编码为ISO-8859-1 中文需要转为Unicode
        messageSource.setDefaultEncoding("UTF-8");
        //不设置这个,如果没有code会默认抛出异常，而是直接返回key的名称，但是想使用默认的hibernate的ValidationMessages 这里不能设置为true
        //messageSource.setUseCodeAsDefaultMessage(true);
        return messageSource;
    }

    @Bean
    MessageUtils messageUtils(MessageSource messageSource) {
        return new MessageUtils(messageSource);
    }

    /**
     * 注意这里是 javax.validation包下面的 Validator
     *
     * @param messageSource
     * @return
     */
    @Bean
    Validator validator(MessageSource messageSource) {
        LocalValidatorFactoryBean factoryBean = new LocalValidatorFactoryBean();
        //设置MessageSource
        factoryBean.setValidationMessageSource(messageSource);
        return factoryBean;
    }

}
