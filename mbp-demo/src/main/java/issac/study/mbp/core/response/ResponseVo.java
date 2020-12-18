package issac.study.mbp.core.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author issac.hu
 */
@Data
@Accessors(chain = true)
public class ResponseVo {

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

    public static ResponseVo success() {
        ResponseVo responseVo = new ResponseVo();
        responseVo.setSuccess(true);
        responseVo.setErrorCode(SUCCESS_ERROR_CODE);
        responseVo.setMsg(SUCCESS_MSG);
        return responseVo;
    }

    public static ResponseVo success(Object data) {
        ResponseVo responseVo = new ResponseVo();
        responseVo.setSuccess(true);
        responseVo.setErrorCode(SUCCESS_ERROR_CODE);
        responseVo.setMsg(SUCCESS_MSG);
        responseVo.setData(data);
        return responseVo;
    }


    public static ResponseVo fail(Integer errorCode, String msg) {
        ResponseVo responseVo = new ResponseVo();
        responseVo.setSuccess(false);
        responseVo.setErrorCode(errorCode);
        responseVo.setMsg(msg);
        return responseVo;
    }

    public static ResponseVo fail(String msg) {
        ResponseVo responseVo = new ResponseVo();
        responseVo.setSuccess(false);
        responseVo.setErrorCode(FAIL_ERROR_CODE);
        responseVo.setMsg(msg);
        return responseVo;
    }


}
