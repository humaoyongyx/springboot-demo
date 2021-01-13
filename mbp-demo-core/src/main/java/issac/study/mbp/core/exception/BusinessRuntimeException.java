package issac.study.mbp.core.exception;

import issac.study.mbp.core.constant.ErrorCodeConstant;
import lombok.Data;

/**
 * @author humy6
 * @Date: 2019/7/2 8:46
 */
@Data
public class BusinessRuntimeException extends RuntimeException implements ErrorCodeConstant {

    private int errorCode;

    private String msg;

    public static BusinessRuntimeException errorCode(IErrorCode errorCode) {
        return new BusinessRuntimeException(errorCode.getErrorCode(), errorCode.getMsg());
    }

    public static BusinessRuntimeException error(String msg) {
        return errorCode(DEFAULT_FAIL_ERROR_CODE, msg);
    }

    public static BusinessRuntimeException errorCode(int errorCode, String msg) {
        return new BusinessRuntimeException(errorCode, msg);
    }

    private BusinessRuntimeException(int errorCode, String msg) {
        super(msg);
        this.errorCode = errorCode;
        this.msg = msg;
    }

}
