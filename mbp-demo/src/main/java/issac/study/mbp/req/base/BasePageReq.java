package issac.study.mbp.req.base;

import lombok.Data;

/**
 * @author issac.hu
 */
@Data
public class BasePageReq {
    private int page = 1;
    private int size = 50;
    /**
     * 默认id倒序
     */
    private String order = "id,desc";

}
