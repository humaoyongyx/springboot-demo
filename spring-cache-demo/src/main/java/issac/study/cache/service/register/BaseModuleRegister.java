package issac.study.cache.service.register;

import issac.study.cache.core.service.BaseCrudService;
import issac.study.cache.req.UserInfoReq;
import issac.study.cache.req.base.BaseReq;
import issac.study.cache.service.UserInfoService;
import issac.study.cache.service.base.BaseModule;
import issac.study.cache.vo.UserInfoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author issac.hu
 */
@Service
public class BaseModuleRegister {

    private final Map<String, BaseModule> moduleMap = new HashMap<>();

    @Autowired
    UserInfoService userInfoService;

    /**
     * 注册快捷请求方法
     */
    public void register() {
        //todo
        register("userInfo", userInfoService, UserInfoReq.class, UserInfoVo.class);
    }


    /**
     * 注册快捷类
     *
     * @param name
     * @param baseCrudService
     * @param reqClass
     * @param vClass
     */
    private void register(String name, BaseCrudService baseCrudService, Class<? extends BaseReq> reqClass, Class vClass) {
        moduleMap.put(name, new BaseModule().setBaseCrudService(baseCrudService).setReqClass(reqClass).setVoClass(vClass));
    }


    public Map<String, BaseModule> getModuleMap() {
        register();
        return moduleMap;
    }

}
