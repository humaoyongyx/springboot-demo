package issac.study.cache.core.service;

import issac.study.cache.core.jpa.base.BaseTreeJpaRepository;

/**
 * @author issac.hu
 */
public interface BaseTreeService extends BaseCrudService {
    int ROOT_DEPTH = 1;
    String ID_PATH_DELIMITER = "-";
    int SEQ_FIRST_START = 1;
    int INIT_CHILD_SEQ = 0;

    BaseTreeJpaRepository baseJpaRepository();
}
