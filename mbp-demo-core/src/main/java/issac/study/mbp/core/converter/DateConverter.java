package issac.study.mbp.core.converter;

import issac.study.mbp.core.locale.MessageUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.convert.converter.Converter;

import javax.validation.ValidationException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * controller接收string类型转换为日期类
 * 将标准日期、标准日期时间、时间戳转换成Date类型
 */
public class DateConverter implements Converter<String, Date> {
    private static final String dateFormat = "yyyy-MM-dd HH:mm:ss";
    private static final String shortDateFormat = "yyyy-MM-dd";
    private static final String timeStampFormat = "^\\d+$";

    @Override
    public Date convert(String value) {

        if (StringUtils.isBlank(value)) {
            return null;
        }
        value = value.trim();
        try {
            if (value.contains("-")) {
                SimpleDateFormat formatter;
                if (value.contains(":")) {
                    formatter = new SimpleDateFormat(dateFormat);
                } else {
                    formatter = new SimpleDateFormat(shortDateFormat);
                }
                return formatter.parse(value);
            } else if (value.matches(timeStampFormat)) {
                Long lDate = new Long(value);
                return new Date(lDate);
            }
        } catch (Exception e) {
            throw new ValidationException(MessageUtils.get("core.validate.date"));
        }
        throw new ValidationException(MessageUtils.get("core.validate.date"));
    }
}
