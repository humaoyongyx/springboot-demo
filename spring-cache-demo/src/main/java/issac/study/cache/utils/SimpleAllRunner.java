package issac.study.cache.utils;

/**
 * @author issac.hu
 */
@FunctionalInterface
public interface SimpleAllRunner {
    /**
     * 运行
     *
     * @param tenantId
     */
    void run(String tenantId);
}
