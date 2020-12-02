package issac.study.cache.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import issac.study.cache.req.base.BaseReq;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author issac.hu
 */
@ApiModel("用户对象")
@Data
public class UserInfoReq extends BaseReq {

    @ApiModelProperty("姓名")
    @NotBlank
    private String name;
    private Integer age;
    private String idCardNo;
}
