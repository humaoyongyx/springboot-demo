package issac.study.mbp.core.model.tree2;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * @author issac.hu
 */
@Data
public class BaseTreeModel {
    @TableId
    private Long id;

    private Long rootId;

    private Long parentId;

    private String idPath;

    private Integer depth;

    private Integer leaf;

    private Integer sort;

    private Integer childSort;

}
