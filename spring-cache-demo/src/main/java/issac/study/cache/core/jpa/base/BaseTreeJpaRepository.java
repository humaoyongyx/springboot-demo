package issac.study.cache.core.jpa.base;

import issac.study.cache.model.base.BaseTreeEntity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;

/**
 * @author issac.hu
 */
@NoRepositoryBean
public interface BaseTreeJpaRepository<T extends BaseTreeEntity, ID extends Serializable> extends BaseJpaRepository<T, ID> {

    /**
     * 查询父类子项的数量
     *
     * @param parentId 父id
     * @return
     */
    int countByParentId(Integer parentId);

    /**
     * 通过idPath删除
     *
     * @param idPath id路径
     */
    @Modifying
    @Transactional
    //这里写hql比它生成的效率要高，由于加了乐观锁，因此删除的时候，是先查询在删除的（未验证）
    @Query(value = "delete from #{#entityName} where idPath like ?1")
    void deleteByIdPathIsLike(String idPath);

    /**
     * 更新父项为叶子节点
     */
    @Modifying
    @Transactional
    @Query(value = "update #{#entityName} set leaf = 1 where id = ?1")
    void updateParentToLeaf(Integer id);

    @Modifying
    @Transactional
    @Query(value = "update #{#entityName} set childSeq = ?1 ,leaf = ?2 where id = ?3")
    void updateChildSeqAndLeafById(Integer childSeq, Boolean leaf, Integer id);

    @Modifying
    @Transactional
    @Query(value = "update #{#entityName} set rootId = ?1 where id = ?2")
    void updateRootIdById(Integer rootId, Integer id);

    @Modifying
    @Transactional
    @Query(value = "update #{#entityName} set parentId = ?1 where id = ?2")
    void updateParentIdById(Integer parentId, Integer id);

    /**
     * 更新一棵树，通过偏移量
     *
     * @param depthIncr
     * @param currentIdPath
     * @param newIdPath
     * @param childIdPath
     */
    @Modifying
    @Transactional
    @Query(value = "update #{#entityName} set depth = depth + ?1 , id_path = REPLACE(id_path , ?2 , ?3) where id_path like ?4")
    void updateTreeByIncr(Integer depthIncr, String currentIdPath, String newIdPath, String childIdPath);

}
