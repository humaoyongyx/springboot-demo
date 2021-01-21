package issac.study.mbp.core.locale;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

/**
 * @author issac.hu
 */
public class MessageUtils {

    private static MessageSource messageSource;

    public MessageUtils(MessageSource messageSource) {
        MessageUtils.messageSource = messageSource;
    }

    /**
     * 获取国际化message
     *
     * @param key
     * @return
     */
    public static String get(String key) {
        return get(key, null);
    }

    /**
     * 获取国际化message
     *
     * @param key
     * @param args 格式化占位符
     * @return
     */
    public static String get(String key, Object... args) {
        return messageSource.getMessage(key, args, LocaleContextHolder.getLocale());
    }

    /**
     * 获取不到message就返回默认值
     *
     * @param key
     * @param args
     * @return
     */
    public static String getOrElseReturnKey(String key, Object... args) {
        try {
            return get(key, args);
        } catch (Exception e) {
            return key;
        }
    }
}
