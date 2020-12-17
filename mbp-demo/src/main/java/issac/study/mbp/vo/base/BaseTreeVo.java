package issac.study.mbp.vo.base;

import lombok.Data;

/**
 * @author issac.hu
 */
@Data
public class BaseTreeVo extends BaseVo {

    private Integer rootId;

    private Integer parentId;

    private String idPath;

    private Integer depth;

    private Boolean leaf;

    private Integer seq;
}
