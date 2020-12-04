package issac.study.cache.contract;

import issac.study.cache.core.redis.RootRedisKey;

/**
 * @author issac.hu
 */
public class DemoRedisKey extends RootRedisKey {
    {
        addModuleKey("demo");
    }

    public static void main(String[] args) {
        DemoRedisKey demoRedisKey = new DemoRedisKey();
        System.out.println(demoRedisKey.name("sd").name("dd","s").getKey());
        System.out.println(demoRedisKey.getKey());

    }
}
