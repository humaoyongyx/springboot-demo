package issac.study.cache.controller.base;

import issac.study.cache.service.base.BaseModuleService;
import issac.study.cache.vo.response.ResponseVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author humy6
 * @Date: 2019/7/1 13:17
 * <p>
 * 模板控制器类
 * moduleId 为具体请求类的名称：如equipment
 */
@RestController
@RequestMapping("/{moduleId}/")
public class BaseModuleController {

    @Autowired
    BaseModuleService baseModuleService;

    /**
     * @param moduleId
     * @param id
     * @return
     */
    @GetMapping(value = "/{id}")
    public ResponseVo getById(@PathVariable("moduleId") String moduleId, @PathVariable("id") Integer id) {
        return baseModuleService.getById(moduleId, id);
    }

    @GetMapping(value = "/page")
    public ResponseVo page(@PathVariable("moduleId") String moduleId, @PageableDefault(page = 0, size = 50, sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable, @RequestParam(required = false) Map<String, Object> param) {
        return baseModuleService.page(moduleId, pageable, param);
    }

    @PostMapping
    public ResponseVo save(@PathVariable("moduleId") String moduleId, @RequestBody(required = false) Map<String, Object> param) {
        return baseModuleService.save(moduleId, param);
    }

    @PutMapping("/{id}")
    public ResponseVo update(@PathVariable("moduleId") String moduleId, @PathVariable("id") Integer id, @RequestBody(required = false) Map<String, Object> param, @RequestParam(required = false, defaultValue = "false") boolean withNull) {
        param.put("id", id);
        return baseModuleService.update(moduleId, param, withNull);
    }

    @DeleteMapping("/{id}")
    public ResponseVo delete(@PathVariable("moduleId") String moduleId, @PathVariable("id") Integer id) {
        return baseModuleService.delete(moduleId, id);
    }

}
