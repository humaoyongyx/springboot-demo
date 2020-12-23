package issac.study.mbp.model;

import com.baomidou.mybatisplus.annotation.TableName;
import issac.study.mbp.core.model.GeneralModel;
import lombok.Data;

/**
 * @author issac.hu
 */
@Data
@TableName("student")
public class StudentModel extends GeneralModel {
    private Integer classId;
    private String name;
}
