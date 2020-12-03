package issac.study.cache.utils;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import issac.study.cache.core.page.PageParam;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.*;

/**
 * @author issac.hu
 */
public class ConvertUtils {
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
     * @param format                   是否格式化
     * @return
     */
    public static String toJsonString(Object object, boolean disableCircularReference, boolean format) {
        List<SerializerFeature> serializerFeatureList = new ArrayList<>();
        if (disableCircularReference) {
            serializerFeatureList.add(SerializerFeature.DisableCircularReferenceDetect);
        }
        if (format) {
            serializerFeatureList.add(SerializerFeature.PrettyFormat);
        }
        if (serializerFeatureList.isEmpty()) {
            return JSON.toJSONString(object);
        } else {
            return JSON.toJSONString(object, serializerFeatureList.toArray(new SerializerFeature[]{}));
        }
    }

    /**
     * 参数转换
     *
     * @param pageable
     * @return
     */
    public static PageParam pageableToPageParam(Pageable pageable) {
        Sort sort = pageable.getSort();
        List<String> sorts = new ArrayList<>();
        for (Sort.Order order : sort) {
            if (order.isAscending()) {
                sorts.add(order.getProperty() + " ASC ");
            } else {
                sorts.add(order.getProperty() + " DESC ");
            }
        }
        PageParam pageParam = new PageParam();
        pageParam.setOffset(pageable.getOffset());
        pageParam.setSize(pageable.getPageSize());
        pageParam.setSorts(sorts);
        return pageParam;
    }
}
