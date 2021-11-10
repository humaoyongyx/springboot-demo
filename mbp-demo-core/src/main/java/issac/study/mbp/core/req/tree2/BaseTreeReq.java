package issac.study.mbp.core.req.tree2;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author issac.hu
 */
@Data
public class BaseTreeReq extends BaseReq {

    @ApiModelProperty("根节点id")
    private Long rootId;

    @ApiModelProperty("父节点id")
    private Long parentId;

    @ApiModelProperty("插入节点的下一个id，如果为null，表示插入到最后一个节点")
    private Long nextId;

    /**
     * 是否递归操作
     */
    @ApiModelProperty("是否递归操作")
    private Boolean deeper;

}
