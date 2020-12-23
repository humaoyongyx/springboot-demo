package issac.study.mbp.mapper;

import issac.study.mbp.core.mapper.GeneralMapper;
import issac.study.mbp.model.SchoolModel;
import issac.study.mbp.model.cross.SchoolCrossModel;
import org.apache.ibatis.annotations.Param;

/**
 * @author issac.hu
 */
public interface SchoolMapper extends GeneralMapper<SchoolModel> {

    SchoolCrossModel findCrossById(@Param("id") Integer id);
}
