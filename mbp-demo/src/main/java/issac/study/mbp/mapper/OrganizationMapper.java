package issac.study.mbp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import issac.study.mbp.model.OrganizationModel;
import org.apache.ibatis.annotations.Param;

/**
 * @author issac.hu
 */
public interface OrganizationMapper extends BaseMapper<OrganizationModel> {

    IPage<OrganizationModel> selectPageVo(IPage<?> page, @Param("model") OrganizationModel model);
}
