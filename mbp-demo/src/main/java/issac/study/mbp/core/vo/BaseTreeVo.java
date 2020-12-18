package issac.study.mbp.core.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author issac.hu
 */
@Data
public class BaseTreeVo extends BaseVo {

    @ApiModelProperty("根节点id")
    private Integer rootId;

    @ApiModelProperty("父节点id")
    private Integer parentId;

    @ApiModelProperty("节点路径")
    private String idPath;

    @ApiModelProperty("节点深度")
    private Integer depth;

    @ApiModelProperty("是否是叶子节点")
    private Boolean leaf;

    @ApiModelProperty("节点层级序号")
    private Integer seq;

    @ApiModelProperty(hidden = true)
    private List<? extends BaseTreeVo> children = new ArrayList<>();

}
