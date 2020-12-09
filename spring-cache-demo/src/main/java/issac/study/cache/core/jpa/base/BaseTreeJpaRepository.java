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

    int countByParentId(Integer parentId);

    /**
     * 通过idPath删除
     *
     * @param idPath
     */
    @Modifying
    @Transactional
    //这里写hql比它生成的效率要高，由于加了乐观锁，因此删除的时候，是先查询在删除的（未验证）
    @Query(value = "delete from #{#entityName} where idPath like ?1")
    void deleteByIdPathIsLike(String idPath);
}
