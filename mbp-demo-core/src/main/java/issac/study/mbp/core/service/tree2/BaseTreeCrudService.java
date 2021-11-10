//package issac.study.mbp.core.service.tree2;
//
//
//import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
//import issac.study.mbp.core.model.tree2.BaseTreeModel;
//import issac.study.mbp.core.req.BaseTreeReq;
//import issac.study.mbp.core.service.GeneralCrudService;
//import issac.study.mbp.core.vo.BaseTreeResp;
//
//import java.util.List;
//import java.util.function.Consumer;
//
///**
// * 树形结构的基础接口
// *
// * @author issac.hu
// */
//public interface BaseTreeCrudService<T extends BaseTreeModel, V extends BaseTreeResp> extends GeneralCrudService<T, V> {
//
//    Integer ROOT_DEPTH = 1;
//    String ID_PATH_DELIMITER = "-";
//    Integer SORT_FIRST_START = 1;
//    Integer INIT_CHILD_SORT = 1;
//    String LIKE_SUFFIX = "%";
//    Integer LEAF_FALSE = 0;
//    Integer LEAF_TRUE = 1;
//    Long ROOT_PARENT_ID = 0L;
//
//    /**
//     * 支持树的新增以及新增插入到任一节点之下
//     * 其中：parentId表示：要新增的父节点id，如果为空，表示新增到根节点下
//     * 其中：nextId表示：新增的时候，要插入到这个节点之前的节点id；如果为空，表示追加到父节点下的最后一个节点
//     *
//     * @param baseTreeReq
//     * @return
//     */
//    V saveTree(BaseTreeReq baseTreeReq);
//
//    /**
//     * 支持树的拖拽插入到某个节点之前，注意不支持跨树（也即跨根节点）的拖拽操作
//     * 如果是要拖拽的插入的话，parentId不能为null，如果为null，只会更新树的业务属性
//     * 其中：nextId表示：拖拽的时候，要插入到这个节点之前的节点id；如果为空，表示追加到父节点下的最后一个节点
//     *
//     * @param baseTreeReq
//     * @return
//     */
//    V updateTree(BaseTreeReq baseTreeReq);
//
//    /**
//     * 支持树的拖拽插入到某个节点之前，注意不支持跨树（也即跨根节点）的拖拽操作
//     * 如果是要拖拽的插入的话，parentId不能为null，如果为null，只会更新树的业务属性
//     * 其中：nextId表示：拖拽的时候，要插入到这个节点之前的节点id；如果为空，表示追加到父节点下的最后一个节点
//     *
//     * @param baseTreeReq
//     * @param includeEmptyValue 是否包含非空值，对于string是非空，对于其他类型是null
//     * @return
//     */
//    V updateTree(BaseTreeReq baseTreeReq, boolean includeEmptyValue);
//
//    /**
//     * 只是更新数据，且不包括树的变动
//     *
//     * @param baseTreeReq
//     * @return
//     */
//    V updateNoTree(BaseTreeReq baseTreeReq);
//
//    /**
//     * 只是更新数据，且不包括树的变动
//     *
//     * @param baseTreeReq
//     * @param includeEmptyValue
//     * @return
//     */
//    V updateNoTree(BaseTreeReq baseTreeReq, boolean includeEmptyValue);
//
//    /**
//     * 通过id删除树数据，如果包含子节点会报错
//     *
//     * @param id
//     * @return
//     */
//    Long deleteById(Long id);
//
//    /**
//     * 通过id删除树数据，支持递归删除
//     *
//     * @param id
//     * @param deeper 是否递归删除子节点
//     * @return
//     */
//    Long deleteById(Long id, boolean deeper);
//
//    /**
//     * 递归复制树，并且置于nextId节点的上层
//     * 注意使用此方法，不可使用DynamicSql生产的语句；如果非要使用，需要将Dynamic生成的insert语句去掉
//     *
//     * @param req
//     * @param nextId
//     */
//    void copyTreeUp(BaseTreeReq req, Long nextId);
//
//    /**
//     * 递归复制树，并且置于nextNode节点的上层
//     * 注意使用此方法，不可使用DynamicSql生产的语句；如果非要使用，需要将Dynamic生成的insert语句去掉
//     *
//     * @param req
//     * @param nextNode
//     * @param nextNodeChildrenList
//     */
//    void copyTreeUp(BaseTreeReq req, T nextNode, List<T> nextNodeChildrenList);
//
//    /**
//     * 查询树形结构
//     *
//     * @param baseTreeReq
//     * @return
//     */
//    List<V> tree(BaseTreeReq baseTreeReq);
//
//    /**
//     * 查询树形结构
//     *
//     * @param baseTreeReq
//     * @param nodeConsumer 处理返回的节点
//     * @return
//     */
//    List<V> tree(BaseTreeReq baseTreeReq, Consumer<V> nodeConsumer);
//
//    /**
//     * @param baseTreeReq
//     * @param queryWrapper 查询条件
//     * @return
//     */
//    List<V> tree(BaseTreeReq baseTreeReq, QueryWrapper<T> queryWrapper);
//
//    /**
//     * @param baseTreeReq
//     * @param queryWrapper 查询条件
//     * @param nodeConsumer 处理返回的节点
//     * @return
//     */
//    List<V> tree(BaseTreeReq baseTreeReq, QueryWrapper<T> queryWrapper, Consumer<V> nodeConsumer);
//}
