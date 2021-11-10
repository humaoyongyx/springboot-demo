package issac.study.mbp.core.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author issac.hu
 */
@Data
public class BaseTreeResp {

    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("根节点id")
    private Long rootId;

    @ApiModelProperty("父节点id")
    private Long parentId;

    @ApiModelProperty("节点路径")
    private String idPath;

    @ApiModelProperty("节点深度")
    private Integer depth;

    @ApiModelProperty("是否是叶子节点")
    private Integer leaf;

    @ApiModelProperty("节点层级序号")
    private Integer sort;

    @ApiModelProperty(hidden = true)
    private List<? extends BaseTreeResp> children;

}
