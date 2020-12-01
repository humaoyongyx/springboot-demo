package issac.study.cache.exception;

/**
 * @author humy6
 * @Date: 2019/7/2 8:46
 */
public class BusinessRuntimeException extends RuntimeException {

    /**
     * 一般类型的错误代码
     */
    public static final int ERROR_CODE_DEFAULT = -1;
    private int errorCode;
    private String msg;


    public static BusinessRuntimeException errorCode(IErrorCode errorCode) {
        return new BusinessRuntimeException(errorCode.getErrorCode(), errorCode.getMsg());
    }

    public static BusinessRuntimeException errorCode(int errorCode, String msg) {
        return new BusinessRuntimeException(errorCode, msg);
    }

    public static BusinessRuntimeException error(String msg) {
        return new BusinessRuntimeException(ERROR_CODE_DEFAULT, msg);
    }

    private BusinessRuntimeException(int errorCode, String msg) {
        super(msg);
        this.errorCode = errorCode;
        this.msg = msg;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
