package issac.study.cache.vo;

import issac.study.cache.vo.base.BaseVo;
import lombok.Data;

/**
 * @author issac.hu
 */
@Data
public class UserInfoVo extends BaseVo {
    private String name;
    private Integer age;
    private String idCardNo;
}
