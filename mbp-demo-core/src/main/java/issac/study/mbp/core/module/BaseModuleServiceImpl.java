package issac.study.mbp.core.module;


import issac.study.mbp.core.exception.BusinessRuntimeException;
import issac.study.mbp.core.exception.Error404RuntimeException;
import issac.study.mbp.core.req.BasePageReq;
import issac.study.mbp.core.req.BaseReq;
import issac.study.mbp.core.response.ResponseResult;
import issac.study.mbp.core.service.BaseCrudService;
import issac.study.mbp.core.utils.ConvertUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author humy6
 * @Date: 2019/7/1 13:24
 * <p>
 * 模板类service，通过init方法配置模板类，达到增删改查接口自动生成
 */

@Service
public class BaseModuleServiceImpl implements BaseModuleService, ApplicationContextAware {

    private static final Logger LOGGER = LoggerFactory.getLogger(BaseModuleServiceImpl.class);

    private Map<String, BaseModule> moduleMap = new HashMap<>();


    public BaseModule getModule(String moduleId) {
        BaseModule baseModule = moduleMap.get(moduleId);
        if (baseModule == null) {
            throw new Error404RuntimeException();
        }
        return baseModule;
    }

    @Override
    public ResponseResult save(String moduleId, Map<String, Object> param) {
        BaseModule module = getModule(moduleId);
        BaseReq baseReq = validateAndGet(param, module.getReqClass());
        BaseCrudService baseCrudService = module.getBaseCrudService();
        baseCrudService.validateReq(baseReq);
        return ResponseResult.success(baseCrudService.save(baseReq));
    }

    @Override
    public ResponseResult update(String moduleId, Map<String, Object> param, boolean withNull) {
        BaseModule module = getModule(moduleId);
        BaseReq baseReq = validateAndGet(param, module.getReqClass());
        BaseCrudService baseCrudService = module.getBaseCrudService();
        return ResponseResult.success(baseCrudService.update(baseReq, withNull));
    }

    @Override
    public ResponseResult delete(String moduleId, Integer id) {
        BaseModule module = getModule(moduleId);
        module.getBaseCrudService().deleteById(id);
        return ResponseResult.success();
    }

    @Override
    public ResponseResult getById(String moduleId, Integer id) {
        BaseModule module = getModule(moduleId);
        return ResponseResult.success(module.getBaseCrudService().getById(id));
    }

    @Override
    public ResponseResult page(String moduleId, BasePageReq basePageReq, Map<String, Object> param) {
        BaseModule module = getModule(moduleId);
        return ResponseResult.success(module.getBaseCrudService().page(validateAndGet(param, module.getReqClass()), basePageReq));
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

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Map<String, BaseModuleRegisterService> beansOfType = applicationContext.getBeansOfType(BaseModuleRegisterService.class);
        for (BaseModuleRegisterService baseModuleRegisterService : beansOfType.values()) {
            Map<String, BaseModule> moduleMap = baseModuleRegisterService.getModuleMap();
            this.moduleMap.putAll(moduleMap);
        }
    }
}
