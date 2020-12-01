package issac.study.cache;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@MapperScan("issac.study.cache.mapper")
@EnableJpaRepositories("issac.study.cache.repository")
@EntityScan("issac.study.cache.model")
@EnableJpaAuditing
public class SpringCacheDemo {

    public static void main(String[] args) {
        SpringApplication.run(SpringCacheDemo.class, args);
    }

}
