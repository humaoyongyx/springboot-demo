package issac.study.mbp.core.service;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import issac.study.mbp.core.model.BaseTreeModel;
import issac.study.mbp.core.req.BaseTreeReq;

import java.util.List;

/**
 * 树形结构的基础接口
 *
 * @author issac.hu
 */
public interface BaseTreeCrudService<T extends BaseTreeModel, V> extends BaseCrudService<T, V> {
    int ROOT_DEPTH = 1;
    String ID_PATH_DELIMITER = "-";
    int SEQ_FIRST_START = 1;
    int INIT_CHILD_SEQ = 0;
    String LIKE_SUFFIX = "%";
    int LEAF_FALSE = 0;
    int LEAF_TRUE = 1;

    /**
     * 单条保存树形对象
     *
     * @param baseTreeReq
     * @return
     */
    V save(BaseTreeReq baseTreeReq);

    /**
     * 单条更新树形对象
     *
     * @param baseTreeReq
     * @return
     */
    V update(BaseTreeReq baseTreeReq);

    /**
     * 单条更新树形对象
     *
     * @param baseTreeReq
     * @param includeEmptyValue 是否包含非空值，对于string是非空，对于其他类型是null
     * @return
     */
    V update(BaseTreeReq baseTreeReq, boolean includeEmptyValue);

    /**
     * 根据id删除树形对象
     *
     * @param id
     * @return
     */
    Integer deleteById(Integer id);

    /**
     * 根据id删除树形对象
     *
     * @param id
     * @param deeper 是否递归删除子节点
     * @return
     */
    Integer deleteById(Integer id, boolean deeper);

    /**
     * 查询树形结构
     *
     * @param baseTreeReq
     * @return
     */
    List<V> tree(BaseTreeReq baseTreeReq);

    /**
     * @param baseTreeReq
     * @param queryWrapper 查询条件
     * @return
     */
    List<V> tree(BaseTreeReq baseTreeReq, QueryWrapper<T> queryWrapper);
}
