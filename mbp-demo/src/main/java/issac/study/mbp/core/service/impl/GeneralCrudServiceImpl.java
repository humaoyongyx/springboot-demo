package issac.study.mbp.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.baomidou.mybatisplus.core.toolkit.ReflectionKit;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import issac.study.mbp.core.exception.BusinessRuntimeException;
import issac.study.mbp.core.model.GeneralModel;
import issac.study.mbp.core.req.BasePageReq;
import issac.study.mbp.core.req.BaseReq;
import issac.study.mbp.core.service.GeneralCrudService;
import issac.study.mbp.core.utils.ConvertUtils;
import issac.study.mbp.core.utils.ReflectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.util.*;

/**
 * @author issac.hu
 */
public class GeneralCrudServiceImpl<M extends BaseMapper<T>, T extends GeneralModel, V> extends ServiceImpl<M, T> implements GeneralCrudService<T, V> {

    /**
     * 当前值对象的类
     */
    protected Class<V> voClass = currentVoClass();

    /**
     * 获取实体表名称
     *
     * @return
     */
    protected String currentTableName() {
        TableInfo tableInfo = TableInfoHelper.getTableInfo(this.entityClass);
        Assert.notNull(tableInfo, "error: can not execute. because can not find cache of TableInfo for entity!");
        return tableInfo.getTableName();
    }

    /**
     * 获取值对象的类
     *
     * @return
     */
    protected Class<V> currentVoClass() {
        return (Class<V>) ReflectionKit.getSuperClassGenericType(getClass(), 2);
    }

    @Override
    public T saveModel(T model) {
        model.setId(null);
        model = commonSave(model);
        super.save(model);
        return model;
    }

    @Override
    public V saveGet(BaseReq baseReq) {
        Objects.requireNonNull(baseReq, "保存的对象不能为空");
        baseReq.setId(null);
        T model = ConvertUtils.convertObject(baseReq, getEntityClass());
        model = commonSave(model);
        super.save(model);
        return ConvertUtils.convertObject(model, voClass);
    }


    private T commonSave(T model) {
        Date time = new Date();
        model.setCreatedTime(time);
        model.setUpdatedTime(time);
        model = saveCustom(model);
        return model;
    }

    /**
     * 子类可以继承覆写
     *
     * @param model
     * @return
     */
    protected T saveCustom(T model) {
        return model;
    }

    private T commonUpdate(T model) {
        model.setUpdatedTime(new Date());
        model = updateCustom(model);
        return model;
    }

    /**
     * 子类继承覆写
     *
     * @param model
     * @return
     */
    protected T updateCustom(T model) {
        return model;
    }


    @Override
    public T updateModel(T model) {
        return updateModel(model, false);
    }

    @Override
    public T updateModel(T model, boolean includeNullValue) {
        T db = getBaseMapper().selectById(model.getId());
        if (includeNullValue) {
            ConvertUtils.copyWithNullProperties(model, db);
        } else {
            ConvertUtils.copyNotNullProperties(model, db);
        }
        db = commonUpdate(db);
        getBaseMapper().updateById(db);
        return db;
    }

    @Override
    public V updateGet(BaseReq baseReq) {
        return updateGet(baseReq, false);
    }

    @Override
    public V updateGet(BaseReq baseReq, boolean includeNullValue) {
        T db = checkReqForUpdate(baseReq, includeNullValue);
        db = commonUpdate(db);
        getBaseMapper().updateById(db);
        return ConvertUtils.convertObject(db, voClass);
    }

    protected T checkReqForUpdate(BaseReq baseReq, boolean includeNullValue) {
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
        return db;
    }

    @Override
    public Page<V> page(BaseReq baseReq, BasePageReq basePageReq) {
        Page<T> page = new Page<>(basePageReq.getPage(), basePageReq.getSize());
        QueryWrapper<T> queryWrapper = new QueryWrapper<>();
        List<Field> reqFields = ReflectionUtils.getAllFields(baseReq);
        List<Field> modelFields = ReflectionUtils.getAllFields(getEntityClass());
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
            String column = com.baomidou.mybatisplus.core.toolkit.StringUtils.camelToUnderline(fieldName);
            try {
                Object val = reqField.get(baseReq);
                if (val != null) {
                    if (val instanceof String) {
                        queryWrapper.like(StringUtils.isNotBlank((CharSequence) val), column, val);
                    } else if (val instanceof Integer || val instanceof Long || val instanceof Boolean) {
                        queryWrapper.eq(val != null, column, val);
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
                String[] columnOrder = item.split(",");
                if (columnOrder.length < 2) {
                    continue;
                }
                String column = columnOrder[0].trim();
                String order = columnOrder[1].trim();
                if (modelFieldsMap.get(column) == null) {
                    continue;
                }
                if (StringUtils.equalsIgnoreCase("asc", order)) {
                    orderItemList.add(OrderItem.asc(column));
                } else if (StringUtils.equalsIgnoreCase("desc", order)) {
                    orderItemList.add(OrderItem.desc(column));
                }
            }
        }

        if (!orderItemList.isEmpty()) {
            page.setOrders(orderItemList);
        }
        page = page(page, queryWrapper);
        Page<V> newPage = new Page<>(page.getCurrent(), page.getSize(), page.getTotal());
        newPage.setOrders(page.getOrders());
        newPage.setRecords(ConvertUtils.convertList(page.getRecords(), voClass));
        return newPage;
    }


}
