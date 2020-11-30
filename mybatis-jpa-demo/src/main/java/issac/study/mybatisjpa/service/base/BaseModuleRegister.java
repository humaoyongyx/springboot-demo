package issac.study.mybatisjpa.service.base;

import issac.study.mybatisjpa.core.service.BaseCrudService;
import issac.study.mybatisjpa.req.InfoReq;
import issac.study.mybatisjpa.req.base.BaseReq;
import issac.study.mybatisjpa.service.InfoService;
import issac.study.mybatisjpa.vo.InfoVo;
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
    InfoService infoService;

    /**
     * 注册快捷请求方法
     */
    public void register() {
        register("info", infoService, InfoReq.class, InfoVo.class);
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
