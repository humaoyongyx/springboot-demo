package issac.study.mbp.core.locale;


import org.springframework.util.StringUtils;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;

/**
 * 国际化解析器
 * <p>
 * 解析url参数是否带有locale，解析header Accept-Language
 *
 * @author issac.hu
 */
public class MyLocaleResolver implements LocaleResolver {

    public static final String LOCALE = "locale";

    @Override
    public Locale resolveLocale(HttpServletRequest request) {
        String lang = request.getParameter(LOCALE);
        //默认locale中文_大陆
        if (StringUtils.hasText(lang)) {
            try {
                Locale locale = StringUtils.parseLocale(lang);
                return locale;
            } catch (Exception e) {

            }
        }
        AcceptHeaderLocaleResolver acceptHeaderLocaleResolver = new AcceptHeaderLocaleResolver();
        //如果解析不出，最终的默认locale为Locale.CHINA
        acceptHeaderLocaleResolver.setDefaultLocale(Locale.CHINA);
        return acceptHeaderLocaleResolver.resolveLocale(request);
    }

    @Override
    public void setLocale(HttpServletRequest request, HttpServletResponse response, Locale locale) {

    }
}
