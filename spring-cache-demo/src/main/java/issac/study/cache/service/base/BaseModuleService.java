package issac.study.cache.service.base;

import issac.study.cache.vo.response.ResponseVo;
import org.springframework.data.domain.Pageable;

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
    ResponseVo save(String moduleId, Map<String, Object> param);

    /**
     * 更新
     *
     * @param moduleId
     * @param param
     * @param withNull
     * @return
     */
    ResponseVo update(String moduleId, Map<String, Object> param, boolean withNull);

    /**
     * 删除
     *
     * @param moduleId
     * @param id
     * @return
     */
    ResponseVo delete(String moduleId, Integer id);

    /**
     * id查询
     *
     * @param moduleId
     * @param id
     * @return
     */
    ResponseVo getById(String moduleId, Integer id);

    /**
     * 分页查询
     *
     * @param moduleId
     * @param pageable
     * @param param
     * @return
     */
    ResponseVo page(String moduleId, Pageable pageable, Map<String, Object> param);
}
