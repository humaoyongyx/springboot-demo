package issac.study.mbp.core.config;

import issac.study.mbp.core.redis.RedisCrudService;
import issac.study.mbp.core.redis.RedisCurdTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * @author issac.hu
 */
@Configuration
public class RedisConfig {

    @Bean
    public RedisCurdTemplate redisCurdTemplate(StringRedisTemplate stringRedisTemplate) {
        return new RedisCurdTemplate(stringRedisTemplate);
    }

    @Bean
    public RedisCrudService redisCrudService(RedisCurdTemplate redisCurdTemplate) {
        return new RedisCrudService(redisCurdTemplate);
    }
}
