package issac.study.mybatis.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.Date;

/**
 * @author issac.hu
 */
@Data
//@TableName("infoTest") //默认映射的是info_test表
public class InfoTest {
    @TableId(type = IdType.AUTO)
    private Integer id;

    private String name;

    private String descp;

    private Date createTime;
}
