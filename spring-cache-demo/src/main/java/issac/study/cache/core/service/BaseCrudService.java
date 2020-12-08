package issac.study.cache.core.service;

import issac.study.cache.core.jpa.base.BaseJpaRepository;
import issac.study.cache.req.base.BaseReq;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.Optional;

/**
 * @author issac.hu
 */
public interface BaseCrudService extends CacheableService {

    /**
     * 获取repository
     *
     * @return
     */
    BaseJpaRepository baseJpaRepository();

    /**
     * 新增
     *
     * @param baseReq
     * @param vClass
     * @param <V>
     * @return
     */
    <V> V save(BaseReq baseReq, Class<V> vClass);

    /**
     * 删除
     *
     * @param id
     */
    Object deleteById(Object id);

    /**
     * 更新接口
     *
     * @param baseReq
     * @param vClass
     * @param <V>
     * @return
     */
    <V> V update(BaseReq baseReq, Class<V> vClass);

    /**
     * 修改
     *
     * @param baseReq
     * @param vClass
     * @param includeNullValue
     * @param <V>
     * @return
     */
    <V> V update(BaseReq baseReq, Class<V> vClass, boolean includeNullValue);

    /**
     * 返回optional vo
     *
     * @param id
     * @param vClass
     * @param <V>
     * @return
     */
    <V> Optional<V> findById(Object id, Class<V> vClass);

    /**
     * 通过id获取
     *
     * @param id
     * @param vClass
     * @param <V>
     * @return
     */
    <V> V getById(Object id, Class<V> vClass);

    /**
     * 快速分页
     *
     * @param baseReq
     * @param pageable
     * @param vClass
     * @param <V>
     * @return
     */
    <V> Page<V> page(BaseReq baseReq, Pageable pageable, Class<V> vClass);

    /**
     * 条件分页
     *
     * @param baseReq
     * @param pageable
     * @param vClass
     * @param specification
     * @param <V>
     * @return
     */
    <V> Page<V> page(BaseReq baseReq, Pageable pageable, Class<V> vClass, Specification specification);

    /**
     * 手动验证
     *
     * @param req
     */
    void validateReq(Object req);

    /**
     * 手动验证
     *
     * @param req
     * @param checkNone 是否验证参数为null
     */
    void validateReq(Object req, boolean checkNone);
}
