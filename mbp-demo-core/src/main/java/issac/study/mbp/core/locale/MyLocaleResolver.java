package issac.study.mbp.core.locale;


import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;

/**
 * 国际化解析器
 * <p>
 * 解析url参数是否带有lang，如果解析header Accept-Language
 *
 * @author issac.hu
 */
public class MyLocaleResolver implements LocaleResolver {

    public static final String LOCALE = "locale";

    @Override
    public Locale resolveLocale(HttpServletRequest request) {
        String lang = request.getParameter(LOCALE);
        //默认locale中文_大陆
        Locale locale = Locale.CHINA;
        if (StringUtils.isNotBlank(lang)) {
            try {
                locale = org.springframework.util.StringUtils.parseLocale(lang);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            AcceptHeaderLocaleResolver acceptHeaderLocaleResolver = new AcceptHeaderLocaleResolver();
            acceptHeaderLocaleResolver.setDefaultLocale(locale);
            return acceptHeaderLocaleResolver.resolveLocale(request);
        }
        return locale;
    }

    @Override
    public void setLocale(HttpServletRequest request, HttpServletResponse response, Locale locale) {

    }
}
