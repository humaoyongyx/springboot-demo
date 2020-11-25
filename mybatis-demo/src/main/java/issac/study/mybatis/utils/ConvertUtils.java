package issac.study.mybatis.utils;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.lang.reflect.Field;
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
    public static <T> Page<T> pageableToPage(Pageable pageable) {
        Page<T> page = new Page<>(pageable.getPageNumber() + 1, pageable.getPageSize());
        Sort sort = pageable.getSort();
        List<OrderItem> orderItemList = new ArrayList<>();
        for (Sort.Order order : sort) {
            OrderItem orderItem;
            if (order.isAscending()) {
                orderItem = OrderItem.asc(order.getProperty());
            } else {
                orderItem = OrderItem.desc(order.getProperty());
            }
            orderItemList.add(orderItem);
        }
        if (!orderItemList.isEmpty()) {
            page.setOrders(orderItemList);
        }
        return page;
    }

    /**
     * 体对象转 optional vo
     *
     * @param entity
     * @param vClass
     * @param <V>
     * @return
     */
    public static <V> Optional<V> toOpVo(Object entity, Class<V> vClass) {
        return Optional.of(convertObject(entity, vClass));
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
        return JSON.parseObject(toJsonString(object), tClass);
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

    private static String toJsonString(Object object) {
        return toJsonString(object, true);
    }

    /**
     * 转换为json字符串
     *
     * @param object
     * @param disableCircularReference 是否开启循环引用
     * @return
     */
    private static String toJsonString(Object object, boolean disableCircularReference) {
        if (disableCircularReference) {
            return JSON.toJSONString(object, SerializerFeature.DisableCircularReferenceDetect);
        } else {
            return JSON.toJSONString(object);
        }
    }

    public static <E> QueryWrapper<E> convertReqToQueryWrapper(Object req) {
        return convertReqToQueryWrapper(req, null);
    }

    /**
     * 请求转换为QueryWrapper
     *
     * @param req
     * @param eClass
     * @param <E>
     * @return
     */
    public static <E> QueryWrapper<E> convertReqToQueryWrapper(Object req, Class<E> eClass) {
        QueryWrapper<E> queryWrapper = new QueryWrapper<>();
        List<Field> reqFields = ReflectionUtils.getAllFields(req);
        List<Field> resultReqFields = new ArrayList<>();
        if (eClass == null || req.getClass().equals(eClass)) {
            resultReqFields = reqFields;
        } else {
            Map<String, Field> reqFieldMap = new HashMap<>();
            for (Field reqField : reqFields) {
                reqFieldMap.put(reqField.getName(), reqField);
            }
            List<Field> eClassFields = ReflectionUtils.getAllFields(eClass);

            for (Field eClassField : eClassFields) {
                Field reqField = reqFieldMap.get(eClassField.getName());
                if (reqField != null) {
                    resultReqFields.add(reqField);
                }
            }
        }
        for (Field resultReqField : resultReqFields) {
            resultReqField.setAccessible(true);
            String name = com.baomidou.mybatisplus.core.toolkit.StringUtils.camelToUnderline(resultReqField.getName());
            try {
                Object value = resultReqField.get(req);
                if (value != null) {
                    if (value instanceof String) {
                        if (StringUtils.isNoneBlank((CharSequence) value)) {
                            queryWrapper.like(name, value);
                        }
                    } else if (value instanceof Number || value instanceof Boolean) {
                        queryWrapper.eq(name, value);
                    } else if (value instanceof Collection) {
                        if (!((Collection<?>) value).isEmpty()) {
                            queryWrapper.in(name, (Collection) value);
                        }
                    }
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return queryWrapper;
    }

    public static <T> PageImpl<T> toJpaPage(Page page, Pageable pageable, Class<T> tClass) {
        return new PageImpl<>(convertList(page.getRecords(), tClass), pageable, page.getTotal());
    }

}
