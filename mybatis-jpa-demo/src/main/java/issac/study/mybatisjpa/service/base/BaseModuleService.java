package issac.study.mybatisjpa.service.base;

import issac.study.mybatisjpa.core.service.BaseCrudService;
import issac.study.mybatisjpa.exception.BusinessRuntimeException;
import issac.study.mybatisjpa.req.base.BaseReq;
import issac.study.mybatisjpa.utils.ConvertUtils;
import issac.study.mybatisjpa.vo.response.ResponseVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author humy6
 * @Date: 2019/7/1 13:24
 * <p>
 * 模板类service，通过init方法配置模板类，达到增删改查接口自动生成
 */

@Service
public class BaseModuleService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BaseModuleService.class);

    private Map<String, BaseModule> moduleMap;

    @Autowired
    public void setModuleMap(BaseModuleRegister baseModuleRegister) {
        this.moduleMap = baseModuleRegister.getModuleMap();
    }

    public BaseModule getModule(String moduleId) {
        return moduleMap.get(moduleId);
    }

    public ResponseVo getById(String moduleId, Integer id) {
        BaseModule module = getModule(moduleId);
        return ResponseVo.success(module.getBaseCrudService().findById(id, module.getVoClass()));
    }

    public ResponseVo page(String moduleId, Pageable pageable, Map<String, Object> param) {
        BaseModule module = getModule(moduleId);
        return ResponseVo.success(module.getBaseCrudService().page(validateAndGet(param, module.getReqClass()), pageable, module.getVoClass()));
    }

    public ResponseVo save(String moduleId, Map<String, Object> param) {
        BaseModule module = getModule(moduleId);
        BaseReq baseReq = validateAndGet(param, module.getReqClass());
        BaseCrudService baseCrudService = module.getBaseCrudService();
        baseCrudService.validateReq(baseReq);
        return ResponseVo.success(baseCrudService.save(baseReq, module.getVoClass()));
    }

    public ResponseVo update(String moduleId, Map<String, Object> param, boolean withNull) {
        BaseModule module = getModule(moduleId);
        BaseReq baseReq = validateAndGet(param, module.getReqClass());
        BaseCrudService baseCrudService = module.getBaseCrudService();
        return ResponseVo.success(baseCrudService.update(baseReq, module.getVoClass(), withNull));
    }

    public ResponseVo delete(String moduleId, Integer id) {
        BaseModule module = getModule(moduleId);
        module.getBaseCrudService().deleteById(id);
        return ResponseVo.success();
    }

    private BaseReq validateAndGet(Map<String, Object> param, Class<? extends BaseReq> clazz) {
        try {
            BaseReq baseReq = ConvertUtils.convertObject(param, clazz);
            return baseReq;
        } catch (Exception e) {
            LOGGER.error("validateAndGet:", e);
            throw BusinessRuntimeException.error(e.getMessage());
        }
    }

}
