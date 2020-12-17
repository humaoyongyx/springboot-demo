package issac.study.mbp.core.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import issac.study.mbp.model.base.GeneralModel;
import issac.study.mbp.req.base.BasePageReq;
import issac.study.mbp.req.base.BaseReq;

/**
 * @author issac.hu
 */
public interface GeneralCrudService<T extends GeneralModel, V> extends IService<T> {

    /**
     * 保存实体,且不做任何验证
     *
     * @param entity
     * @return
     */
    T saveEntity(T entity);

    /**
     * 单条新增且获取保存后的值
     *
     * @param baseReq
     * @return
     */
    V saveGet(BaseReq baseReq);

    /**
     * 更新实体，不做任何验证
     *
     * @param entity
     * @return
     */
    T updateEntity(T entity);

    /**
     * 更新实体，不做任何验证
     *
     * @param entity
     * @param includeNullValue 是否包含null值
     * @return
     */
    T updateEntity(T entity, boolean includeNullValue);

    /**
     * 单条更新且获取更新后的值
     *
     * @param baseReq
     * @return
     */
    V updateGet(BaseReq baseReq);

    /**
     * 单条更新且获取更新后的值
     *
     * @param baseReq
     * @param includeNullValue 是否包含null值
     * @return
     */
    V updateGet(BaseReq baseReq, boolean includeNullValue);

    /**
     * 分页
     *
     * @param baseReq
     * @param basePageReq
     * @return
     */
    Page<V> page(BaseReq baseReq, BasePageReq basePageReq);
}
