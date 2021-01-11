package issac.study.mbp.core.module;


import issac.study.mbp.core.req.BaseReq;
import issac.study.mbp.core.service.BaseCrudService;

import java.util.HashMap;
import java.util.Map;

/**
 * 模块抽象类，可以直接继承此类，覆写register
 *
 * @author issac.hu
 */
public abstract class AbstractModuleRegister implements BaseModuleRegisterService {


    private Map<String, BaseModule> moduleMap = new HashMap<>();

    private boolean isRegister = false;


    protected abstract void register();

    /**
     * 注册快捷类
     *
     * @param name
     * @param baseCrudService
     * @param reqClass
     */
    protected void register(String name, BaseCrudService baseCrudService, Class<? extends BaseReq> reqClass) {
        moduleMap.put(name, new BaseModule().setBaseCrudService(baseCrudService).setReqClass(reqClass));
    }


    @Override
    public Map<String, BaseModule> getModuleMap() {
        if (!isRegister) {
            register();
        }
        return moduleMap;
    }
}
