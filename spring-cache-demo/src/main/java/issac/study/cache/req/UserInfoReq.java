package issac.study.cache.req;

import issac.study.cache.req.base.BaseReq;
import lombok.Data;

/**
 * @author issac.hu
 */
@Data
public class UserInfoReq extends BaseReq {
    private String name;
    private Integer age;
    private String idCardNo;
}
