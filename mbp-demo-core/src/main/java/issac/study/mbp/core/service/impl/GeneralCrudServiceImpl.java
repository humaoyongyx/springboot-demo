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
import issac.study.mbp.core.annotation.TimeBegin;
import issac.study.mbp.core.annotation.TimeEnd;
import issac.study.mbp.core.exception.BusinessRuntimeException;
import issac.study.mbp.core.exception.CoreErrorCode;
import issac.study.mbp.core.locale.MessageUtils;
import issac.study.mbp.core.model.GeneralModel;
import issac.study.mbp.core.req.BasePageReq;
import issac.study.mbp.core.req.BaseReq;
import issac.study.mbp.core.service.GeneralCrudService;
import issac.study.mbp.core.utils.ConvertUtils;
import issac.study.mbp.core.utils.ReflectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;

import javax.validation.ConstraintViolation;
import javax.validation.ValidationException;
import javax.validation.Validator;
import java.lang.reflect.Field;
import java.util.*;

/**
 * @author issac.hu
 */
public class GeneralCrudServiceImpl<M extends BaseMapper<T>, T extends GeneralModel, V> extends ServiceImpl<M, T> implements GeneralCrudService<T, V> {

    @Autowired
    Validator validator;

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
        model = commonSave(model);
        super.save(model);
        return model;
    }

    @CachePut(value = "mbp:cache", key = "targetClass+''+#result.id", condition = "target.cacheable()")
    @Override
    public V save(BaseReq baseReq) {
        Objects.requireNonNull(baseReq, "保存的对象不能为空");
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
    public T updateModel(T model, boolean includeEmptyValue) {
        T db = getBaseMapper().selectById(model.getId());
        if (includeEmptyValue) {
            ConvertUtils.copyProperties(model, db);
        } else {
            ConvertUtils.copyNotEmptyProperties(model, db);
        }
        db = commonUpdate(db);
        getBaseMapper().updateById(db);
        return db;
    }

    @Override
    public V update(BaseReq baseReq) {
        return update(baseReq, false);
    }

    @CachePut(value = "mbp:cache", key = "targetClass+''+#result.id", condition = "target.cacheable()")
    @Override
    public V update(BaseReq baseReq, boolean includeEmptyValue) {
        T db = checkReqForUpdate(baseReq, includeEmptyValue);
        db = commonUpdate(db);
        getBaseMapper().updateById(db);
        return ConvertUtils.convertObject(db, voClass);
    }

    protected T checkReqForUpdate(BaseReq baseReq, boolean includeEmptyValue) {
        Objects.requireNonNull(baseReq, "更新的对象不能为空");
        if (baseReq.getId() == null) {
            throw BusinessRuntimeException.error("id不能为空");
        }
        T db = getBaseMapper().selectById(baseReq.getId());
        if (db == null) {
            throw BusinessRuntimeException.error("更新的数据不存在");
        }
        if (includeEmptyValue) {
            ConvertUtils.copyProperties(baseReq, db);
        } else {
            ConvertUtils.copyNotEmptyProperties(baseReq, db);
        }
        return db;
    }

    @Override
    public Page<V> page(BaseReq baseReq, BasePageReq basePageReq) {

        QueryWrapper<T> queryWrapper = new QueryWrapper<>();
        handleQueryWrapper(baseReq, queryWrapper, getEntityClass());

        Page<T> page = convertPageReq(basePageReq);
        page = page(page, queryWrapper);
        Page<V> newPage = new Page<>(page.getCurrent(), page.getSize(), page.getTotal());
        newPage.setOrders(page.getOrders());
        newPage.setRecords(ConvertUtils.convertList(page.getRecords(), voClass));
        return newPage;
    }

    public Page<T> convertPageReq(BasePageReq basePageReq) {
        Page<T> page = new Page<>(basePageReq.getPage(), basePageReq.getSize());
        String orderStr = basePageReq.getOrder();
        List<OrderItem> orderItemList = new ArrayList<>();
        Map<String, Field> modelFieldsMap = getFieldMap(getEntityClass());
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
                String fieldName = fieldOrder[0].trim();
                String order = fieldOrder[1].trim();
                if (modelFieldsMap.get(fieldName) == null) {
                    continue;
                }
                String column = getColumn(fieldName);
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
        return page;
    }

    private String getColumn(String fieldName) {
        return com.baomidou.mybatisplus.core.toolkit.StringUtils.camelToUnderline(fieldName);
    }

    public void handleQueryWrapper(BaseReq baseReq, QueryWrapper<T> queryWrapper, Class modelClass, String... excludeFields) {
        List<Field> reqFields = ReflectionUtils.getAllFields(baseReq);
        Map<String, Field> modelFieldsMap = getFieldMap(modelClass);
        for (Field reqField : reqFields) {
            reqField.setAccessible(true);
            String fieldName = reqField.getName();
            if (excludeFields != null && Arrays.asList(excludeFields).contains(fieldName)) {
                continue;
            }
            if (modelFieldsMap.get(fieldName) == null && !checkFieldAnnotation(reqField)) {
                continue;
            }
            String column = getColumn(fieldName);
            try {
                Object val = reqField.get(baseReq);
                if (val != null) {
                    if (val instanceof String) {
                        queryWrapper.like(StringUtils.isNotBlank((CharSequence) val), column, val);
                    } else if (val instanceof Integer || val instanceof Long || val instanceof Boolean) {
                        queryWrapper.eq(val != null, column, val);
                    } else if (val instanceof Date) {
                        TimeBegin timeBegin = reqField.getAnnotation(TimeBegin.class);
                        TimeEnd timeEnd = reqField.getAnnotation(TimeEnd.class);
                        if (timeBegin != null && StringUtils.isNotBlank(timeBegin.value())) {
                            queryWrapper.ge(getColumn(timeBegin.value()), val);
                        } else if (timeEnd != null && StringUtils.isNotBlank(timeEnd.value())) {
                            queryWrapper.lt(getColumn(timeEnd.value()), val);
                        }
                    }
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean checkFieldAnnotation(Field reqField) {
        TimeBegin timeBegin = reqField.getAnnotation(TimeBegin.class);
        TimeEnd timeEnd = reqField.getAnnotation(TimeEnd.class);
        if (timeBegin != null || timeEnd != null) {
            return true;
        }
        return false;
    }

    public Map<String, Field> getFieldMap(Class clazz) {
        List<Field> modelFields = ReflectionUtils.getAllFields(clazz);
        Map<String, Field> modelFieldsMap = new HashMap<>();
        for (Field modelField : modelFields) {
            modelFieldsMap.put(modelField.getName(), modelField);
        }
        return modelFieldsMap;
    }

    @Cacheable(value = "mbp:cache", key = "targetClass+''+#id", condition = "target.cacheable()")
    @Override
    public V getById(Integer id) {
        T model = super.getById(id);
        return ConvertUtils.convertObject(model, voClass);
    }

    @CacheEvict(value = "mbp:cache", key = "targetClass+''+#id", condition = "target.cacheable()")
    @Override
    public Integer deleteById(Integer id) {
        if (super.removeById(id)) {
            return id;
        }
        return null;
    }

    @Override
    public void validateReq(Object req) {
        validateReq(req, true);
    }

    @Override
    public void validateReq(Object req, boolean checkNone) {
        validateReqs(Arrays.asList(req), checkNone);
    }

    @Override
    public void validateReqs(List<Object> reqs, boolean checkNone) {
        if (checkNone) {
            if (reqs == null) {
                throw BusinessRuntimeException.errorCode(CoreErrorCode.CORE_PARAM_NULL_ERROR);
            }
        }
        for (Object req : reqs) {
            Set<ConstraintViolation<Object>> validate = validator.validate(req);
            if (!validate.isEmpty()) {
                ConstraintViolation<Object> next = validate.iterator().next();
                String msg = MessageUtils.get("core.validate.bind.exception", next.getPropertyPath(), next.getInvalidValue(), next.getMessage());
                throw new ValidationException(msg);
            }
        }
    }


    @Override
    public boolean cacheable() {
        return false;
    }
}
