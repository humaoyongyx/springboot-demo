package issac.study.mbp.model;

import com.baomidou.mybatisplus.annotation.TableName;
import issac.study.mbp.core.model.GeneralModel;
import lombok.Data;

/**
 * @author issac.hu
 */
@Data
@TableName("class")
public class ClassModel extends GeneralModel {
    private Integer schoolId;
    private String name;
}
