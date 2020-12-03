package issac.study.cache.config;

import issac.study.cache.core.redis.RedisCurdTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author issac.hu
 */
@Configuration
@MapperScan("issac.study.cache.mapper")
@EnableScheduling
public class GlobalConfig {

    @Bean
    public RedisCurdTemplate redisCurdTemplate(StringRedisTemplate stringRedisTemplate) {
        return new RedisCurdTemplate(stringRedisTemplate);
    }
}
