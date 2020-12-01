package issac.study.cache.service.base;

import issac.study.cache.core.service.BaseCrudService;
import issac.study.cache.req.base.BaseReq;
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

    private Class voClass;


}
