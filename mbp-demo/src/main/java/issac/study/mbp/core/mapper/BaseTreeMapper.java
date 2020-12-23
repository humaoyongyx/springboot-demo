package issac.study.mbp.core.mapper;

import issac.study.mbp.core.model.BaseTreeModel;
import org.apache.ibatis.annotations.Param;

/**
 * @author issac.hu
 */
public interface BaseTreeMapper<T extends BaseTreeModel> extends GeneralMapper<T> {

    /**
     * 查询父类子项的数量
     *
     * @param tableName
     * @param parentId
     * @return
     */
    int countByParentId(@Param("tableName") String tableName, @Param("parentId") Integer parentId);

    /**
     * 通过idPath删除
     *
     * @param tableName
     * @param idPath    id路径
     */
    void deleteByIdPathIsLike(@Param("tableName") String tableName, @Param("idPath") String idPath);

    /**
     * 更新节点为叶子
     *
     * @param tableName
     * @param id
     */
    void updateToLeaf(@Param("tableName") String tableName, @Param("id") Integer id);

    /**
     * 通过id更新子节点顺序和叶子
     *
     * @param tableName
     * @param childSeq
     * @param leaf
     * @param id
     */
    void updateChildSeqAndLeafById(@Param("tableName") String tableName, @Param("childSeq") Integer childSeq, @Param("leaf") Integer leaf, @Param("id") Integer id);

    /**
     * 通过id更新更节点id
     *
     * @param tableName
     * @param rootId
     * @param id
     */
    void updateRootIdById(@Param("tableName") String tableName, @Param("rootId") Integer rootId, @Param("id") Integer id);

    /**
     * 通过id更新父节点id
     *
     * @param tableName
     * @param parentId
     * @param id
     */
    void updateParentIdById(@Param("tableName") String tableName, @Param("parentId") Integer parentId, @Param("id") Integer id);

    /**
     * 更新一棵树，通过偏移量
     *
     * @param tableName
     * @param depthIncr
     * @param currentIdPath
     * @param newIdPath
     * @param childIdPath
     */
    void updateTreeByIncr(@Param("tableName") String tableName, @Param("depthIncr") Integer depthIncr, @Param("currentIdPath") String currentIdPath, @Param("newIdPath") String newIdPath, @Param("childIdPath") String childIdPath);

}
