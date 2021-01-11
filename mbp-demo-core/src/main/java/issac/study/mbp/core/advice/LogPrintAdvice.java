package issac.study.mbp.core.advice;


import issac.study.mbp.core.annotation.PrintLog;
import issac.study.mbp.core.req.BaseReq;
import issac.study.mbp.core.utils.ConvertUtils;
import issac.study.mbp.core.utils.RequestIpUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * 这种拦截器，如果参数没有toString 使用下面的toJson的方法不是太好
 * <p>
 * 通用日志拦截
 */
@Component
@Aspect
@Slf4j
public class LogPrintAdvice {

    @Around(value = "@annotation(printLog)", argNames = "joinPoint,printLog")
    public Object handleLogAspect(ProceedingJoinPoint joinPoint, PrintLog printLog) throws Throwable {

        long beginTime = System.currentTimeMillis();
        Object[] args = joinPoint.getArgs();
        HttpServletRequest request = getHttpServletRequest();
        log.info("request: {} {} ,args : {} ,ip : {}", request.getMethod(), request.getRequestURI(), getArgs(printLog, args), RequestIpUtils.getIpAddress(request));
        Object result = null;
        try {
            result = joinPoint.proceed();
        } catch (Exception e) {
            throw e;
        } finally {
            long endTime = System.currentTimeMillis();
            long costTime = endTime - beginTime;
            if (costTime > printLog.slowMillis()) {
                log.warn("response:slow {} {} ,args : {} ,cost : {} ,result : {}", request.getMethod(), request.getRequestURI(), getArgs(printLog, args), costTime + "ms", getResultStr(printLog, result));
            } else {
                log.info("response: {} {} ,args : {} ,cost : {} ,result : {}", request.getMethod(), request.getRequestURI(), getArgs(printLog, args), costTime + "ms", getResultStr(printLog, result));
            }
        }
        return result;
    }

    private String getResultStr(PrintLog printLog, Object result) {
        String resultStr = "";
        if (printLog.showResult() && result != null) {
            resultStr = "\n" + ConvertUtils.toJsonString(result, true, true, true);
        }
        return resultStr;
    }

    // 不推荐这种打印方式，如果参数是特殊对象 如httpServletRequest或者file等会有问题
    private String getArgs(PrintLog printLog, Object[] args) {

        return printLog.showArgs() ? printArgs(args) : "";
    }

    // 优化参数打印
    private String printArgs(Object[] args) {

        StringBuffer sb = new StringBuffer("[ ");

        if (args != null) {
            int i = 1;
            for (Object arg : args) {
                if (arg instanceof BaseReq) {
                    sb.append(ConvertUtils.toJsonString(arg));
                } else {
                    sb.append(arg);
                }
                if (i < args.length) {
                    sb.append(" , ");
                }
                i++;
            }
        }
        sb.append(" ]");
        return sb.toString();
    }

    private HttpServletRequest getHttpServletRequest() {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return requestAttributes.getRequest();
    }

}
