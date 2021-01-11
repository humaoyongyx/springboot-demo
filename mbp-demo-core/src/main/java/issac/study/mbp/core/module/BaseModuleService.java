package issac.study.mbp.core.module;

import issac.study.mbp.core.req.BasePageReq;
import issac.study.mbp.core.response.ResponseResult;

import java.util.Map;

/**
 * @author issac.hu
 */
public interface BaseModuleService {

    /**
     * 新增
     *
     * @param moduleId
     * @param param
     * @return
     */
    ResponseResult save(String moduleId, Map<String, Object> param);

    /**
     * 更新
     *
     * @param moduleId
     * @param param
     * @param withNull
     * @return
     */
    ResponseResult update(String moduleId, Map<String, Object> param, boolean withNull);

    /**
     * 删除
     *
     * @param moduleId
     * @param id
     * @return
     */
    ResponseResult delete(String moduleId, Integer id);

    /**
     * id查询
     *
     * @param moduleId
     * @param id
     * @return
     */
    ResponseResult getById(String moduleId, Integer id);

    /**
     * 分页查询
     *
     * @param moduleId
     * @param basePageReq
     * @param param
     * @return
     */
    ResponseResult page(String moduleId, BasePageReq basePageReq, Map<String, Object> param);
}
