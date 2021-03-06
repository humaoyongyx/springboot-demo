package issac.study.mbp.core.advice;

import issac.study.mbp.core.exception.BusinessRuntimeException;
import issac.study.mbp.core.exception.Error404RuntimeException;
import issac.study.mbp.core.response.ResponseResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ValidationException;

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
    public ResponseResult exception(Exception e) {
        LOGGER.error("global ex:", e);
        return ResponseResult.failI18n("core.system.error");
    }

    /**
     * 处理参数验证异常
     * valid注解 和 bean上的注解结合
     *
     * @param e
     * @return
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BindException.class)
    public ResponseResult bindException(BindException e) {
        FieldError fieldError = e.getBindingResult().getFieldError();
        LOGGER.error("global BindException:", e);
        return ResponseResult.failI18n("core.validate.bind.exception", fieldError.getField(), fieldError.getRejectedValue(), fieldError.getDefaultMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseResult methodArgumentNotValidException(MethodArgumentNotValidException e) {
        LOGGER.error("global MethodArgumentNotValidException:", e);
        return ResponseResult.fail(e.getMessage());
    }

    /**
     * 处理参数验证异常
     * <p>
     * validate注解controller和当个参数验证
     *
     * @param e
     * @return
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ValidationException.class)
    public ResponseResult validationException(ValidationException e) {
        LOGGER.error("global ValidationException:", e);
        return ResponseResult.fail(e.getMessage());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(BusinessRuntimeException.class)
    public ResponseResult businessRuntimeException(BusinessRuntimeException e) {
        LOGGER.error("global BusinessRuntimeException:", e);
        return ResponseResult.fail(e.getErrorCode(), e.getMsg());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(Error404RuntimeException.class)
    public ResponseResult error404RuntimeException(Error404RuntimeException e) {
        return ResponseResult.fail(HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND.getReasonPhrase());
    }

    /**
     * 接口 method不支持，会报错
     *
     * @param e
     * @return
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseResult httpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        LOGGER.error("global HttpRequestMethodNotSupportedException:", e);
        return ResponseResult.failI18n("core.validate.method.not.supported", e.getMethod());
    }

}
