package issac.study.mbp.core.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author issac.hu
 */
@Data
public class GeneralVo {

    @ApiModelProperty("主键id")
    private Integer id;

    @ApiModelProperty("创建时间")
    private Date createdTime;

    @ApiModelProperty("更新时间")
    private Date updatedTime;
}
