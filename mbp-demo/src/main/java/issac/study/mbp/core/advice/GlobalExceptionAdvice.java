package issac.study.mbp.core.advice;

import issac.study.mbp.core.exception.BusinessRuntimeException;
import issac.study.mbp.core.exception.Error404RuntimeException;
import issac.study.mbp.core.response.ResponseVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器
 *
 * @author issac
 */
@RestControllerAdvice
public class GlobalExceptionAdvice {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionAdvice.class);


    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ResponseVo customHandler(Exception e) {
        LOGGER.error("global ex:", e);
        return ResponseVo.fail("系统异常，请联系管理员！");
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BindException.class)
    public ResponseVo BindExceptionHandler(BindException e) {
        FieldError fieldError = e.getBindingResult().getFieldError();
        LOGGER.error("global BindException:", e);
        String result = "参数验证失败:名称[%s],值[%s],原因[%s]";
        String msg = String.format(result, fieldError.getField(), fieldError.getRejectedValue(), fieldError.getDefaultMessage());
        return ResponseVo.fail(msg);
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

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(Error404RuntimeException.class)
    public ResponseVo Error404RuntimeException(Error404RuntimeException e) {
        return ResponseVo.fail(HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND.getReasonPhrase());
    }

}
