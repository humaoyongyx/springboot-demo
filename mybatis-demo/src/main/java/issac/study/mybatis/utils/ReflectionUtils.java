package issac.study.mybatis.utils;

import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author issac.hu
 */
public class ReflectionUtils {

    /**
     * 获取对象的所有字段，包括父类
     *
     * @param o
     * @return
     */
    public static List<Field> getAllFields(Object o) {
        List<Field> allFields = new ArrayList<>();
        Class tempReqClass = o.getClass();
        while (tempReqClass != null && Object.class != tempReqClass) {
            allFields.addAll(Arrays.asList(tempReqClass.getDeclaredFields()));
            tempReqClass = tempReqClass.getSuperclass();
        }
        return allFields;
    }

    /**
     * 获取类的所有字段，包括父类
     *
     * @param clazz
     * @return
     */
    public static List<Field> getAllFields(Class<?> clazz) {
        List<Field> allFields = new ArrayList<>();
        Class tempReqClass = clazz;
        while (tempReqClass != null && Object.class != tempReqClass) {
            allFields.addAll(Arrays.asList(tempReqClass.getDeclaredFields()));
            tempReqClass = tempReqClass.getSuperclass();
        }
        return allFields;
    }

    /**
     * 设置拷贝的对象的特定字段的值为null
     *
     * @param source
     * @param target
     * @param overrideFields
     */
    public static List<String> setNullValueForOverrideFields(Object source, Object target, String[] overrideFields) {
        if (overrideFields == null || overrideFields.length == 0) {
            return null;
        }
        List<String> fields = Arrays.asList(overrideFields);
        List<String> setNullFields = new ArrayList<>();
        List<String> resultSetNullFields = new ArrayList<>();
        List<Field> allFields = getAllFields(source);
        for (Field sourceField : allFields) {
            String sourceFieldName = sourceField.getName();
            if (fields.contains(sourceFieldName)) {
                sourceField.setAccessible(true);
                try {
                    Object fieldVal = sourceField.get(source);
                    if (fieldVal instanceof String) {
                        if (StringUtils.isBlank((String) fieldVal)) {
                            setNullFields.add(sourceFieldName);
                        }
                    } else {
                        if (fieldVal == null) {
                            setNullFields.add(sourceFieldName);
                        }
                    }
                } catch (IllegalAccessException e) {
                }
            }
        }

        if (setNullFields.isEmpty()) {
            return null;
        }
        List<Field> targetFields = getAllFields(target);
        for (Field targetField : targetFields) {
            String targetFieldName = targetField.getName();
            if (setNullFields.contains(targetFieldName)) {
                targetField.setAccessible(true);
                try {
                    targetField.set(target, null);
                    resultSetNullFields.add(targetFieldName);
                } catch (IllegalAccessException e) {
                }
            }
        }
        return resultSetNullFields;
    }

}
