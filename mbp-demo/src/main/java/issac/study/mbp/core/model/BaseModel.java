package issac.study.mbp.core.model;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

/**
 * @author issac.hu
 */
@Data
public class BaseModel extends GeneralModel {

    @TableField(fill = FieldFill.UPDATE)
    private Integer updatedBy;

    @TableField(fill = FieldFill.INSERT)
    private Integer createdBy;
}
