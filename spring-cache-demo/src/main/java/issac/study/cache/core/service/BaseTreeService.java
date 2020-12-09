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
    String LIKE_SUFFIX = "%";

    BaseTreeJpaRepository baseJpaRepository();

    /**
     * 删除节点
     *
     * @param id
     * @param deeper true删除其所有子节点
     * @return
     */
    Object deleteById(Object id, boolean deeper);
}
