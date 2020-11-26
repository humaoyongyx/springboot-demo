package issac.study.mybatisjpa.req;

import issac.study.mybatisjpa.req.base.BaseReq;
import lombok.Data;

/**
 * @author issac.hu
 */
@Data
public class UserReq extends BaseReq {
    private String name;
    private String descp;

}
