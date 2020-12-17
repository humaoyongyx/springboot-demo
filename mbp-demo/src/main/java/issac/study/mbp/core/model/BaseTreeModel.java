package issac.study.mbp.core.model;

import lombok.Data;

/**
 * @author issac.hu
 */
@Data
public class BaseTreeModel extends BaseModel {

    private Integer rootId;

    private Integer parentId;

    private String idPath;

    private Integer depth;

    private Boolean leaf;

    private Integer seq;

    private Integer childSeq;

}
