package issac.study.mbp.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import issac.study.mbp.core.req.BaseTreeReq;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.Date;


/**
 * @author issac.hu
 */
@Data
@ApiModel("组织请求")
public class OrganizationReq extends BaseTreeReq {

    @ApiModelProperty("名称")
    @NotBlank
    private String name;

    private Date createdTime;
}
