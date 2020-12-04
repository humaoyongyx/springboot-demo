package issac.study.cache.core.redis;

/**
 * @author issac.hu
 */
public interface RedisKey {

    String DELIMITER = ":";

    /**
     * 获取全路径的key
     *
     * @return
     */
    String getKey();

    /**
     * key的父路径，即module的路径
     *
     * @return
     */
    String getModuleKey();
}
