package issac.study.cache.repository;

import issac.study.cache.core.jpa.base.BaseJpaRepository;
import issac.study.cache.model.ConfigEntity;

/**
 * @author issac.hu
 */
public interface ConfigRepository extends BaseJpaRepository<ConfigEntity, Integer> {

    ConfigEntity findBycKey(String cKey);
}
