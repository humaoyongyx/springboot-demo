package issac.study.cache.config;

import issac.study.cache.core.jpa.JpaQueryTemplate;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import javax.persistence.EntityManagerFactory;

/**
 * @author issac.hu
 */
@Configuration
@EnableJpaAuditing
@EnableJpaRepositories("issac.study.cache.repository")
@EntityScan("issac.study.cache.model")
public class JpaConfig {

    @Bean
    public JpaQueryTemplate jpaQueryTemplate(EntityManagerFactory entityManagerFactory) {
        return new JpaQueryTemplate(entityManagerFactory);
    }

}
