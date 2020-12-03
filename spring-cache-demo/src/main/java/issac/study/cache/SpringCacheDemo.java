package issac.study.cache;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@MapperScan("issac.study.cache.mapper")
@EnableScheduling
public class SpringCacheDemo {

    public static void main(String[] args) {
        SpringApplication.run(SpringCacheDemo.class, args);
    }

}
