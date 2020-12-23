package issac.study.mbp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import issac.study.mbp.model.SchoolModel;
import issac.study.mbp.model.cross.SchoolCrossModel;
import org.apache.ibatis.annotations.Param;

/**
 * @author issac.hu
 */
public interface SchoolMapper extends BaseMapper<SchoolModel> {

    SchoolCrossModel findCrossById(@Param("id") Integer id);
}
