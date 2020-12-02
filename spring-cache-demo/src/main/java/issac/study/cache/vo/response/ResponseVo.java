package issac.study.cache.vo.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author issac.hu
 */
@ApiModel("返回对象")
@Data
@Accessors(chain = true)
public class ResponseVo {

    public static final int SUCCESS_ERROR_CODE = 0;
    public static final int FAIL_ERROR_CODE = -1;
    public static final String SUCCESS_MSG = "SUCCESS";
    @ApiModelProperty("请求是否成功，成功为true，否则为false")
    private Boolean success;
    @ApiModelProperty("请求出错时的消息")
    private String msg;
    @ApiModelProperty("请求的消息码，请求成功的消息码为0，其他的都为错误码")
    private Integer errorCode;
    @ApiModelProperty("请求成功时，返回的数据")
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
