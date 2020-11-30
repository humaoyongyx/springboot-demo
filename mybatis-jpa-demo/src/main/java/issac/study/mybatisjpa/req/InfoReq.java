package issac.study.mybatisjpa.req;

import issac.study.mybatisjpa.req.base.BaseReq;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author issac.hu
 */
@Data
public class InfoReq extends BaseReq {

    @NotBlank
    private String name;

    private String descp;

}
