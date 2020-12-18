package issac.study.mbp.core.model;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author issac.hu
 */
@Data
@TableName("config")
public class ConfigModel extends GeneralModel {
    private String cKey;

    private String cVal;

    private String cDesc;
}
