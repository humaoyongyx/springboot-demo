package issac.study.cache.core.service;

import issac.study.cache.core.jpa.JpaQuerySpecification;
import issac.study.cache.exception.BusinessRuntimeException;
import issac.study.cache.exception.ErrorCode;
import issac.study.cache.model.base.BaseEntity;
import issac.study.cache.model.base.BaseTreeEntity;
import issac.study.cache.req.base.BaseReq;
import issac.study.cache.utils.ConvertUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;


/**
 * 参考 https://www.cnblogs.com/mzq123/p/12629142.html
 *
 * @author issac.hu
 */
public abstract class AbstractBaseCrudServiceImpl implements BaseCrudService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractBaseCrudServiceImpl.class);

    @Autowired
    Validator validator;

    protected abstract Class<? extends BaseEntity> entityClass();

    @Override
    public boolean cacheable() {
        return false;
    }

    /**
     * 保存
     *
     * @param baseReq
     * @param vClass
     * @param <V>
     * @return
     */
    @CachePut(value = "cacheDemo", key = "targetClass+''+#result.id", condition = "target.cacheable()")
    @Override
    public <V> V save(BaseReq baseReq, Class<V> vClass) {
        Objects.requireNonNull(baseReq, "保存的对象不能为空");
        baseReq.setId(null);
        BaseEntity baseEntity = ConvertUtils.convertObject(baseReq, entityClass());
        Object db = baseJpaRepository().save(baseEntity);
        return ConvertUtils.convertObject(db, vClass);
    }


    /**
     * 通过id删除
     *
     * @param id
     */
    @CacheEvict(value = "cacheDemo", key = "targetClass+''+#id", condition = "target.cacheable()")
    @Override
    public Integer deleteById(Integer id) {
        try {
            baseJpaRepository().deleteById(id);
            return id;
        } catch (Exception e) {
            LOGGER.warn("deleteById:{}", e.getMessage());
        }
        return null;
    }

    /**
     * 更新
     *
     * @param baseReq
     * @param vClass
     * @param <V>
     * @return
     */
    @Override
    public <V> V update(BaseReq baseReq, Class<V> vClass) {
        return update(baseReq, vClass, false);
    }

    /**
     * 更新
     *
     * @param baseReq
     * @param vClass
     * @param includeNullValue 是否包含值为null的属性
     * @param <V>
     * @return
     */
    @CachePut(value = "cacheDemo", key = "targetClass+''+#p0.id", condition = "target.cacheable()")
    @Override
    public <V> V update(BaseReq baseReq, Class<V> vClass, boolean includeNullValue) {
        Object db = checkReqForUpdate(baseReq, includeNullValue);
        Object updateDb = baseJpaRepository().save(db);
        return ConvertUtils.convertObject(updateDb, vClass);
    }

    protected BaseTreeEntity checkReqForUpdate(BaseReq baseReq, boolean includeNullValue) {
        Objects.requireNonNull(baseReq, "更新的对象不能为空");
        if (baseReq.getId() == null) {
            throw BusinessRuntimeException.error("id不能为空");
        }
        Optional byId = baseJpaRepository().findById(baseReq.getId());
        if (!byId.isPresent()) {
            throw BusinessRuntimeException.error("更新的数据不存在");
        }
        Object db = byId.get();
        if (includeNullValue) {
            ConvertUtils.copyWithNullProperties(baseReq, db);
        } else {
            ConvertUtils.copyNotNullProperties(baseReq, db);
        }
        return (BaseTreeEntity) db;
    }

    /**
     * 返回Option的值
     *
     * @param id
     * @param vClass
     * @param <V>
     * @return
     */
    @Override
    public <V> Optional<V> findById(Object id, Class<V> vClass) {
        Optional byId = baseJpaRepository().findById(id);
        return ConvertUtils.toOpVo(byId, vClass);
    }

    /**
     * 获取id
     *
     * @param id
     * @param vClass
     * @param <V>
     * @return
     */

    @Cacheable(value = "cacheDemo", key = "targetClass+''+#id", condition = "target.cacheable()")
    @Override
    public <V> V getById(Object id, Class<V> vClass) {
        Optional byId = baseJpaRepository().findById(id);
        return ConvertUtils.convertOptionObject(byId, vClass);
    }

    /**
     * 快速分页
     *
     * @param baseReq
     * @param pageable
     * @param vClass
     * @param <V>
     * @return
     */
    @Override
    public <V> Page<V> page(BaseReq baseReq, Pageable pageable, Class<V> vClass) {
        return page(baseReq, pageable, vClass, null);
    }

    /**
     * 分页接口
     *
     * @param baseReq
     * @param pageable
     * @param vClass
     * @param specification
     * @param <V>
     * @return
     */
    @Override
    public <V> Page<V> page(BaseReq baseReq, Pageable pageable, Class<V> vClass, Specification specification) {
        if (specification == null) {
            specification = JpaQuerySpecification.and(baseReq, entityClass());
        }
        Page page = baseJpaRepository().findAll(specification, pageable);
        return new PageImpl<>(ConvertUtils.convertList(page.getContent(), vClass), pageable, page.getTotalElements());
    }


    @Override
    public void validateReq(Object req) {
        validateReq(req, true);
    }

    @Override
    public void validateReq(Object req, boolean checkNone) {
        if (checkNone) {
            if (req == null) {
                throw BusinessRuntimeException.errorCode(ErrorCode.COMMON_PARAM_NULL_ERROR);
            }
        }
        Set<ConstraintViolation<Object>> validate = validator.validate(req);
        if (!validate.isEmpty()) {
            ConstraintViolation<Object> next = validate.iterator().next();
            throw BusinessRuntimeException.error(next.getPropertyPath() + " " + next.getMessage());
        }
    }
}
