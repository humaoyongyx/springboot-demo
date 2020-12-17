package issac.study.mbp.core.req;

import lombok.Data;

/**
 * @author issac.hu
 */
@Data
public class BaseTreeReq extends BaseReq {

    private Integer rootId;

    private Integer parentId;
    /**
     * 是否递归操作
     */
    private Boolean deeper;
}
