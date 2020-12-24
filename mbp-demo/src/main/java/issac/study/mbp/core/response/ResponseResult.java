package issac.study.mbp.core.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author issac.hu
 */
@Data
@Accessors(chain = true)
public class ResponseResult {

    public static final int SUCCESS_ERROR_CODE = 0;
    public static final int FAIL_ERROR_CODE = -1;
    public static final String SUCCESS_MSG = "SUCCESS";

    @ApiModelProperty("请求是否成功")
    private Boolean success;

    @ApiModelProperty("请求返回补充消息")
    private String msg;

    @ApiModelProperty("错误码")
    private Integer errorCode;

    @ApiModelProperty("数据")
    private Object data;

    public static ResponseResult success() {
        ResponseResult responseResult = new ResponseResult();
        responseResult.setSuccess(true);
        responseResult.setErrorCode(SUCCESS_ERROR_CODE);
        responseResult.setMsg(SUCCESS_MSG);
        return responseResult;
    }

    public static ResponseResult success(Object data) {
        ResponseResult responseResult = new ResponseResult();
        responseResult.setSuccess(true);
        responseResult.setErrorCode(SUCCESS_ERROR_CODE);
        responseResult.setMsg(SUCCESS_MSG);
        responseResult.setData(data);
        return responseResult;
    }


    public static ResponseResult fail(Integer errorCode, String msg) {
        ResponseResult responseResult = new ResponseResult();
        responseResult.setSuccess(false);
        responseResult.setErrorCode(errorCode);
        responseResult.setMsg(msg);
        return responseResult;
    }

    public static ResponseResult fail(String msg) {
        ResponseResult responseResult = new ResponseResult();
        responseResult.setSuccess(false);
        responseResult.setErrorCode(FAIL_ERROR_CODE);
        responseResult.setMsg(msg);
        return responseResult;
    }


}
