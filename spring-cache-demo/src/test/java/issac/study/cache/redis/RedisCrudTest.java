package issac.study.cache.redis;

import issac.study.cache.base.BaseServiceTest;
import issac.study.cache.core.redis.RedisCurdTemplate;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author issac.hu
 */
public class RedisCrudTest extends BaseServiceTest {

    @Autowired
    RedisCurdTemplate redisCurdTemplate;

    @Test
    public void testGet() {
        String testKey = String.format("testKey:%s", "test");
        String result = redisCurdTemplate.get(testKey);
        System.out.println(result);
    }

    @Test
    public void testSave() {
        String testKey = String.format("testKey:other:%s", "test");
        redisCurdTemplate.set(testKey, testKey);
    }

    @Test
    public void testHashSet() {
        String testKey = String.format("testKey:%s", "testHash");
        redisCurdTemplate.hset(testKey, "key1", "value1");
    }


}
