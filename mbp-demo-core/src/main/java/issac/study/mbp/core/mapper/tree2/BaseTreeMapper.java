package issac.study.mbp.core.mapper.tree2;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import issac.study.mbp.core.model.BaseTreeModel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
 * @author issac.hu
 */
@Mapper
public interface BaseTreeMapper<T extends BaseTreeModel> extends BaseMapper<T> {

    /**
     * 查询父类子项的数量
     *
     * @param tableName
     * @param parentId
     * @return
     */
    int countByParentId(@Param("tableName") String tableName, @Param("parentId") Long parentId);

    /**
     * 查询父类子项的sort
     * 目前用于根节点的sort计算
     *
     * @param tableName
     * @param parentId
     * @return
     */
    Integer maxSortByParentId(@Param("tableName") String tableName, @Param("parentId") Long parentId);

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
    void updateToLeaf(@Param("tableName") String tableName, @Param("id") Long id);

    /**
     * 通过id更新子节点顺序和叶子
     *
     * @param tableName
     * @param incr      偏移量
     * @param leaf
     * @param id
     */
    void updateChildSortAndLeafById(@Param("tableName") String tableName, @Param("incr") Integer incr, @Param("leaf") Integer leaf, @Param("id") Long id);

    /**
     * 通过id更新更节点id
     *
     * @param tableName
     * @param rootId
     * @param id
     */
    void updateRootIdById(@Param("tableName") String tableName, @Param("rootId") Long rootId, @Param("id") Long id);

    /**
     * 通过id更新父节点id
     *
     * @param tableName
     * @param parentId
     * @param id
     */
    void updateParentIdById(@Param("tableName") String tableName, @Param("parentId") Long parentId, @Param("id") Long id);

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

    /**
     * 更新下个节点的顺序+1
     *
     * @param tableName
     * @param nextSort
     * @param parentId
     */
    void incrNextSort(@Param("tableName") String tableName, @Param("nextSort") Integer nextSort, @Param("parentId") Long parentId);

    /**
     * 更新节点为非叶子节点
     *
     * @param tableName
     * @param id
     */
    void updateToNonLeaf(@Param("tableName") String tableName, @Param("id") Long id);

    /**
     * 获取第一个子节点
     *
     * @param tableName
     * @param parentId
     * @return
     */
    Map<String, Object> getFirstChildByParentId(@Param("tableName") String tableName, @Param("parentId") Long parentId);

}
