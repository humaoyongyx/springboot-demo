package issac.study.cache.config;

import issac.study.cache.exception.BusinessRuntimeException;
import issac.study.cache.vo.response.ResponseVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * <p>Title: GlobalExceptionHandler</p>
 * 全局异常拦截器
 *
 * @author Xuly
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);


    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ResponseVo customHandler(Exception e) {
        LOGGER.error("global ex:", e);
        return ResponseVo.fail("系统异常，请联系管理员！");
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BindException.class)
    public ResponseVo BindExceptionHandler(BindException e) {
        LOGGER.error("global BindException:", e);
        return ResponseVo.fail("参数类型错误！");
    }


    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseVo MethodArgumentNotValidException(MethodArgumentNotValidException e) {
        LOGGER.error("global MethodArgumentNotValidException:", e);
        return ResponseVo.fail(e.getMessage());
    }


    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(BusinessRuntimeException.class)
    public ResponseVo BusinessRuntimeExceptionHandler(BusinessRuntimeException e) {
        LOGGER.error("global BusinessRuntimeException:", e);
        return ResponseVo.fail(e.getErrorCode(), e.getMsg());
    }

}
