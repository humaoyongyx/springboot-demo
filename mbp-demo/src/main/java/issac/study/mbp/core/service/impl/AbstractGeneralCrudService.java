package issac.study.mbp.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import issac.study.mbp.core.service.GeneralCrudService;
import issac.study.mbp.exception.BusinessRuntimeException;
import issac.study.mbp.model.base.GeneralModel;
import issac.study.mbp.req.base.BasePageReq;
import issac.study.mbp.req.base.BaseReq;
import issac.study.mbp.utils.ConvertUtils;
import issac.study.mbp.utils.ReflectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.*;

/**
 * @author issac.hu
 */
public abstract class AbstractGeneralCrudService<M extends BaseMapper<T>, T extends GeneralModel, V> extends ServiceImpl<M, T> implements GeneralCrudService<T, V> {

    protected Class<T> getTClass() {
        Class<T> tClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[1];
        return tClass;
    }

    protected Class<V> getVClass() {
        Class<V> vClass = (Class<V>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[2];
        return vClass;
    }

    @Override
    public V saveGet(BaseReq baseReq) {
        Objects.requireNonNull(baseReq, "保存的对象不能为空");
        baseReq.setId(null);
        T model = ConvertUtils.convertObject(baseReq, getTClass());
        model.setCreatedTime(new Date());
        model = saveGetCustom(model);
        save(model);
        return ConvertUtils.convertObject(model, getVClass());
    }

    /**
     * 子类可以继承复写
     *
     * @param model
     * @return
     */
    protected T saveGetCustom(T model) {
        return model;
    }

    @Override
    public V updateGet(BaseReq baseReq) {
        return updateGet(baseReq, false);
    }

    @Override
    public V updateGet(BaseReq baseReq, boolean includeNullValue) {
        Objects.requireNonNull(baseReq, "更新的对象不能为空");
        if (baseReq.getId() == null) {
            throw BusinessRuntimeException.error("id不能为空");
        }
        T db = getBaseMapper().selectById(baseReq.getId());
        if (db == null) {
            throw BusinessRuntimeException.error("更新的数据不存在");
        }
        if (includeNullValue) {
            ConvertUtils.copyWithNullProperties(baseReq, db);
        } else {
            ConvertUtils.copyNotNullProperties(baseReq, db);
        }
        getBaseMapper().updateById(db);
        return ConvertUtils.convertObject(db, getVClass());
    }

    @Override
    public Page<V> page(BaseReq baseReq, BasePageReq basePageReq) {
        Page<T> page = new Page<>(basePageReq.getPage(), basePageReq.getSize());
        QueryWrapper<T> queryWrapper = new QueryWrapper<>();
        List<Field> reqFields = ReflectionUtils.getAllFields(baseReq);
        List<Field> modelFields = ReflectionUtils.getAllFields(getTClass());
        Map<String, Field> modelFieldsMap = new HashMap<>();
        for (Field modelField : modelFields) {
            modelFieldsMap.put(modelField.getName(), modelField);
        }
        for (Field reqField : reqFields) {
            String fieldName = reqField.getName();
            if (modelFieldsMap.get(fieldName) == null) {
                continue;
            }
            reqField.setAccessible(true);
            try {
                Object val = reqField.get(baseReq);
                if (val != null) {
                    if (val instanceof String) {
                        queryWrapper.like(StringUtils.isNotBlank((CharSequence) val), fieldName, val);
                    } else if (val instanceof Integer || val instanceof Long || val instanceof Boolean) {
                        queryWrapper.eq(val != null, fieldName, val);
                    }
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        String orderStr = basePageReq.getOrder();
        List<OrderItem> orderItemList = new ArrayList<>();
        if (StringUtils.isNotBlank(orderStr)) {
            String[] items = orderStr.split(";");
            for (String item : items) {
                if (StringUtils.isBlank(item)) {
                    continue;
                }
                String[] fieldOrder = item.split(",");
                if (fieldOrder.length < 2) {
                    continue;
                }
                String field = fieldOrder[0];
                String order = fieldOrder[1];
                if (modelFieldsMap.get(field) == null) {
                    continue;
                }
                if (StringUtils.equalsIgnoreCase("asc", order.trim())) {
                    orderItemList.add(OrderItem.asc(field));
                } else if (StringUtils.equalsIgnoreCase("desc", order.trim())) {
                    orderItemList.add(OrderItem.desc(field));
                }
            }
        }

        if (!orderItemList.isEmpty()) {
            page.setOrders(orderItemList);
        }
        page = page(page, queryWrapper);
        Page<V> newPage = new Page<>(page.getCurrent(), page.getSize(), page.getTotal());
        newPage.setOrders(page.getOrders());
        newPage.setRecords(ConvertUtils.convertList(page.getRecords(), getVClass()));
        return newPage;
    }
}
