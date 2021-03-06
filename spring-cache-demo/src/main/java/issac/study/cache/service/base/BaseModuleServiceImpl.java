package issac.study.cache.service.base;

import issac.study.cache.core.service.BaseCrudService;
import issac.study.cache.exception.BusinessRuntimeException;
import issac.study.cache.exception.Error404RuntimeException;
import issac.study.cache.req.base.BaseReq;
import issac.study.cache.service.register.BaseModuleRegister;
import issac.study.cache.utils.ConvertUtils;
import issac.study.cache.vo.response.ResponseVo;
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
public class BaseModuleServiceImpl implements BaseModuleService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BaseModuleServiceImpl.class);

    private Map<String, BaseModule> moduleMap;

    @Autowired
    public void setModuleMap(BaseModuleRegister baseModuleRegister) {
        this.moduleMap = baseModuleRegister.getModuleMap();
    }

    public BaseModule getModule(String moduleId) {
        BaseModule baseModule = moduleMap.get(moduleId);
        if (baseModule == null) {
            throw new Error404RuntimeException();
        }
        return baseModule;
    }

    @Override
    public ResponseVo save(String moduleId, Map<String, Object> param) {
        BaseModule module = getModule(moduleId);
        BaseReq baseReq = validateAndGet(param, module.getReqClass());
        BaseCrudService baseCrudService = module.getBaseCrudService();
        baseCrudService.validateReq(baseReq);
        return ResponseVo.success(baseCrudService.save(baseReq, module.getVoClass()));
    }

    @Override
    public ResponseVo update(String moduleId, Map<String, Object> param, boolean withNull) {
        BaseModule module = getModule(moduleId);
        BaseReq baseReq = validateAndGet(param, module.getReqClass());
        BaseCrudService baseCrudService = module.getBaseCrudService();
        return ResponseVo.success(baseCrudService.update(baseReq, module.getVoClass(), withNull));
    }

    @Override
    public ResponseVo delete(String moduleId, Integer id) {
        BaseModule module = getModule(moduleId);
        module.getBaseCrudService().deleteById(id);
        return ResponseVo.success();
    }

    @Override
    public ResponseVo getById(String moduleId, Integer id) {
        BaseModule module = getModule(moduleId);
        return ResponseVo.success(module.getBaseCrudService().getById(id, module.getVoClass()));
    }

    @Override
    public ResponseVo page(String moduleId, Pageable pageable, Map<String, Object> param) {
        BaseModule module = getModule(moduleId);
        return ResponseVo.success(module.getBaseCrudService().page(validateAndGet(param, module.getReqClass()), pageable, module.getVoClass()));
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
