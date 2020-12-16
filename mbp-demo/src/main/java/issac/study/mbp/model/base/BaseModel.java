package issac.study.mbp.model.base;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

/**
 * @author issac.hu
 */
@Data
public class BaseModel extends GeneralModel {

    @TableField(fill = FieldFill.UPDATE)
    private String updatedBy;

    @TableField(fill = FieldFill.INSERT)
    private String createdBy;
}
