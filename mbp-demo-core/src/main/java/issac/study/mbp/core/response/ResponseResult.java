package issac.study.mbp.core.response;

import io.swagger.annotations.ApiModelProperty;
import issac.study.mbp.core.constant.ErrorCodeConstant;
import issac.study.mbp.core.locale.MessageUtils;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author issac.hu
 */
@Data
@Accessors(chain = true)
public class ResponseResult implements ErrorCodeConstant {

    @ApiModelProperty("请求是否成功")
    private Boolean success;

    @ApiModelProperty("请求返回补充消息")
    private String msg;

    @ApiModelProperty("错误码")
    private Integer errorCode;

    @ApiModelProperty("数据")
    private Object data;

    public static ResponseResult success() {
        return success(null);
    }

    public static ResponseResult success(Object data) {
        ResponseResult responseResult = new ResponseResult();
        responseResult.setSuccess(true);
        responseResult.setErrorCode(DEFAULT_SUCCESS_ERROR_CODE);
        responseResult.setMsg(DEFAULT_SUCCESS_MSG);
        responseResult.setData(data);
        return responseResult;
    }

    public static ResponseResult fail(String msg, Object... i18nArgs) {
        return fail(DEFAULT_FAIL_ERROR_CODE, msg, i18nArgs);
    }

    public static ResponseResult fail(Integer errorCode, String msg, Object... i18nArgs) {
        ResponseResult responseResult = new ResponseResult();
        responseResult.setSuccess(false);
        responseResult.setErrorCode(errorCode);
        responseResult.setMsg(MessageUtils.getOrElseReturnKey(msg, i18nArgs));
        return responseResult;
    }

}
