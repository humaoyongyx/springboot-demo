package issac.study.mbp.core.exception;


import issac.study.mbp.core.locale.MessageUtils;

/**
 * 支持国际化的消息，msg被{}包含的将交由国际化message处理
 *
 * @author issac.hu
 */
public enum CoreErrorCode implements IErrorCode {

    //业务类异常
    CORE_PARAM_NULL_ERROR(10000, "core.param.null.error");

    private Integer errorCode;
    private String msg;
    private Boolean i18n;

    CoreErrorCode(Integer errorCode, String msg) {
        this.errorCode = errorCode;
        this.msg = msg;
        this.i18n = true;
    }

    CoreErrorCode(Integer errorCode, String msg, boolean i18n) {
        this.errorCode = errorCode;
        this.msg = msg;
        this.i18n = i18n;
    }


    @Override
    public Integer getErrorCode() {
        return errorCode;
    }

    @Override
    public String getMsg() {
        if (this.i18n) {
            return MessageUtils.get(msg);
        }
        return msg;
    }
}
