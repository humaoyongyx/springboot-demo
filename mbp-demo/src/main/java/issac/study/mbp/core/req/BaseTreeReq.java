package issac.study.mbp.core.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author issac.hu
 */
@Data
public class BaseTreeReq extends BaseReq {

    @ApiModelProperty("根节点id")
    private Integer rootId;

    @ApiModelProperty("父节点id")
    private Integer parentId;
    /**
     * 是否递归操作
     */
    @ApiModelProperty("是否递归操作")
    private Boolean deeper;
}
