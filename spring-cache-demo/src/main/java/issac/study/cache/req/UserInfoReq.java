package issac.study.cache.req;

import issac.study.cache.req.base.BaseReq;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author issac.hu
 */
@Data
public class UserInfoReq extends BaseReq {
    @NotBlank
    private String name;
    private Integer age;
    private String idCardNo;
}
