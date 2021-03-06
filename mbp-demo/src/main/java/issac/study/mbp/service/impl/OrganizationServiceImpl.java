package issac.study.mbp.service.impl;

import issac.study.mbp.core.service.impl.BaseTreeCrudServiceImpl;
import issac.study.mbp.mapper.OrganizationMapper;
import issac.study.mbp.model.OrganizationModel;
import issac.study.mbp.service.OrganizationService;
import issac.study.mbp.vo.OrganizationVo;
import org.springframework.stereotype.Service;

/**
 * @author issac.hu
 */
@Service
public class OrganizationServiceImpl extends BaseTreeCrudServiceImpl<OrganizationMapper, OrganizationModel, OrganizationVo> implements OrganizationService {

}
