package issac.study.cache.aop;

import issac.study.cache.core.annotation.Log;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author issac.hu
 */
@Aspect
@Component
public class LogPrintAspect {

    private static final Logger LOGGER = LoggerFactory.getLogger(LogPrintAspect.class);

    @Around(value = "@annotation(log)", argNames = "joinPoint,log")
    public Object handleLogAspect(ProceedingJoinPoint joinPoint, Log log) throws Throwable {
        Object[] args = joinPoint.getArgs(); //参数
        Signature signature = joinPoint.getSignature();//方法签名
        long begin = System.currentTimeMillis();
        //方法执行前
        LOGGER.info("method:{},param:{},descp:{},time:{}", signature, args, log.value());
        //方法执行
        Object proceed = joinPoint.proceed();
        //方法执行
        long end = System.currentTimeMillis();
        return proceed;
    }
}
