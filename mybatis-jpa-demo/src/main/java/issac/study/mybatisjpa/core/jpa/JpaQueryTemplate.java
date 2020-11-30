package issac.study.mybatisjpa.core.jpa;

import issac.study.mybatisjpa.core.jpa.annotation.SyncDateTime;
import issac.study.mybatisjpa.core.jpa.annotation.TimeBegin;
import issac.study.mybatisjpa.core.jpa.annotation.TimeEnd;
import issac.study.mybatisjpa.utils.ConvertUtils;
import issac.study.mybatisjpa.utils.DateUtils;
import issac.study.mybatisjpa.utils.ReflectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.lang.reflect.Field;
import java.time.ZoneId;
import java.util.*;
import java.util.function.Consumer;

/**
 * jpa动态查询工具类
 *
 * @author humy6
 * @Date: 2019/7/9 14:04
 */

public class JpaQueryTemplate {

    private static final Logger LOGGER = LoggerFactory.getLogger(JpaQueryTemplate.class);

    private EntityManagerFactory entityManagerFactory;

    public JpaQueryTemplate() {

    }

    public JpaQueryTemplate(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    /**
     * 构建Query类且自动注入entityManagerFactory，结合query()方法做最后查询，此类不可new必须，注入到容器
     *
     * @param entityClass 实体类
     * @param <E>
     * @return
     */
    public <E> Query<E> build(Class<E> entityClass) {
        return new Query(entityManagerFactory.createEntityManager(), entityClass);
    }

    /**
     * 构建Specification的查询类，结合predicates()方法获取predicate，此类可以直接new
     * 快捷类接口 JpaQuerySpecification 可以使用lambda表达式
     *
     * @param root
     * @param criteriaQuery
     * @param criteriaBuilder
     * @param <E>
     * @return
     */
    public <E> Query<E> buildSpecification(Root root, CriteriaQuery criteriaQuery, CriteriaBuilder criteriaBuilder) {
        return new Query(root, criteriaQuery, criteriaBuilder);
    }


    public static class GroupCondition {
        private Root root;
        private CriteriaBuilder criteriaBuilder;
        private List<Selection<?>> selectionList = new ArrayList<>();
        private List<Expression<?>> groupList = new ArrayList<>();
        private List<Order> orderList = new ArrayList<>();

        public GroupCondition(Root root, CriteriaBuilder criteriaBuilder) {
            this.root = root;
            this.criteriaBuilder = criteriaBuilder;
        }

        public Root getRoot() {
            return root;
        }

        public CriteriaBuilder getCriteriaBuilder() {
            return criteriaBuilder;
        }

        public List<Selection<?>> getSelectionList() {
            return selectionList;
        }

        public List<Expression<?>> getGroupList() {
            return groupList;
        }

        public List<Order> getOrderList() {
            return orderList;
        }

        public GroupCondition select(Selection<?>... selections) {
            for (Selection<?> selection : selections) {
                this.selectionList.add(selection);
            }
            return this;
        }

        public GroupCondition groupBy(Expression<?>... expressions) {
            for (Expression<?> expression : expressions) {
                this.groupList.add(expression);
            }
            return this;
        }

        public GroupCondition orderBy(Order... orders) {
            for (Order order : orders) {
                this.orderList.add(order);
            }
            return this;
        }

        public Path getField(String name) {
            return this.root.get(name);
        }

        public CriteriaBuilder getCb() {
            return criteriaBuilder;
        }
    }

    /**
     * 查询内部类
     *
     * @param <E>
     */
    public static class Query<E> {

        public static final String ID = "id";
        private EntityManager entityManager;

        private CriteriaBuilder criteriaBuilder;

        private Root<E> root;

        private CriteriaQuery<E> criteriaQuery;
        private Predicate predicateAnd;
        private Predicate predicateOr;
        /**
         * 是否开启or操作
         */
        private boolean isOr;
        /**
         * 关联表
         */
        private Map<String, JoinType> joinMap = new HashMap<>();
        private Map<String, Join> joinMapCache = new HashMap<>();

        private MultiValueMap<String, InnerQuery> andQueryMap = new LinkedMultiValueMap<>();

        private MultiValueMap<String, InnerQuery> orQueryMap = new LinkedMultiValueMap<>();

        private Class<E> entityClass;
        /**
         * 升序列表
         */
        private List<InnerQuery> ascOrderList = new ArrayList<>();
        /**
         * 降序列表
         */
        private List<InnerQuery> descOrderList = new ArrayList<>();

        public Query(EntityManager entityManager, Class<E> entityClass) {
            this.entityManager = entityManager;
            this.criteriaBuilder = entityManager.getCriteriaBuilder();
            this.criteriaQuery = this.criteriaBuilder.createQuery(entityClass);
            this.root = this.criteriaQuery.from(entityClass);
            this.predicateAnd = this.criteriaBuilder.conjunction();
            this.predicateOr = this.criteriaBuilder.disjunction();
            this.entityClass = entityClass;
        }


        public Query(Root<E> root, CriteriaQuery<E> criteriaQuery, CriteriaBuilder criteriaBuilder) {
            this.criteriaBuilder = criteriaBuilder;
            this.criteriaQuery = criteriaQuery;
            this.root = root;
            this.predicateAnd = this.criteriaBuilder.conjunction();
            this.predicateOr = this.criteriaBuilder.disjunction();
        }

        /**
         * 无需手动开启，现在使用or操作的时候，会自动开启
         */
        @Deprecated
        public Query<E> enableOr() {
            return this;
        }

        /**
         * 关联表 注意：下面的关联查询必须先在这里声明才可以查询
         *
         * @param joinName
         * @param joinType
         * @return
         */
        public Query<E> join(String joinName, JoinType joinType) {
            joinMap.put(joinName, joinType);
            return this;
        }

        private Query<E> check(String joinName, Operator operator, String name, Object value) {
            if (StringUtils.isBlank(name)) {
                return this;
            }
            if (value == null) {
                if (operator != Operator.IS_NULL && operator != Operator.IS_NOT_NULL) {
                    return this;
                }
            }
            if (value instanceof String) {
                if (StringUtils.isBlank((String) value)) {
                    return this;
                }
            }
            if (value instanceof Collection) {
                if (((Collection) value).isEmpty()) {
                    return this;
                }
            }
            if (operator == Operator.GE || operator == Operator.GT || operator == Operator.LE || operator == Operator.LT) {
                if (!(value instanceof Comparable)) {
                    return this;
                }
            }
            return null;
        }

        private Object resetValue(String joinName, Operator operator, String name, Object value) {
            if (operator == Operator.LIKE || operator == Operator.NOT_LIKE) {
                String tempVal = value + "";
                if (tempVal.indexOf("%") < 0) {
                    value = "%" + tempVal.trim() + "%";
                }
            }
            if (operator == Operator.GT || operator == Operator.GE || operator == Operator.LE || operator == Operator.LT) {
                if (value instanceof Date) {
                    value = (((Date) value).toInstant().atZone(ZoneId.systemDefault())).toLocalDateTime();
                }
            }
            return value;
        }

        private Path getPath(String joinName, String name) {
            Path path;
            if (joinName == null) {
                path = root.get(name);
            } else {
                Objects.requireNonNull(joinName);
                Join join = joinMapCache.get(joinName);
                if (join == null) {
                    JoinType joinType = joinMap.get(joinName);
                    if (joinType == null) {
                        join = root.join(joinName, JoinType.INNER);
                    } else {
                        join = root.join(joinName, joinType);
                    }
                    joinMapCache.put(joinName, join);
                }
                path = join.get(name);
            }
            return path;
        }

        /**
         * 此方法会自动推测需要查询的字段
         *
         * @param req
         * @return
         */
        public Query<E> and(Object req, Class<E> eClass) {
            List<Field> reqFields = ReflectionUtils.getAllFields(req);
            List<Field> entityFields = ReflectionUtils.getAllFields(eClass);
            Map<String, Field> entityFieldMap = new HashMap<>();
            entityFields.forEach(it -> entityFieldMap.put(it.getName(), it));
            for (Field field : reqFields) {
                String fieldName = field.getName();
                SyncDateTime syncDateTime = field.getAnnotation(SyncDateTime.class);
                if (syncDateTime != null) {
                    fieldName = syncDateTime.value();
                }
                TimeBegin timeBegin = field.getAnnotation(TimeBegin.class);
                if (timeBegin != null) {
                    fieldName = timeBegin.value();
                }
                TimeEnd timeEnd = field.getAnnotation(TimeEnd.class);
                if (timeEnd != null) {
                    fieldName = timeEnd.value();
                }
                if (entityFieldMap.get(fieldName) == null) {
                    continue;
                }
                try {
                    field.setAccessible(true);
                    Object value = field.get(req);
                    if (value != null) {
                        if (value instanceof String) {
                            //处理同步更新时间字段
                            if (syncDateTime != null) {
                                String syncDateTimeField = syncDateTime.value();
                                if (StringUtils.isNotBlank(syncDateTimeField)) {
                                    andGe(syncDateTimeField, DateUtils.fmt(value + ""));
                                }
                                continue;
                            }
                            if (timeBegin != null) {
                                String timeBeginField = timeBegin.value();
                                if (StringUtils.isNoneBlank(timeBeginField)) {
                                    andGe(timeBeginField, DateUtils.fmt(value + ""));
                                }
                                continue;
                            }
                            if (timeEnd != null) {
                                String timeEndField = timeEnd.value();
                                if (StringUtils.isNoneBlank(timeEndField)) {
                                    andLe(timeEndField, DateUtils.fmt(value + ""));
                                }
                                continue;
                            }
                            andLike(fieldName, value);
                        } else if (value instanceof Integer || value instanceof Boolean) {
                            andEq(fieldName, value);
                        } else if (value instanceof Collection) {
                            andIn(fieldName, value);
                        }
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            return this;
        }

        /**
         * 此方法会自动推测需要查询的字段
         *
         * @param joinName
         * @param req
         * @param eClass
         * @param jClass   join的类
         * @return
         */
        public Query<E> and(String joinName, Object req, Class<E> eClass, Class<?> jClass) {
            List<Field> reqFields = ReflectionUtils.getAllFields(req);
            List<Field> jEntityFields = ReflectionUtils.getAllFields(jClass);
            Map<String, Field> jEntityFieldMap = new HashMap<>();
            jEntityFields.forEach(it -> jEntityFieldMap.put(it.getName(), it));
            for (Field field : reqFields) {
                String fieldName = field.getName();
                SyncDateTime syncDateTime = field.getAnnotation(SyncDateTime.class);
                if (syncDateTime != null) {
                    fieldName = syncDateTime.value();
                }
                if (jEntityFieldMap.get(fieldName) == null) {
                    continue;
                }
                try {
                    field.setAccessible(true);
                    Object value = field.get(req);
                    if (value != null) {
                        if (value instanceof String) {
                            //处理同步更新时间字段
                            if (syncDateTime != null) {
                                String syncDateTimeField = syncDateTime.value();
                                if (StringUtils.isNotBlank(syncDateTimeField)) {
                                    andGe(joinName, syncDateTimeField, DateUtils.fmt(value + ""));
                                }
                                continue;
                            }
                            andLike(joinName, fieldName, value);
                        } else if (value instanceof Integer || value instanceof Boolean) {
                            andEq(joinName, fieldName, value);
                        } else if (value instanceof Collection) {
                            andIn(joinName, fieldName, value);
                        }
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            return this;
        }

        /**
         * 关联表查询功能
         *
         * @param joinName 关联表的在主表的字段名称，主表必须实现关联（如OneToOne）
         * @param operator 操作因子 如 = >
         * @param name     查询的实体字段名称
         * @param value    查询的值
         * @return
         */
        public Query<E> and(String joinName, Operator operator, String name, Object value) {
            return setInnerQueryMap(joinName, operator, name, value, false, false, false);
        }

        private void andPredicate(String joinName, Operator operator, String name, Object value) {
            value = resetValue(joinName, operator, name, value);
            Path path = getPath(joinName, name);
            switch (operator) {
                case EQ:
                    predicateAnd = criteriaBuilder.and(predicateAnd, criteriaBuilder.equal(path, value));
                    break;
                case GE:
                    predicateAnd = criteriaBuilder.and(predicateAnd, criteriaBuilder.greaterThanOrEqualTo(path, (Comparable) value));
                    break;
                case GT:
                    predicateAnd = criteriaBuilder.and(predicateAnd, criteriaBuilder.greaterThan(path, (Comparable) value));
                    break;
                case IN:
                    Collection collection = (Collection) value;
                    if (collection.size() == 1) {
                        predicateAnd = criteriaBuilder.and(predicateAnd, criteriaBuilder.equal(path, collection.iterator().next()));
                    } else {
                        predicateAnd = criteriaBuilder.and(predicateAnd, path.in(collection));
                    }
                    break;
                case NOT_IN:
                    Collection collection2 = (Collection) value;
                    if (collection2.size() == 1) {
                        predicateAnd = criteriaBuilder.and(predicateAnd, criteriaBuilder.notEqual(path, collection2.iterator().next()));
                    } else {
                        predicateAnd = criteriaBuilder.and(predicateAnd, criteriaBuilder.not(path.in(collection2)));
                    }
                    break;
                case LE:
                    predicateAnd = criteriaBuilder.and(predicateAnd, criteriaBuilder.lessThanOrEqualTo(path, (Comparable) value));
                    break;
                case LT:
                    predicateAnd = criteriaBuilder.and(predicateAnd, criteriaBuilder.lessThan(path, (Comparable) value));
                    break;
                case NE:
                    predicateAnd = criteriaBuilder.and(predicateAnd, criteriaBuilder.notEqual(path, value));
                    break;
                case LIKE:
                    predicateAnd = criteriaBuilder.and(predicateAnd, criteriaBuilder.like(path, (String) value));
                    break;
                case NOT_LIKE:
                    predicateAnd = criteriaBuilder.and(predicateAnd, criteriaBuilder.notLike(path, (String) value));
                    break;
                case IS_NULL:
                    predicateAnd = criteriaBuilder.and(predicateAnd, path.isNull());
                    break;
                case IS_NOT_NULL:
                    predicateAnd = criteriaBuilder.and(predicateAnd, path.isNotNull());
                    break;
            }
        }

        private Query<E> setInnerQueryMap(String joinName, Operator operator, String name, Object value, boolean isOr, boolean isOverride, boolean isDelete) {
            if (!isDelete) {
                Query<E> check = check(joinName, operator, name, value);
                if (check != null) {
                    return check;
                }
            }
            String key = "";
            if (StringUtils.isNotBlank(joinName)) {
                key = joinName;
            }
            key = key + "_" + name;
            if (isOr) {
                if (isOverride) {
                    this.orQueryMap.remove(key);
                }
                if (isDelete) {
                    this.orQueryMap.remove(key);
                } else {
                    this.orQueryMap.add(key, new InnerQuery().setRootName(joinName).setName(name).setOperator(operator).setValue(value));
                }
                if (this.orQueryMap.size() > 0) {
                    this.isOr = true;
                } else {
                    this.isOr = false;
                }
            } else {
                if (isOverride) {
                    this.andQueryMap.remove(key);
                }
                if (isDelete) {
                    this.andQueryMap.remove(key);
                } else {
                    this.andQueryMap.add(key, new InnerQuery().setRootName(joinName).setName(name).setOperator(operator).setValue(value));
                }
            }
            return this;
        }

        /**
         * 关联表查询功能
         *
         * @param joinName 关联表的在主表的字段名称，主表必须实现关联（如OneToOne）
         * @param operator 操作因子 如 = >
         * @param name     查询的实体字段名称
         * @param value    查询的值
         * @return
         */
        public Query<E> or(String joinName, Operator operator, String name, Object value) {
            return setInnerQueryMap(joinName, operator, name, value, true, false, false);
        }

        private void orPredicate(String joinName, Operator operator, String name, Object value) {
            value = resetValue(joinName, operator, name, value);
            Path path = getPath(joinName, name);
            switch (operator) {
                case EQ:
                    predicateOr = criteriaBuilder.or(predicateOr, criteriaBuilder.equal(path, value));
                    break;
                case GE:
                    predicateOr = criteriaBuilder.or(predicateOr, criteriaBuilder.greaterThanOrEqualTo(path, (Comparable) value));
                    break;
                case GT:
                    predicateOr = criteriaBuilder.or(predicateOr, criteriaBuilder.greaterThan(path, (Comparable) value));
                    break;
                case IN:
                    Collection collection = (Collection) value;
                    if (collection.size() == 1) {
                        predicateOr = criteriaBuilder.or(predicateOr, criteriaBuilder.equal(path, collection.iterator().next()));
                    } else {
                        predicateOr = criteriaBuilder.or(predicateOr, path.in(collection));
                    }
                    break;
                case LE:
                    predicateOr = criteriaBuilder.or(predicateOr, criteriaBuilder.lessThanOrEqualTo(path, (Comparable) value));
                    break;
                case LT:
                    predicateOr = criteriaBuilder.or(predicateOr, criteriaBuilder.lessThan(path, (Comparable) value));
                    break;
                case NE:
                    predicateOr = criteriaBuilder.or(predicateOr, criteriaBuilder.notEqual(path, value));
                    break;
                case LIKE:
                    predicateOr = criteriaBuilder.or(predicateOr, criteriaBuilder.like(path, (String) value));
                    break;
                case NOT_LIKE:
                    predicateOr = criteriaBuilder.or(predicateOr, criteriaBuilder.notLike(path, (String) value));
                    break;
                case IS_NULL:
                    predicateOr = criteriaBuilder.or(predicateOr, path.isNull());
                    break;
                case IS_NOT_NULL:
                    predicateOr = criteriaBuilder.or(predicateOr, path.isNotNull());
                    break;
            }
        }

        /**
         * 主表查询的快捷方式
         *
         * @param operator
         * @param name
         * @param value
         * @return
         */
        public Query<E> and(Operator operator, String name, Object value) {
            return and(null, operator, name, value);
        }

        /**
         * equal关联表查询的快捷方式
         *
         * @param joinName
         * @param name
         * @param value
         * @return
         */
        public Query<E> andEq(String joinName, String name, Object value) {
            return and(joinName, Operator.EQ, name, value);
        }

        /**
         * 操作符为Eq的主表查询快捷方式
         *
         * @param name
         * @param value
         * @return
         */
        public Query<E> andEq(String name, Object value) {
            return andEq(null, name, value);
        }

        /**
         * in关联表查询的快捷方式
         *
         * @param joinName
         * @param name
         * @param value
         * @return
         */
        public Query<E> andIn(String joinName, String name, Object value) {
            return and(joinName, Operator.IN, name, value);
        }

        /**
         * in主表查询的快捷方式
         *
         * @param name
         * @param value
         * @return
         */
        public Query<E> andIn(String name, Object value) {
            return andIn(null, name, value);
        }

        /**
         * not in
         *
         * @param joinName
         * @param name
         * @param value
         * @return
         */
        public Query<E> andNotIn(String joinName, String name, Object value) {
            return and(joinName, Operator.NOT_IN, name, value);
        }

        /**
         * 快捷方式
         *
         * @param name
         * @param value
         * @return
         */
        public Query<E> andNotIn(String name, Object value) {
            return andNotIn(null, name, value);
        }

        /**
         * like关联表查询的快捷方式
         *
         * @param joinName
         * @param name
         * @param value
         * @return
         */
        public Query<E> andLike(String joinName, String name, Object value) {
            return and(joinName, Operator.LIKE, name, value);
        }

        /**
         * like主表查询的快捷方式
         *
         * @param name
         * @param value
         * @return
         */
        public Query<E> andLike(String name, Object value) {
            return andLike(null, name, value);
        }

        /**
         * 关联表id查询的快捷方式
         *
         * @param joinName
         * @param value
         * @return
         */
        public Query<E> andEqId(String joinName, Object value) {
            return andEq(joinName, ID, value);
        }

        /**
         * 操作符为Eq且查询字段为id的主表查询快捷方式
         *
         * @param value
         * @return
         */
        public Query<E> andEqId(Object value) {
            return andEqId(null, value);
        }

        /**
         * 关联表ids列表查询的快捷方式
         *
         * @param joinName
         * @param ids
         * @return
         */
        public Query<E> andInIds(String joinName, Collection ids) {
            return andIn(joinName, ID, ids);
        }

        /**
         * 操作符为in且查询字段为id的list查询快捷方式
         *
         * @param ids
         * @return
         */
        public Query<E> andInIds(Collection ids) {
            return andInIds(null, ids);
        }

        /**
         * and操作isNull
         *
         * @param joinName
         * @param name
         * @return
         */
        public Query<E> andIsNull(String joinName, String name) {
            return and(joinName, Operator.IS_NULL, name, null);
        }

        /**
         * 快捷方式
         *
         * @param joinName
         * @param name
         * @return
         */
        public Query<E> andIsNotNull(String joinName, String name) {
            return and(joinName, Operator.IS_NOT_NULL, name, null);
        }


        /**
         * 快捷方式
         *
         * @param name
         * @return
         */
        public Query<E> andIsNull(String name) {
            return andIsNull(null, name);
        }

        /**
         * 快捷方式
         *
         * @param name
         * @return
         */
        public Query<E> andIsNotNull(String name) {
            return andIsNotNull(null, name);
        }

        /**
         * 快捷方式
         *
         * @param joinName
         * @param name
         * @param value
         * @return
         */
        public Query<E> andGe(String joinName, String name, Object value) {
            return and(joinName, Operator.GE, name, value);
        }

        /**
         * 快捷方式
         *
         * @param name
         * @param value
         * @return
         */
        public Query<E> andGe(String name, Object value) {
            return andGe(null, name, value);
        }

        /**
         * 快捷方式
         *
         * @param joinName
         * @param name
         * @param value
         * @return
         */
        public Query<E> andGt(String joinName, String name, Object value) {
            return and(joinName, Operator.GT, name, value);
        }

        /**
         * 快捷方式
         *
         * @param name
         * @param value
         * @return
         */
        public Query<E> andGt(String name, Object value) {
            return andGt(null, name, value);
        }


        /**
         * 快捷方式
         *
         * @param joinName
         * @param name
         * @param value
         * @return
         */
        public Query<E> andLe(String joinName, String name, Object value) {
            return and(joinName, Operator.LE, name, value);
        }

        /**
         * 快捷方式
         *
         * @param name
         * @param value
         * @return
         */
        public Query<E> andLe(String name, Object value) {
            return andLe(null, name, value);
        }

        /**
         * 快捷方式
         *
         * @param joinName
         * @param name
         * @param value
         * @return
         */
        public Query<E> andLt(String joinName, String name, Object value) {
            return and(joinName, Operator.LT, name, value);
        }

        /**
         * 快捷方式
         *
         * @param name
         * @param value
         * @return
         */
        public Query<E> andLt(String name, Object value) {
            return andLt(null, name, value);
        }

        /**
         * 范围查询
         *
         * @param joinName
         * @param name
         * @param gValue   区间[a,b] gValue 为a lValue为b，include为true表示包含两边，否则不包含
         * @param lValue
         * @param include
         * @return
         */
        public Query<E> andBetween(String joinName, String name, Object gValue, Object lValue, boolean include) {
            if (include) {
                and(joinName, Operator.GE, name, gValue);
                and(joinName, Operator.LE, name, lValue);
            } else {
                and(joinName, Operator.GT, name, gValue);
                and(joinName, Operator.LT, name, lValue);
            }
            return this;
        }

        /**
         * 范围查询的快捷方式
         *
         * @param name
         * @param gValue
         * @param lValue
         * @return
         */
        public Query<E> andBetween(String name, Object gValue, Object lValue) {
            return andBetween(null, name, gValue, lValue, true);
        }

        /**
         * 主表查询的快捷方式
         *
         * @param operator
         * @param name
         * @param value
         * @return
         */
        public Query<E> or(Operator operator, String name, Object value) {
            return or(null, operator, name, value);
        }

        /**
         * equal关联表查询的快捷方式
         *
         * @param joinName
         * @param name
         * @param value
         * @return
         */
        public Query<E> orEq(String joinName, String name, Object value) {
            return or(joinName, Operator.EQ, name, value);
        }

        /**
         * 操作符为Eq的主表查询快捷方式
         *
         * @param name
         * @param value
         * @return
         */
        public Query<E> orEq(String name, Object value) {
            return orEq(null, name, value);
        }

        /**
         * in关联表查询的快捷方式
         *
         * @param joinName
         * @param name
         * @param value
         * @return
         */
        public Query<E> orIn(String joinName, String name, Object value) {
            return or(joinName, Operator.IN, name, value);
        }

        /**
         * in主表查询的快捷方式
         *
         * @param name
         * @param value
         * @return
         */
        public Query<E> orIn(String name, Object value) {
            return orIn(null, name, value);
        }

        /**
         * like关联表查询的快捷方式
         *
         * @param joinName
         * @param name
         * @param value
         * @return
         */
        public Query<E> orLike(String joinName, String name, Object value) {
            return or(joinName, Operator.LIKE, name, value);
        }

        /**
         * like主表查询的快捷方式
         *
         * @param name
         * @param value
         * @return
         */
        public Query<E> orLike(String name, Object value) {
            return orLike(null, name, value);
        }

        /**
         * 关联表id查询的快捷方式
         *
         * @param joinName
         * @param value
         * @return
         */
        public Query<E> orEqId(String joinName, Object value) {
            return orEq(joinName, ID, value);
        }

        /**
         * 操作符为Eq且查询字段为id的主表查询快捷方式
         *
         * @param value
         * @return
         */
        public Query<E> orEqId(Object value) {
            return orEqId(null, value);
        }

        /**
         * 关联表ids列表查询的快捷方式
         *
         * @param joinName
         * @param ids
         * @return
         */
        public Query<E> orInIds(String joinName, Collection ids) {
            return orIn(joinName, ID, ids);
        }

        /**
         * 操作符为in且查询字段为id的list查询快捷方式
         *
         * @param ids
         * @return
         */
        public Query<E> orInIds(Collection ids) {
            return orInIds(null, ids);
        }

        /**
         * and操作isNull
         *
         * @param joinName
         * @param name
         * @return
         */
        public Query<E> orIsNull(String joinName, String name) {
            return or(joinName, Operator.IS_NULL, name, null);
        }

        /**
         * 快捷方式
         *
         * @param joinName
         * @param name
         * @return
         */
        public Query<E> orIsNotNull(String joinName, String name) {
            return or(joinName, Operator.IS_NOT_NULL, name, null);
        }

        /**
         * 快捷方式
         *
         * @param name
         * @return
         */
        public Query<E> orIsNull(String name) {
            return orIsNull(null, name);
        }

        /**
         * 快捷方式
         *
         * @param name
         * @return
         */
        public Query<E> orIsNotNull(String name) {
            return orIsNotNull(null, name);
        }

        /**
         * 覆盖查询条件
         *
         * @param joinName
         * @param operator
         * @param name
         * @param value
         * @param isOr
         * @return
         */
        public Query<E> override(String joinName, Operator operator, String name, Object value, boolean isOr) {
            return setInnerQueryMap(joinName, operator, name, value, isOr, true, false);
        }

        public Query<E> orOverride(String joinName, Operator operator, String name, Object value) {
            return override(joinName, operator, name, value, true);
        }

        public Query<E> andOverride(String joinName, Operator operator, String name, Object value) {
            return override(joinName, operator, name, value, false);
        }

        public Query<E> orOverride(Operator operator, String name, Object value) {
            return orOverride(null, operator, name, value);
        }

        public Query<E> andOverride(Operator operator, String name, Object value) {
            return andOverride(null, operator, name, value);
        }


        public Query<E> delete(String joinName, String name, Object value, boolean isOr) {
            return setInnerQueryMap(joinName, null, name, value, isOr, false, true);
        }

        public Query<E> orDelete(String joinName, String name, Object value) {
            return delete(joinName, name, value, true);
        }

        public Query<E> andDelete(String joinName, String name, Object value) {
            return delete(joinName, name, value, false);
        }

        public Query<E> orDelete(String name, Object value) {
            return orDelete(null, name, value);
        }

        public Query<E> andDelete(String name, Object value) {
            return andDelete(null, name, value);
        }

        /**
         * 排序-升序
         *
         * @param joinName
         * @param name
         * @return
         */
        public Query<E> asc(String joinName, String name) {
            this.ascOrderList.add(new InnerQuery().setJoinName(joinName).setName(name));
            return this;
        }

        public Query<E> asc(String name) {
            return asc(null, name);
        }

        /**
         * 排序-降序
         *
         * @param joinName
         * @param name
         * @return
         */
        public Query<E> desc(String joinName, String name) {
            this.descOrderList.add(new InnerQuery().setJoinName(joinName).setName(name));
            return this;
        }

        public Query<E> desc(String name) {
            return desc(null, name);
        }

        /**
         * 配合 build(Class<E> entityClass)  查询
         *
         * @return
         */
        public List<E> query() {
            List<E> resultList = new ArrayList<>();
            try {
                CriteriaQuery<E> query = handleCondition();
                resultList = this.entityManager.createQuery(query).getResultList();
            } finally {
                closeEntityManager(this.entityManager);
            }
            return resultList;
        }

        private CriteriaQuery<E> handleCondition() {
            Predicate restrictions = getPredicate();
            CriteriaQuery<E> query = this.criteriaQuery.where(restrictions);
            handleQuery(query);
            return query;
        }

        /**
         * 查询唯一一个，如果有多个抛出错误
         *
         * @return
         */
        public E querySingleOne() {
            try {
                CriteriaQuery<E> query = handleCondition();
                return this.entityManager.createQuery(query).getSingleResult();
            } catch (NoResultException noResultException) {
                return null;
            } finally {
                closeEntityManager(this.entityManager);
            }
        }

        /**
         * 快捷方式
         *
         * @param vClass
         * @param <V>
         * @return
         */
        public <V> V querySingleOne(Class<V> vClass) {
            E e = querySingleOne();
            if (e != null) {
                return ConvertUtils.convertObject(e, vClass);
            }
            return null;
        }

        /**
         * 快捷方式
         *
         * @param vClass
         * @param <V>
         * @return
         */
        public <V> List<V> query(Class<V> vClass) {
            return ConvertUtils.convertList(query(), vClass);
        }

        /**
         * 多个里面，获取第一个
         *
         * @return
         */
        public Optional<E> queryFirstOne() {
            List<E> result = query();
            if (!result.isEmpty()) {
                return Optional.of(result.get(0));
            }
            return Optional.empty();
        }

        /**
         * 获取并且转换
         *
         * @param vClass
         * @param <V>
         * @return
         */
        public <V> Optional<V> queryFirstOne(Class<V> vClass) {
            List<E> result = query();
            if (!result.isEmpty()) {
                Optional<E> e = Optional.of(result.get(0));
                return e.map(it -> ConvertUtils.convertObject(it, vClass));
            }
            return Optional.empty();
        }

        private void handleQuery(CriteriaQuery query) {
            List<Order> orderList = new ArrayList<>();
            if (!this.ascOrderList.isEmpty()) {
                for (InnerQuery innerQuery : this.ascOrderList) {
                    Path path = getPath(innerQuery.getJoinName(), innerQuery.getName());
                    orderList.add(this.criteriaBuilder.asc(path));
                }
            } else if (!this.descOrderList.isEmpty()) {
                for (InnerQuery innerQuery : this.descOrderList) {
                    Path path = getPath(innerQuery.getJoinName(), innerQuery.getName());
                    orderList.add(this.criteriaBuilder.desc(path));
                }
            }
            if (!orderList.isEmpty()) {
                query.orderBy(orderList);
            }
        }


        private void closeEntityManager(EntityManager entityManager) {
            if (entityManager != null) {
                entityManager.close();
            }
        }

        /**
         * 求和
         *
         * @param name
         * @return
         */
        public Number sum(String name) {
            CriteriaQuery<Number> query = this.criteriaBuilder.createQuery(Number.class);
            this.root = query.from(this.entityClass);
            Expression<Number> sum = this.criteriaBuilder.sum(this.root.get(name));
            query.select(sum);
            query.where(getPredicate());
            try {
                return this.entityManager.createQuery(query).getSingleResult();
            } finally {
                closeEntityManager(this.entityManager);
            }

        }

        /**
         * 求平均数
         *
         * @param name
         * @return
         */
        public Double avg(String name) {
            CriteriaQuery<Double> query = this.criteriaBuilder.createQuery(Double.class);
            this.root = query.from(this.entityClass);
            Expression<Double> avg = this.criteriaBuilder.avg(this.root.get(name));
            query.select(avg);
            query.where(getPredicate());
            try {
                return this.entityManager.createQuery(query).getSingleResult();
            } finally {
                closeEntityManager(this.entityManager);
            }
        }

        /**
         * groupBy聚合查询
         *
         * @param groupConditionConsumer
         * @param vClass
         * @param <V>
         * @return
         */
        public <V> List<V> groupBy(Consumer<GroupCondition> groupConditionConsumer, Class<V> vClass) {
            CriteriaQuery<V> query = this.criteriaBuilder.createQuery(vClass);
            this.root = query.from(this.entityClass);
            GroupCondition groupCondition = new GroupCondition(this.root, this.criteriaBuilder);
            groupConditionConsumer.accept(groupCondition);
            query.multiselect(groupCondition.getSelectionList()).where(getPredicate()).groupBy(groupCondition.getGroupList()).orderBy(groupCondition.getOrderList());
            try {
                return this.entityManager.createQuery(query).getResultList();
            } finally {
                closeEntityManager(this.entityManager);
            }
        }

        /**
         * groupBy聚合分页查询
         *
         * @param groupConditionConsumer
         * @param vClass
         * @param pageable
         * @param <V>
         * @return
         */
        public <V> Page<V> groupBy(Consumer<GroupCondition> groupConditionConsumer, Class<V> vClass, Pageable pageable) {
            CriteriaQuery<V> query = this.criteriaBuilder.createQuery(vClass);
            this.root = query.from(this.entityClass);
            GroupCondition groupCondition = new GroupCondition(this.root, this.criteriaBuilder);
            groupConditionConsumer.accept(groupCondition);
            query.multiselect(groupCondition.getSelectionList()).where(getPredicate()).groupBy(groupCondition.getGroupList()).orderBy(groupCondition.getOrderList());
            try {
                long count = this.entityManager.createQuery(query).getResultStream().count();
                TypedQuery<V> typedQuery = this.entityManager.createQuery(query);
                typedQuery.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
                typedQuery.setMaxResults(pageable.getPageSize());
                List<V> resultList = typedQuery.getResultList();
                if (resultList == null) {
                    resultList = new ArrayList<>();
                }
                return new PageImpl<>(resultList, pageable, count);
            } finally {
                closeEntityManager(this.entityManager);
            }

        }

        /**
         * 查询返回条数
         *
         * @return
         */
        public long count() {
            CriteriaQuery<Long> query = this.criteriaBuilder.createQuery(Long.class);
            this.root = query.from(this.entityClass);
            Expression<Long> count = this.criteriaBuilder.count(this.root.get(ID));
            query.select(count);
            query.where(getPredicate());
            try {
                return this.entityManager.createQuery(query).getSingleResult();
            } finally {
                closeEntityManager(this.entityManager);
            }

        }

        /**
         * 查询条件是否存在
         *
         * @return
         */
        public boolean exist() {
            return count() > 0 ? true : false;
        }

        private Predicate getPredicate() {
            Predicate restrictions;
            for (List<InnerQuery> innerQueryList : this.andQueryMap.values()) {
                for (InnerQuery innerQuery : innerQueryList) {
                    andPredicate(innerQuery.getRootName(), innerQuery.getOperator(), innerQuery.getName(), innerQuery.getValue());
                }
            }
            if (this.isOr) {
                for (List<InnerQuery> innerQueryList : this.orQueryMap.values()) {
                    for (InnerQuery innerQuery : innerQueryList) {
                        orPredicate(innerQuery.getRootName(), innerQuery.getOperator(), innerQuery.getName(), innerQuery.getValue());
                    }
                }
                restrictions = this.criteriaBuilder.and(this.predicateAnd, this.predicateOr);
            } else {
                restrictions = this.criteriaBuilder.and(this.predicateAnd);
            }
            return restrictions;
        }

        /**
         * 配合buildSpecification(Root root, CriteriaQuery criteriaQuery, CriteriaBuilder criteriaBuilder) ，用于产生specification类
         *
         * @return
         */
        public Predicate restrictions() {
            Predicate restrictions = getPredicate();
            return restrictions;
        }
    }

    private static class InnerQuery {
        private Operator operator;
        private Object value;
        private String rootName;
        private String name;
        private String joinName;

        public Operator getOperator() {
            return operator;
        }

        public InnerQuery setOperator(Operator operator) {
            this.operator = operator;
            return this;
        }

        public Object getValue() {
            return value;
        }

        public InnerQuery setValue(Object value) {
            this.value = value;
            return this;
        }

        public String getRootName() {
            return rootName;
        }

        public InnerQuery setRootName(String rootName) {
            this.rootName = rootName;
            return this;
        }

        public String getName() {
            return name;
        }

        public InnerQuery setName(String name) {
            this.name = name;
            return this;
        }

        public String getJoinName() {
            return joinName;
        }

        public InnerQuery setJoinName(String joinName) {
            this.joinName = joinName;
            return this;
        }
    }

    /**
     * 内部操作因子
     */
    public enum Operator {
        /**
         * EQ 就是 EQUAL等于
         * NE就是 NOT EQUAL不等于
         * GT 就是 GREATER THAN大于
         * LT 就是 LESS THAN小于
         * GE 就是 GREATER THAN OR EQUAL 大于等于
         * LE 就是 LESS THAN OR EQUAL 小于等于
         */
        EQ, NE, LIKE, NOT_LIKE, GT, GE, LT, LE, IN, NOT_IN, IS_NULL, IS_NOT_NULL;

    }

}
