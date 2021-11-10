package issac.study.mbp.core.req.tree2;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author issac.hu
 */
@Data
public class BasePageReq extends BaseReq {

    @ApiModelProperty("页数，默认第1页")
    private int page = 1;

    @ApiModelProperty("每页显示条数，默认20")
    private int size = 20;
    /**
     * 默认id倒序
     */
    @ApiModelProperty("排序，默认为 id,desc 即按id倒序")
    private String order = "id,desc";
}
