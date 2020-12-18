package issac.study.mbp.vo;

import io.swagger.annotations.ApiModelProperty;
import issac.study.mbp.core.vo.BaseTreeVo;
import lombok.Data;

/**
 * @author issac.hu
 */
@Data
public class OrganizationVo extends BaseTreeVo {

    @ApiModelProperty("名称")
    private String name;
}
