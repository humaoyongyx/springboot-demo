package issac.study.cache.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 内部调度器调用
 *
 * @author issac.hu
 */
public class AsyncInnerScheduleRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(AsyncInnerScheduleRunner.class);

    private static ExecutorService executorService = new ThreadPoolExecutor(0, Integer.MAX_VALUE,
            60L, TimeUnit.SECONDS,
            new SynchronousQueue<Runnable>());

    /**
     * 内部线程跑单个租户
     *
     * @param simpleRunner
     */
    public static void runSingle(SimpleRunner simpleRunner) {
        executorService.execute(() -> {
            try {
                LOGGER.debug("AsyncInnerScheduleRunner begin");
                simpleRunner.run();
                LOGGER.debug("AsyncInnerScheduleRunner end");
            } catch (Exception e) {
                LOGGER.error("AsyncInnerScheduleRunner error:", e);
            }
        });
    }


}
