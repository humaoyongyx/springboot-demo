package issac.study.cache.schedule;

import issac.study.cache.core.config.DynamicConfig;
import issac.study.cache.properties.DConfProp;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author issac.hu
 */
@Component
public class TestSchedule {

    DConfProp dConfProp = DynamicConfig.get(DConfProp.class);

    @Scheduled(fixedRate = 1000 * 60)
    public void schedule() {
        System.out.println(dConfProp.getHello());
    }
}
