package issac.study.mbp.core.module;

import issac.study.mbp.core.req.BaseReq;
import issac.study.mbp.core.service.BaseCrudService;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author humy6
 * @Date: 2019/7/1 13:35
 */
@Data
@Accessors(chain = true)
public class BaseModule {

    private BaseCrudService baseCrudService;

    private Class<? extends BaseReq> reqClass;

}
