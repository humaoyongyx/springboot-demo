package issac.study.mbp.core.req.tree2;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author issac.hu
 */
@Data
public class BaseReq {

    @ApiModelProperty(value = "主键id")
    private Long id;

    /**
     * 删除状态,1: 删除 0：未删除
     */
    @ApiModelProperty(hidden = true)
    private Byte deleted = 0;

}
