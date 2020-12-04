package issac.study.cache.redis;

import issac.study.cache.base.BaseServiceTest;
import issac.study.cache.contract.DemoRedisKey;
import issac.study.cache.core.redis.RedisCrudService;
import issac.study.cache.utils.MapParam;
import issac.study.cache.vo.UserInfoVo;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author issac.hu
 */
public class RedisServiceTest extends BaseServiceTest {

    @Autowired
    RedisCrudService redisCrudService;

    DemoRedisKey demoRedisKey = new DemoRedisKey();

    @Test
    public void testSet() {
        redisCrudService.set(demoRedisKey.name("testBoolean"), true);
        redisCrudService.set(demoRedisKey.name("testDate"), new Date());
        redisCrudService.set(demoRedisKey.name("testLocalDateTime"), LocalDateTime.now());
        redisCrudService.set(demoRedisKey.name("testObj"), new UserInfoVo());
        redisCrudService.set(demoRedisKey.name("testArray"), Arrays.asList(1, 2, 3));
        redisCrudService.set(demoRedisKey.name("testTtl"), "with Ttl", 60 * 60L);
        redisCrudService.setMapItem(demoRedisKey.name("testMapItem"), "itemKey", "itemValue");
        redisCrudService.setMap(demoRedisKey.name("testMap"), MapParam.build().put("key1", 1).put("key2", "你好").put("key3", new Date()).toMap());
    }

    @Test
    public void testExpire() {
        redisCrudService.expire(demoRedisKey.name("testBoolean"), 60 * 60 * 24L);
    }

    @Test
    public void testGet() {
        Boolean test = redisCrudService.get(demoRedisKey.name("testBoolean"), Boolean.class);
        System.out.println(test);
        List<Integer> testArray = redisCrudService.getList(demoRedisKey.name("testArray"), Integer.class);
        System.out.println(testArray);
        String mapValue = redisCrudService.getMapItem(demoRedisKey.name("testMapItem"), "itemKey");
        System.out.println(mapValue);
        Map testMap = redisCrudService.getMap(demoRedisKey.name("testMap"));
        System.out.println(testMap);
        Date mapItem = redisCrudService.getMapItem(demoRedisKey.name("testMap"), "key3", Date.class);
        System.out.println(mapItem);
    }

    @Test
    public void testIncr() {
        long testInc = redisCrudService.incr(demoRedisKey.name("testInc"));
        System.out.println(testInc);
        long incrMapItem = redisCrudService.incrMapItem(demoRedisKey.name("testMapItem"), "itemIncr");
        System.out.println(incrMapItem);
    }


}
