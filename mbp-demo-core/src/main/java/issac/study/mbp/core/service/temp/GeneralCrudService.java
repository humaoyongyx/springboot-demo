//package issac.study.mbp.core.service.temp;
//
//
//import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
//import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
//import com.baomidou.mybatisplus.extension.service.IService;
//import com.cbim.cube.common.req.BasePageReq;
//import com.cbim.cube.common.req.BaseReq;
//import com.cbim.cube.model.base.GeneralModel;
//import com.github.yulichang.wrapper.MPJLambdaWrapper;
//
//import java.util.List;
//import java.util.function.Consumer;
//
//
///**
// * @author issac.hu
// */
//public interface GeneralCrudService<T extends GeneralModel, V> extends IService<T> {
//
//    String CREATE_TIME = "createTime";
//    String UPDATE_TIME = "lastModifyTime";
//
//    enum DML {
//        INSERT, UPDATE;
//    }
//
//    /**
//     * 保存实体,且不做任何验证
//     *
//     * @param model
//     * @return
//     */
//    T saveModel(T model);
//
//    /**
//     * 批量保存实体
//     * 注意使用此方法，不可使用DynamicSql生产的语句；如果非要使用，需要将Dynamic生成的insert语句去掉
//     *
//     * @param modelList
//     */
//    void saveModelAll(List<T> modelList);
//
//    /**
//     * 单条新增且获取保存后的值
//     *
//     * @param baseReq
//     * @return
//     */
//    V save(BaseReq baseReq);
//
//    /**
//     * 更新实体，不做任何验证
//     *
//     * @param model
//     * @return
//     */
//    T updateModel(T model);
//
//    /**
//     * 更新实体，不做任何验证
//     *
//     * @param model
//     * @param includeEmptyValue 是否包含非空值，对于string是非空，对于其他类型是null
//     * @return
//     */
//    T updateModel(T model, boolean includeEmptyValue);
//
//    /**
//     * 单条更新且获取更新后的值
//     *
//     * @param baseReq
//     * @return
//     */
//    V update(BaseReq baseReq);
//
//    /**
//     * 单条更新且获取更新后的值
//     *
//     * @param baseReq
//     * @param includeEmptyValue 是否包含非空值，对于string是非空，对于其他类型是null
//     * @return
//     */
//    V update(BaseReq baseReq, boolean includeEmptyValue);
//
//    /**
//     * 列表查询
//     *
//     * @param baseReq
//     * @return
//     */
//    List<V> getList(BaseReq baseReq);
//
//    /**
//     * 列表查询 带排序
//     *
//     * @param baseReq
//     * @param order   多个排序可以这样：id,desc;name,asc
//     * @return
//     */
//    List<V> getList(BaseReq baseReq, String order);
//
//    /**
//     * 通过id列表查询
//     *
//     * @param idList
//     * @return
//     */
//    List<T> getListByIds(List<Long> idList);
//
//    /**
//     * 列表查询
//     *
//     * @param baseReq
//     * @param queryWrapper
//     * @return
//     */
//    List<V> getList(BaseReq baseReq, QueryWrapper<T> queryWrapper);
//
//    /**
//     * 分页查询
//     *
//     * @param basePageReq
//     * @return
//     */
//    Page<V> page(BasePageReq basePageReq);
//
//    /**
//     * 分页查询
//     *
//     * @param basePageReq
//     * @param queryWrapper
//     * @return
//     */
//    Page<V> page(BasePageReq basePageReq, QueryWrapper<T> queryWrapper);
//
//    /**
//     * 分页
//     *
//     * @param baseReq
//     * @param basePageReq
//     * @return
//     */
//    Page<V> page(BaseReq baseReq, BasePageReq basePageReq);
//
//    /**
//     * 支持left join查询
//     *
//     * @param basePageReq
//     * @return
//     */
//    Page<V> joinPage(BasePageReq basePageReq);
//
//    /**
//     * 支持left join查询
//     *
//     * @param basePageReq
//     * @param consumer
//     * @return
//     */
//    Page<V> joinPage(BasePageReq basePageReq, Consumer<List<V>> consumer);
//
//    /**
//     * 支持join查询
//     * 注意：mapper需要继承MPJBaseMapper
//     *
//     * @param basePageReq
//     * @param queryWrapper
//     * @return
//     */
//    Page<V> joinPage(BasePageReq basePageReq, MPJLambdaWrapper<T> queryWrapper);
//
//    /**
//     * 支持join查询
//     * 注意：mapper需要继承MPJBaseMapper
//     *
//     * @param basePageReq
//     * @param queryWrapper
//     * @param consumer
//     * @return
//     */
//    Page<V> joinPage(BasePageReq basePageReq, MPJLambdaWrapper<T> queryWrapper, Consumer<List<V>> consumer);
//
//    /**
//     * 通过id获取值
//     *
//     * @param id
//     * @return
//     */
//    V findById(Long id);
//
//    /**
//     * 通过id删除
//     *
//     * @param id
//     * @return
//     */
//    Long deleteById(Long id);
//
//    /**
//     * 验证请求参数 相当于@valid
//     *
//     * @param req
//     */
//    void validateReq(Object req);
//
//    /**
//     * 验证请求参数 相当于@valid
//     *
//     * @param req
//     * @param checkNone 是否检查参数为null
//     */
//    void validateReq(Object req, boolean checkNone);
//
//    /**
//     * 列表参数验证 相当于@valid
//     *
//     * @param reqs
//     * @param checkNone
//     */
//    void validateReqs(List<Object> reqs, boolean checkNone);
//}
