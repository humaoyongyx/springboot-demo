//package issac.study.mbp.core.service.temp;
//
//import com.baomidou.mybatisplus.core.conditions.AbstractWrapper;
//import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
//import com.baomidou.mybatisplus.core.mapper.BaseMapper;
//import com.baomidou.mybatisplus.core.metadata.IPage;
//import com.baomidou.mybatisplus.core.metadata.OrderItem;
//import com.baomidou.mybatisplus.core.metadata.TableInfo;
//import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
//import com.baomidou.mybatisplus.core.toolkit.Assert;
//import com.baomidou.mybatisplus.core.toolkit.ReflectionKit;
//import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
//import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
//import com.cbim.cube.annotation.*;
//import com.cbim.cube.common.exception.ExceptionEnum;
//import com.cbim.cube.common.exception.ServiceException;
//import com.cbim.cube.common.req.BasePageReq;
//import com.cbim.cube.common.req.BaseReq;
//import com.cbim.cube.constant.QueryEnum;
//import com.cbim.cube.model.base.GeneralModel;
//import com.cbim.cube.util.ConvertUtils;
//import com.cbim.cube.util.MapUtils;
//import com.cbim.cube.util.ReflectionUtils;
//import com.github.yulichang.base.MPJBaseMapper;
//import com.github.yulichang.query.MPJQueryWrapper;
//import com.github.yulichang.wrapper.MPJLambdaWrapper;
//import org.apache.commons.lang3.StringUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//
//import javax.validation.ConstraintViolation;
//import javax.validation.ValidationException;
//import javax.validation.Validator;
//import java.lang.reflect.Field;
//import java.util.*;
//import java.util.function.Consumer;
//import java.util.stream.Collectors;
//
///**
// * @author issac.hu
// */
//public class GeneralCrudServiceImpl<M extends BaseMapper<T>, T extends GeneralModel, V> extends ServiceImpl<M, T> implements GeneralCrudService<T, V> {
//
//    private final Consumer<List<V>> pageNullConsumer = (v) -> {
//    };
//
//    protected Class<T> entityClass = this.currentModelClass();
//
//    public Class<T> getEntityClass() {
//        return this.entityClass;
//    }
//
//    @Autowired
//    Validator validator;
//
//    /**
//     * 当前值对象的类
//     */
//    protected Class<V> voClass = currentVoClass();
//
//    /**
//     * 获取实体表名称
//     *
//     * @return
//     */
//    protected String currentTableName() {
//        TableInfo tableInfo = TableInfoHelper.getTableInfo(this.entityClass);
//        Assert.notNull(tableInfo, "error: can not execute. because can not find cache of TableInfo for entity!");
//        return tableInfo.getTableName();
//    }
//
//    /**
//     * 获取值对象的类
//     *
//     * @return
//     */
//    protected Class<V> currentVoClass() {
//        return (Class<V>) ReflectionKit.getSuperClassGenericType(getClass(), 2);
//    }
//
//    @Override
//    public T saveModel(T model) {
//        model = commonSave(model);
//        super.save(model);
//        return model;
//    }
//
//    @Override
//    public void saveModelAll(List<T> modelList) {
//        for (T model : modelList) {
//            commonSave(model);
//        }
//        super.saveBatch(modelList);
//    }
//
//    @Override
//    public V save(BaseReq baseReq) {
//        Objects.requireNonNull(baseReq, "保存的对象不能为空");
//        T model = ConvertUtils.toBean(baseReq, getEntityClass());
//        model = commonSave(model);
//        super.save(model);
//        return ConvertUtils.toBean(model, voClass);
//    }
//
//
//    private T commonSave(T model) {
//        model = fillValue(model, DML.INSERT);
//        model = saveCustom(model);
//        return model;
//    }
//
//    private T fillValue(T model, DML dml) {
//        MapUtils mapUtils = MapUtils.build(model);
//        boolean needFillValue = false;
//        if (DML.INSERT.equals(dml)) {
//            if (mapUtils.containsKeyWithNull(CREATE_TIME)) {
//                mapUtils.put(CREATE_TIME, new Date());
//                needFillValue = true;
//            }
//        }
//        if (DML.INSERT.equals(dml) || DML.UPDATE.equals(dml)) {
//            if (mapUtils.containsKeyWithNull(UPDATE_TIME)) {
//                mapUtils.put(UPDATE_TIME, new Date());
//                needFillValue = true;
//            }
//        }
//        if (needFillValue) {
//            model = mapUtils.toBean(getEntityClass());
//        }
//        return model;
//    }
//
//    /**
//     * 子类可以继承覆写
//     *
//     * @param model
//     * @return
//     */
//    protected T saveCustom(T model) {
//        return model;
//    }
//
//    private T commonUpdate(T model) {
//        model = fillValue(model, DML.UPDATE);
//        model = updateCustom(model);
//        return model;
//    }
//
//    /**
//     * 子类继承覆写
//     *
//     * @param model
//     * @return
//     */
//    protected T updateCustom(T model) {
//        return model;
//    }
//
//
//    @Override
//    public T updateModel(T model) {
//        return updateModel(model, false);
//    }
//
//    @Override
//    public T updateModel(T model, boolean includeEmptyValue) {
//        T db = getBaseMapper().selectById(model.getId());
//        if (includeEmptyValue) {
//            ConvertUtils.copyProperties(model, db);
//        } else {
//            ConvertUtils.copyNotEmptyProperties(model, db);
//        }
//        db = commonUpdate(db);
//        getBaseMapper().updateById(db);
//        return db;
//    }
//
//    @Override
//    public V update(BaseReq baseReq) {
//        return update(baseReq, false);
//    }
//
//    @Override
//    public V update(BaseReq baseReq, boolean includeEmptyValue) {
//        T db = checkReqForUpdate(baseReq, includeEmptyValue);
//        db = commonUpdate(db);
//        getBaseMapper().updateById(db);
//        return ConvertUtils.toBean(db, voClass);
//    }
//
//    protected T checkReqForUpdate(BaseReq baseReq, boolean includeEmptyValue) {
//        Objects.requireNonNull(baseReq, "更新的对象不能为空");
//        if (baseReq.getId() == null) {
//            throw ServiceException.errorMsg("id不能为空");
//        }
//        T db = getBaseMapper().selectById(baseReq.getId());
//        if (db == null) {
//            throw ServiceException.errorMsg("更新的数据不存在");
//        }
//        if (includeEmptyValue) {
//            ConvertUtils.copyProperties(baseReq, db);
//        } else {
//            ConvertUtils.copyNotEmptyProperties(baseReq, db);
//        }
//        return db;
//    }
//
//    @Override
//    public List<V> getList(BaseReq baseReq) {
//        QueryWrapper<T> queryWrapper = new QueryWrapper<>();
//        handleQueryWrapper(baseReq, queryWrapper, getEntityClass());
//        List<T> list = this.getBaseMapper().selectList(queryWrapper);
//        return ConvertUtils.toList(list, voClass);
//    }
//
//    @Override
//    public List<V> getList(BaseReq baseReq, String order) {
//        QueryWrapper<T> queryWrapper = new QueryWrapper<>();
//        handleQueryWrapper(baseReq, queryWrapper, getEntityClass());
//        List<OrderItem> orderItems = getOrderItems(order);
//        for (OrderItem orderItem : orderItems) {
//            if (orderItem.isAsc()) {
//                queryWrapper.orderByAsc(orderItem.getColumn());
//            } else {
//                queryWrapper.orderByDesc(orderItem.getColumn());
//            }
//        }
//        List<T> list = this.getBaseMapper().selectList(queryWrapper);
//        return ConvertUtils.toList(list, voClass);
//    }
//
//    @Override
//    public List<T> getListByIds(List<Long> idList) {
//        if (idList == null || idList.isEmpty()) {
//            return new ArrayList<>();
//        }
//        idList = idList.stream().distinct().collect(Collectors.toList());
//        return this.getBaseMapper().selectList(new QueryWrapper<T>().in("id", idList));
//    }
//
//    @Override
//    public List<V> getList(BaseReq baseReq, QueryWrapper<T> queryWrapper) {
//        handleQueryWrapper(baseReq, queryWrapper, getEntityClass());
//        List<T> list = this.getBaseMapper().selectList(queryWrapper);
//        return ConvertUtils.toList(list, voClass);
//    }
//
//    @Override
//    public Page<V> page(BasePageReq basePageReq) {
//        QueryWrapper<T> queryWrapper = new QueryWrapper<>();
//        handleQueryWrapper(basePageReq, queryWrapper, getEntityClass());
//        Page<T> page = convertPageReq(basePageReq);
//        page = page(page, queryWrapper);
//        Page<V> newPage = new Page<>(page.getCurrent(), page.getSize(), page.getTotal());
//        newPage.setOrders(page.getOrders());
//        newPage.setRecords(ConvertUtils.toList(page.getRecords(), voClass));
//        return newPage;
//    }
//
//    @Override
//    public Page<V> page(BasePageReq basePageReq, QueryWrapper<T> queryWrapper) {
//        Page<T> page = convertPageReq(basePageReq);
//        page = page(page, queryWrapper);
//        Page<V> newPage = new Page<>(page.getCurrent(), page.getSize(), page.getTotal());
//        newPage.setOrders(page.getOrders());
//        newPage.setRecords(ConvertUtils.toList(page.getRecords(), voClass));
//        return newPage;
//    }
//
//    @Override
//    public Page<V> page(BaseReq baseReq, BasePageReq basePageReq) {
//        QueryWrapper<T> queryWrapper = new QueryWrapper<>();
//        handleQueryWrapper(baseReq, queryWrapper, getEntityClass());
//        Page<T> page = convertPageReq(basePageReq);
//        page = page(page, queryWrapper);
//        Page<V> newPage = new Page<>(page.getCurrent(), page.getSize(), page.getTotal());
//        newPage.setOrders(page.getOrders());
//        newPage.setRecords(ConvertUtils.toList(page.getRecords(), voClass));
//        return newPage;
//    }
//
//    @Override
//    public Page<V> joinPage(BasePageReq basePageReq) {
//        return joinPage(basePageReq, pageNullConsumer);
//    }
//
//    @Override
//    public Page<V> joinPage(BasePageReq basePageReq, Consumer<List<V>> consumer) {
//        if (this.baseMapper instanceof MPJBaseMapper) {
//            Page<T> page = convertPageReq(basePageReq);
//            MPJQueryWrapper<T> mpjQueryWrapper = new MPJQueryWrapper<>();
//            mpjQueryWrapper.selectAll(entityClass);
//            handleQueryWrapper(basePageReq, mpjQueryWrapper, getEntityClass());
//            IPage<V> iPage = ((MPJBaseMapper<?>) this.baseMapper).selectJoinPage(page, voClass, mpjQueryWrapper);
//            if (consumer != pageNullConsumer) {
//                List<V> records = iPage.getRecords();
//                if (records != null && !records.isEmpty()) {
//                    consumer.accept(records);
//                }
//            }
//            return (Page<V>) iPage;
//        } else {
//            throw ServiceException.errorMsg("baseMapper需要集成MPJBaseMapper");
//        }
//    }
//
//    @Override
//    public Page<V> joinPage(BasePageReq basePageReq, MPJLambdaWrapper<T> queryWrapper) {
//        return joinPage(basePageReq, queryWrapper, pageNullConsumer);
//    }
//
//    @Override
//    public Page<V> joinPage(BasePageReq basePageReq, MPJLambdaWrapper<T> queryWrapper, Consumer<List<V>> consumer) {
//        if (this.baseMapper instanceof MPJBaseMapper) {
//            Page<T> page = convertPageReq(basePageReq);
//            IPage<V> iPage = ((MPJBaseMapper<?>) this.baseMapper).selectJoinPage(page, voClass, queryWrapper);
//            Page<V> newPage = (Page<V>) iPage;
//            if (consumer != pageNullConsumer) {
//                List<V> records = newPage.getRecords();
//                if (records != null && !records.isEmpty()) {
//                    consumer.accept(records);
//                }
//            }
//            return newPage;
//        } else {
//            throw ServiceException.errorMsg("baseMapper需要集成MPJBaseMapper");
//        }
//    }
//
//
//    protected Page<T> convertPageReq(BasePageReq basePageReq) {
//        Page<T> page = new Page<>(basePageReq.getPage(), basePageReq.getSize());
//        List<OrderItem> orderItemList = getOrderItems(basePageReq.getOrder());
//        if (!orderItemList.isEmpty()) {
//            page.setOrders(orderItemList);
//        }
//        return page;
//    }
//
//    private List<OrderItem> getOrderItems(String orderStr) {
//        List<OrderItem> orderItemList = new ArrayList<>();
//        Map<String, Field> modelFieldsMap = getFieldMap(getEntityClass());
//        if (StringUtils.isNotBlank(orderStr)) {
//            String[] items = orderStr.split(";");
//            for (String item : items) {
//                if (StringUtils.isBlank(item)) {
//                    continue;
//                }
//                String[] fieldOrder = item.split(",");
//                if (fieldOrder.length < 2) {
//                    continue;
//                }
//                String fieldName = fieldOrder[0].trim();
//                String order = fieldOrder[1].trim();
//                if (modelFieldsMap.get(fieldName) == null) {
//                    continue;
//                }
//                String column = getColumn(fieldName);
//                if (StringUtils.equalsIgnoreCase("asc", order)) {
//                    orderItemList.add(OrderItem.asc(column));
//                } else if (StringUtils.equalsIgnoreCase("desc", order)) {
//                    orderItemList.add(OrderItem.desc(column));
//                }
//            }
//        }
//        return orderItemList;
//    }
//
//    private String getColumn(String fieldName) {
//        return com.baomidou.mybatisplus.core.toolkit.StringUtils.camelToUnderline(fieldName);
//    }
//
//    private String getValidTable(String tableName) {
//        return com.baomidou.mybatisplus.core.toolkit.StringUtils.camelToUnderline(tableName);
//    }
//
//    public void handleQueryWrapper(BaseReq baseReq, AbstractWrapper queryWrapper, Class modelClass, String... excludeFields) {
//        List<Field> reqFields = ReflectionUtils.getAllFields(baseReq);
//        Map<String, Field> modelFieldsMap = getFieldMap(modelClass);
//        Map<String, String> joinTableSqlMap = new HashMap<>();
//        for (Field reqField : reqFields) {
//            reqField.setAccessible(true);
//            String fieldName = reqField.getName();
//            if (excludeFields != null && Arrays.asList(excludeFields).contains(fieldName)) {
//                continue;
//            }
//            QueryCondition queryCondition = reqField.getAnnotation(QueryCondition.class);
//            if (modelFieldsMap.get(fieldName) == null && !checkFieldAnnotation(reqField) && queryCondition == null) {
//                continue;
//            }
//            IgnoreQuery ignoreQuery = reqField.getAnnotation(IgnoreQuery.class);
//            if (ignoreQuery != null) {
//                continue;
//            }
//
//            try {
//                Object val = reqField.get(baseReq);
//                if (val != null) {
//                    String column = getColumn(fieldName);
//                    if (queryWrapper instanceof MPJQueryWrapper) {
//                        if (queryCondition != null) {
//                            JoinCondition joinCondition = queryCondition.leftJoin();
//                            String table = joinCondition.table();
//                            String field = joinCondition.field();
//                            String joinField = joinCondition.joinField();
//                            if (StringUtils.isNotBlank(table) && StringUtils.isNotBlank(joinField) && StringUtils.isNotBlank(field)) {
//                                String asColumn = StringUtils.isNotBlank(joinCondition.asResp()) ? joinCondition.asResp() : column;
//                                String joinFieldName = getColumn(joinField);
//                                String aFieldName = getColumn(field);
//                                String aColumn = StringUtils.isNotBlank(joinCondition.value()) ? getColumn(joinCondition.value()) : column;
//                                column = table + "." + aColumn;
//                                ((MPJQueryWrapper<?>) queryWrapper).select(column + " as " + asColumn);
//                                String joinSql = getValidTable(table) + " as " + table + " on " + table + "." + aFieldName + " = t." + joinFieldName;
//                                if (joinTableSqlMap.get(table) == null) {
//                                    joinTableSqlMap.put(table, joinSql);
//                                    ((MPJQueryWrapper<?>) queryWrapper).leftJoin(joinSql);
//                                }
//                            } else {
//                                column = "t." + column;
//                            }
//                        } else {
//                            column = "t." + column;
//                        }
//                    }
//
//                    if (queryCondition != null && !QueryEnum.AUTO.equals(queryCondition.value())) {
//                        setQueryByCondition(queryWrapper, column, val, queryCondition);
//                    } else {
//                        if (val instanceof String) {
//                            queryWrapper.like(StringUtils.isNotBlank((CharSequence) val), column, val);
//                        } else if (val instanceof Integer || val instanceof Long || val instanceof Boolean || val instanceof Byte || val instanceof Short) {
//                            queryWrapper.eq(val != null, column, val);
//                        } else if (val instanceof Date) {
//                            TimeBegin timeBegin = reqField.getAnnotation(TimeBegin.class);
//                            TimeEnd timeEnd = reqField.getAnnotation(TimeEnd.class);
//                            if (timeBegin != null && StringUtils.isNotBlank(timeBegin.value())) {
//                                queryWrapper.ge(getColumn(timeBegin.value()), val);
//                            } else if (timeEnd != null && StringUtils.isNotBlank(timeEnd.value())) {
//                                queryWrapper.lt(getColumn(timeEnd.value()), val);
//                            }
//                        }
//                    }
//                }
//            } catch (IllegalAccessException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//    private void setQueryByCondition(AbstractWrapper queryWrapper, String column, Object val, QueryCondition queryCondition) {
//        switch (queryCondition.value()) {
//            case EQ:
//                if (val instanceof String) {
//                    queryWrapper.eq(StringUtils.isNotBlank((CharSequence) val), column, val);
//                } else if (val instanceof Integer || val instanceof Long || val instanceof Boolean || val instanceof Byte || val instanceof Short) {
//                    queryWrapper.eq(column, val);
//                }
//                break;
//            case NQ:
//                if (val instanceof String) {
//                    queryWrapper.ne(StringUtils.isNotBlank((CharSequence) val), column, val);
//                } else if (val instanceof Integer || val instanceof Long || val instanceof Boolean || val instanceof Byte || val instanceof Short) {
//                    queryWrapper.ne(column, val);
//                }
//                break;
//            case LIKE_LEFT:
//                if (val instanceof String) {
//                    queryWrapper.likeLeft(StringUtils.isNotBlank((CharSequence) val), column, val);
//                } else if (val instanceof Integer || val instanceof Long || val instanceof Boolean || val instanceof Byte || val instanceof Short) {
//                    queryWrapper.likeLeft(column, val);
//                }
//                break;
//            case LIKE_RIGHT:
//                if (val instanceof String) {
//                    queryWrapper.likeRight(StringUtils.isNotBlank((CharSequence) val), column, val);
//                } else if (val instanceof Integer || val instanceof Long || val instanceof Boolean || val instanceof Byte || val instanceof Short) {
//                    queryWrapper.likeRight(column, val);
//                }
//                break;
//        }
//    }
//
//    private boolean checkFieldAnnotation(Field reqField) {
//        TimeBegin timeBegin = reqField.getAnnotation(TimeBegin.class);
//        TimeEnd timeEnd = reqField.getAnnotation(TimeEnd.class);
//        if (timeBegin != null || timeEnd != null) {
//            return true;
//        }
//        return false;
//    }
//
//    public Map<String, Field> getFieldMap(Class clazz) {
//        List<Field> modelFields = ReflectionUtils.getAllFields(clazz);
//        Map<String, Field> modelFieldsMap = new HashMap<>();
//        for (Field modelField : modelFields) {
//            modelFieldsMap.put(modelField.getName(), modelField);
//        }
//        return modelFieldsMap;
//    }
//
//    @Override
//    public V findById(Long id) {
//        T model = super.getById(id);
//        return ConvertUtils.toBean(model, voClass);
//    }
//
//    @Override
//    public Long deleteById(Long id) {
//        if (super.removeById(id)) {
//            return id;
//        }
//        return null;
//    }
//
//    @Override
//    public void validateReq(Object req) {
//        validateReq(req, true);
//    }
//
//    @Override
//    public void validateReq(Object req, boolean checkNone) {
//        validateReqs(Arrays.asList(req), checkNone);
//    }
//
//    @Override
//    public void validateReqs(List<Object> reqs, boolean checkNone) {
//        if (checkNone) {
//            if (reqs == null) {
//                throw ServiceException.error(ExceptionEnum.PARAMETER_IS_NULL);
//            }
//        }
//        for (Object req : reqs) {
//            Set<ConstraintViolation<Object>> validate = validator.validate(req);
//            if (!validate.isEmpty()) {
//                ConstraintViolation<Object> next = validate.iterator().next();
//                String msg = "参数校验失败,字段:" + next.getPropertyPath() + ",值为:" + next.getInvalidValue() + ",原因:" + next.getMessage();
//                throw new ValidationException(msg);
//            }
//        }
//    }
//
//}
