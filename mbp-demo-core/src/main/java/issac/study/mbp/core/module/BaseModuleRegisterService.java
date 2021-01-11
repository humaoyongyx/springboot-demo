package issac.study.mbp.core.module;


import java.util.Map;

/**
 * @author issac.hu
 */
public interface BaseModuleRegisterService {

    /**
     * 获取module
     *
     * @return
     */
    Map<String, BaseModule> getModuleMap();
}
