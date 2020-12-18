package issac.study.mbp.core.utils;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.apache.commons.lang3.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * @author issac.hu
 */
public class ConvertUtils {

    private static DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private static SimpleDateFormat getSimpleDateFormat() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 转换pageable为page
     *
     * @param pageable
     * @param <T>
     * @return
     */
    /**
     * 体对象转 optional vo
     *
     * @param entity
     * @param vClass
     * @param <V>
     * @return
     */
    public static <V> Optional<V> toOpVo(Object entity, Class<V> vClass) {
        V value = convertObject(entity, vClass);
        if (value == null) {
            return Optional.empty();
        }
        return Optional.of(value);
    }

    /**
     * 将一个对象转换为另一个类的对象
     *
     * @param object
     * @param tClass
     * @param <T>
     * @return
     */
    public static <T> T convertObject(Object object, Class<T> tClass) {
        if (object == null) {
            return null;
        }
        return JSON.parseObject(toJsonString(object), tClass);
    }

    /**
     * 将json转换为list
     *
     * @param jsonStr
     * @param tClass
     * @param <T>
     * @return
     */
    public static <T> List<T> convertList(String jsonStr, Class<T> tClass) {
        if (jsonStr == null) {
            return null;
        }
        return JSONArray.parseArray(jsonStr, tClass);
    }

    /**
     * 将普通bean转为map
     *
     * @param bean
     * @return
     */
    public static Map<String, Object> toMap(Object bean) {
        if (bean == null) {
            return new HashMap<>();
        }
        JSONObject jsonObject = JSON.parseObject(toJsonString(bean));
        return jsonObject.getInnerMap();
    }

    /**
     * 转换option对象
     *
     * @param object
     * @param tClass
     * @param <T>
     * @return
     */
    public static <T> T convertOptionObject(Object object, Class<T> tClass) {
        if (object != null && object instanceof Optional) {
            if (((Optional<?>) object).isPresent()) {
                return JSON.parseObject(toJsonString(((Optional<?>) object).get()), tClass);
            }
        }
        return null;
    }

    /**
     * 通用转换
     *
     * @param pojoList
     * @param tClass
     * @param <T>
     * @return
     */
    public static <T> List<T> convertList(List<?> pojoList, Class<T> tClass) {
        return JSONArray.parseArray(toJsonString(pojoList), tClass);
    }

    /**
     * copy非空值属性,支持属性的类型不相同
     *
     * @param source
     * @param target
     */
    public static void copyNotNullProperties(Object source, Object target) {
        BeanUtil.copyProperties(source, target, CopyOptions.create().setIgnoreNullValue(true).setIgnoreError(true));
    }

    /**
     * copy属性，包括null
     *
     * @param source
     * @param target
     */
    public static void copyWithNullProperties(Object source, Object target) {
        BeanUtil.copyProperties(source, target, CopyOptions.create().setIgnoreError(true));
    }

    public static String objToString(Object value) {
        if (value == null) {
            return null;
        }
        String str;
        if (value instanceof String) {
            str = (String) value;
        } else if (value instanceof Number || value instanceof Boolean) {
            str = String.valueOf(value);
        } else if (value instanceof Date) {
            str = getSimpleDateFormat().format((Date) value);
        } else if (value instanceof LocalDateTime) {
            str = ((LocalDateTime) value).format(dateTimeFormatter);
        } else {
            str = ConvertUtils.toJsonString(value);
        }
        return str;
    }

    public static <T> T strToObject(String str, Class<T> tClass) {
        if (StringUtils.isBlank(str)) {
            return null;
        }
        if (String.class.isAssignableFrom(tClass)) {
            return (T) str;
        } else if (Byte.class.isAssignableFrom(tClass)) {
            return (T) Byte.valueOf(str);
        } else if (Short.class.isAssignableFrom(tClass)) {
            return (T) Short.valueOf(str);
        } else if (Integer.class.isAssignableFrom(tClass)) {
            return (T) Integer.valueOf(str);
        } else if (Long.class.isAssignableFrom(tClass)) {
            return (T) Long.valueOf(str);
        } else if (Float.class.isAssignableFrom(tClass)) {
            return (T) Float.valueOf(str);
        } else if (Double.class.isAssignableFrom(tClass)) {
            return (T) Double.valueOf(str);
        } else if (Date.class.isAssignableFrom(tClass)) {
            try {
                return (T) getSimpleDateFormat().parse(str);
            } catch (ParseException e) {
                e.printStackTrace();
                return null;
            }
        } else if (LocalDateTime.class.isAssignableFrom(tClass)) {
            return (T) LocalDateTime.parse(str, dateTimeFormatter);
        } else {
            return ConvertUtils.convertObject(str, tClass);
        }
    }

    public static String toJsonString(Object object) {
        return toJsonString(object, false);
    }

    /**
     * 转换为json字符串
     *
     * @param object
     * @param format 是否格式化
     * @return
     */
    public static String toJsonString(Object object, boolean format) {
        return toJsonString(object, true, format);
    }

    /**
     * 转换为json字符串
     *
     * @param object
     * @param disableCircularReference 是否开启循环引用
     * @param formatOutput             是否格式化
     * @return
     */
    public static String toJsonString(Object object, boolean disableCircularReference, boolean formatOutput) {
        return toJsonString(object, disableCircularReference, formatOutput, false);
    }

    /**
     * 转换为json字符串
     *
     * @param object
     * @param disableCircularReference
     * @param formatOutput
     * @param dateFormat
     * @return
     */
    public static String toJsonString(Object object, boolean disableCircularReference, boolean formatOutput, boolean dateFormat) {
        List<SerializerFeature> serializerFeatureList = new ArrayList<>();
        if (disableCircularReference) {
            serializerFeatureList.add(SerializerFeature.DisableCircularReferenceDetect);
        }
        if (formatOutput) {
            serializerFeatureList.add(SerializerFeature.PrettyFormat);
        }
        if (dateFormat) {
            serializerFeatureList.add(SerializerFeature.WriteDateUseDateFormat);
        }
        if (serializerFeatureList.isEmpty()) {
            return toJsonString(object);
        } else {
            return toJsonString(object, serializerFeatureList.toArray(new SerializerFeature[]{}));
        }
    }

    /**
     * 使用fastjson
     *
     * @param object
     * @param serializerFeatures
     * @return
     */
    private static String toJsonString(Object object, SerializerFeature... serializerFeatures) {
        return JSON.toJSONString(object, serializerFeatures);
    }


}
