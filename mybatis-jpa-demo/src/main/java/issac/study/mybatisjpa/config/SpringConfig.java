package issac.study.mybatisjpa.config;

import issac.study.mybatisjpa.core.jpa.JpaQueryTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManagerFactory;

/**
 * @author issac.hu
 */
@Configuration
public class SpringConfig {

    @Bean
    public JpaQueryTemplate jpaQueryTemplate(EntityManagerFactory entityManagerFactory) {
        return new JpaQueryTemplate(entityManagerFactory);
    }
}
