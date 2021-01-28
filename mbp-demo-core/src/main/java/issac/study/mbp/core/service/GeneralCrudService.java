package issac.study.mbp.core.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import issac.study.mbp.core.model.GeneralModel;
import issac.study.mbp.core.req.BasePageReq;
import issac.study.mbp.core.req.BaseReq;

import java.util.List;


/**
 * @author issac.hu
 */
public interface GeneralCrudService<T extends GeneralModel, V> extends IService<T>, CacheableService {

    /**
     * 保存实体,且不做任何验证
     *
     * @param model
     * @return
     */
    T saveModel(T model);

    /**
     * 单条新增且获取保存后的值
     *
     * @param baseReq
     * @return
     */
    V save(BaseReq baseReq);

    /**
     * 更新实体，不做任何验证
     *
     * @param model
     * @return
     */
    T updateModel(T model);

    /**
     * 更新实体，不做任何验证
     *
     * @param model
     * @param includeEmptyValue 是否包含非空值，对于string是非空，对于其他类型是null
     * @return
     */
    T updateModel(T model, boolean includeEmptyValue);

    /**
     * 单条更新且获取更新后的值
     *
     * @param baseReq
     * @return
     */
    V update(BaseReq baseReq);

    /**
     * 单条更新且获取更新后的值
     *
     * @param baseReq
     * @param includeEmptyValue 是否包含非空值，对于string是非空，对于其他类型是null
     * @return
     */
    V update(BaseReq baseReq, boolean includeEmptyValue);

    /**
     * 分页
     *
     * @param baseReq
     * @param basePageReq
     * @return
     */
    Page<V> page(BaseReq baseReq, BasePageReq basePageReq);

    /**
     * 通过id获取值
     *
     * @param id
     * @return
     */
    V getById(Integer id);

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    Integer deleteById(Integer id);

    /**
     * 验证请求参数 相当于@valid
     *
     * @param req
     */
    void validateReq(Object req);

    /**
     * 验证请求参数 相当于@valid
     *
     * @param req
     * @param checkNone 是否检查参数为null
     */
    void validateReq(Object req, boolean checkNone);

    /**
     * 列表参数验证 相当于@valid
     *
     * @param reqs
     * @param checkNone
     */
    void validateReqs(List<Object> reqs, boolean checkNone);
}
