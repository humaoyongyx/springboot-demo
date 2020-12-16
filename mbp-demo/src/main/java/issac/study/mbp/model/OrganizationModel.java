package issac.study.mbp.model;

import com.baomidou.mybatisplus.annotation.TableName;
import issac.study.mbp.model.base.BaseTreeModel;
import lombok.Data;

/**
 * @author issac.hu
 */
@Data
@TableName("organization_mbp")
public class OrganizationModel extends BaseTreeModel {
    private String name;

}
