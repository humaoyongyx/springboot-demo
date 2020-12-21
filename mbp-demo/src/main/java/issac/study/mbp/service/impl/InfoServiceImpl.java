package issac.study.mbp.service.impl;

import issac.study.mbp.core.service.impl.BaseCrudServiceImpl;
import issac.study.mbp.mapper.InfoMapper;
import issac.study.mbp.model.InfoModel;
import issac.study.mbp.service.InfoService;
import issac.study.mbp.vo.InfoVo;
import org.springframework.stereotype.Service;

/**
 * @author i.hu
 */
@Service
public class InfoServiceImpl extends BaseCrudServiceImpl<InfoMapper, InfoModel, InfoVo> implements InfoService {

    @Override
    public boolean cacheable() {
        return true;
    }
}
