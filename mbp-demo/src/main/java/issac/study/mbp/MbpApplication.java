package issac.study.mbp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MbpApplication {

    public static void main(String[] args) {
        SpringApplication.run(MbpApplication.class, args);
    }

}
