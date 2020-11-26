package issac.study.mybatisjpa;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@MapperScan("issac.study.mybatisjpa.mapper")
@EnableJpaRepositories("issac.study.mybatisjpa.repository")
@EntityScan("issac.study.mybatisjpa.model")
@EnableJpaAuditing
public class MybatisJpaDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(MybatisJpaDemoApplication.class, args);
    }

}
