package issac.study.cache.core.jpa.base;

import issac.study.cache.model.base.BaseTreeEntity;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;

/**
 * @author issac.hu
 */
@NoRepositoryBean
public interface BaseTreeJpaRepository<T extends BaseTreeEntity, ID extends Serializable> extends BaseJpaRepository<T, ID> {

    int countByParentId(Integer parentId);
}
