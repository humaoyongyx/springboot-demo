package issac.study.mbp.core.vo;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

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

    private List<? extends BaseTreeVo> children = new ArrayList<>();

}
