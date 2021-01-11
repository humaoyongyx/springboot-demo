package issac.study.mbp.core.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author issac.hu
 */
@Data
public class BaseVo extends GeneralVo {

    @ApiModelProperty("创建人")
    private Integer updatedBy;

    @ApiModelProperty("更新人")
    private Integer createdBy;

}
