package issac.study.mbp.core.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author issac.hu
 */
@Data
public class GeneralReq {
    
    @ApiModelProperty(value = "主键id")
    private Integer id;
}
