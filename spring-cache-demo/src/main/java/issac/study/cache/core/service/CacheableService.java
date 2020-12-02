package issac.study.cache.core.service;

/**
 * @author issac.hu
 */
public interface CacheableService {
    /**
     * 是否加入缓存
     * 如果为true 使用AbstractBaseCrudServiceImpl的类 的“增删改“和“getById”将为加入到redis缓存中
     *
     * @return
     */
    boolean cacheable();
}
