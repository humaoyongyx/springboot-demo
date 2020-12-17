package issac.study.mbp.req.base;

import lombok.Data;

/**
 * @author issac.hu
 */
@Data
public class BaseTreeReq extends BaseReq {
    private Integer parentId;
    /**
     * 是否递归操作
     */
    private Boolean deeper;
}
