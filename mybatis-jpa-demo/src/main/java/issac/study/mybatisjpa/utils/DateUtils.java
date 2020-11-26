package issac.study.mybatisjpa.utils;

import org.apache.commons.lang3.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * @author issac.hu
 */
public class DateUtils {

    public static final String LONG_DEFAULT_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final DateTimeFormatter LONG_DEFAULT_FORMATTER = DateTimeFormatter.ofPattern(LONG_DEFAULT_FORMAT);

    /**
     * 格式化日期
     *
     * @param dateStr
     * @return
     */
    public static LocalDateTime fmt(String dateStr) {
        if (StringUtils.isBlank(dateStr)) {
            return null;
        }
        String date = dateStr.trim();
        if (LONG_DEFAULT_FORMAT.length() == date.length()) {
            return LocalDateTime.parse(date, LONG_DEFAULT_FORMATTER);
        }
        return null;
    }

    public static LocalDateTime toLocalDateTime(String dateStr) {
        if (StringUtils.isBlank(dateStr)) {
            return null;
        }
        String date = dateStr.trim();
        return LocalDateTime.parse(date, LONG_DEFAULT_FORMATTER);
    }

    public static Date toDate(String dateStr) {
        if (StringUtils.isBlank(dateStr)) {
            return null;
        }
        String date = dateStr.trim();
        Date parse = null;
        try {
            parse = new SimpleDateFormat(LONG_DEFAULT_FORMAT).parse(date);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return parse;
    }

    public static String parse(Date date) {
        return new SimpleDateFormat(LONG_DEFAULT_FORMAT).format(date);
    }

    public static String parse(LocalDateTime date) {
        return LONG_DEFAULT_FORMATTER.format(date);
    }
}

