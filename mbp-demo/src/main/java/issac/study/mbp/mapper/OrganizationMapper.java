package issac.study.mbp.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import issac.study.mbp.core.mapper.BaseTreeMapper;
import issac.study.mbp.model.OrganizationModel;
import org.apache.ibatis.annotations.Param;

/**
 * @author issac.hu
 */
public interface OrganizationMapper extends BaseTreeMapper<OrganizationModel> {

    IPage<OrganizationModel> selectPageVo(IPage<?> page, @Param("model") OrganizationModel model);
}
