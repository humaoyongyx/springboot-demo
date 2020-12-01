package issac.study.cache.exception;

/**
 * @author issac.hu
 */
public enum ErrorCode implements IErrorCode {

    //业务类异常
    COMMON_PARAM_NULL_ERROR(1, "参数不能为空");

    private Integer errorCode;
    private String msg;

    ErrorCode(Integer errorCode, String msg) {
        this.errorCode = errorCode;
        this.msg = msg;
    }


    @Override
    public Integer getErrorCode() {
        return errorCode;
    }

    @Override
    public String getMsg() {
        return msg;
    }
}
