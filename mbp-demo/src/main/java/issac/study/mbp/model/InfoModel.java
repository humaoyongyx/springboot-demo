package issac.study.mbp.model;

import com.baomidou.mybatisplus.annotation.TableName;
import issac.study.mbp.core.model.BaseModel;
import lombok.Data;

/**
 * @author issac.hu
 */
@Data
@TableName("info")
public class InfoModel extends BaseModel {
    private String name;
}
