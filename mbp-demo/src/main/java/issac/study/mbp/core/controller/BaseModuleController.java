package issac.study.mbp.core.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import issac.study.mbp.core.annotation.PrintLog;
import issac.study.mbp.core.module.BaseModuleService;
import issac.study.mbp.core.req.BasePageReq;
import issac.study.mbp.core.response.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 简单的通用增删改查Controller
 *
 * @author humy6
 * @Date: 2019/7/1 13:17
 * <p>
 * 模板控制器类
 * moduleId 为具体请求类的名称：如equipment
 */
@Api(tags = "通用接口")
//@RestController
//@RequestMapping("/api/{moduleId}/")
public class BaseModuleController {

    @Autowired
    BaseModuleService baseModuleService;

    /**
     * @param moduleId
     * @param id
     * @return
     */
    @ApiOperation("id查询")
    @ApiImplicitParam(paramType = "path", name = "moduleId", value = "通用的接口名称", required = true)
    @GetMapping(value = "/{id}")
    @PrintLog("id查询")
    public ResponseResult getById(@PathVariable("moduleId") String moduleId, @PathVariable("id") Integer id) {
        return baseModuleService.getById(moduleId, id);
    }

    @ApiOperation("分页查询")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path", name = "moduleId", value = "通用的接口名称", required = true),
            @ApiImplicitParam(paramType = "query", name = "pageable", value = "可以传page=0、size=50和sort=id,desc"),
            @ApiImplicitParam(paramType = "query", name = "param", value = "具体请求对象的属性")
    })
    @GetMapping(value = "/page")
    @PrintLog("分页")
    public ResponseResult page(@PathVariable("moduleId") String moduleId, BasePageReq basePageReq, @RequestParam(required = false) Map<String, Object> param) {
        return baseModuleService.page(moduleId, basePageReq, param);
    }

    @ApiOperation("单条保存")
    @ApiImplicitParam(paramType = "path", name = "moduleId", value = "通用的接口名称", required = true)
    @PostMapping
    @PrintLog("单条保存")
    public ResponseResult save(@PathVariable("moduleId") String moduleId, @RequestBody(required = false) Map<String, Object> param) {
        return baseModuleService.save(moduleId, param);
    }

    @ApiOperation("单条更新")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path", name = "moduleId", value = "通用的接口名称", required = true),
            @ApiImplicitParam(paramType = "query", name = "withNull", value = "是否更新值为null或者空的属性，默认为false不更新")
    })
    @PutMapping("/{id}")
    @PrintLog("单条更新")
    public ResponseResult update(@PathVariable("moduleId") String moduleId, @PathVariable("id") Integer id, @RequestBody(required = false) Map<String, Object> param, @RequestParam(required = false, defaultValue = "false") boolean withNull) {
        param.put("id", id);
        return baseModuleService.update(moduleId, param, withNull);
    }

    @ApiOperation("单条删除")
    @ApiImplicitParam(paramType = "path", name = "moduleId", value = "通用的接口名称", required = true)
    @DeleteMapping("/{id}")
    @PrintLog("单条删除")
    public ResponseResult delete(@PathVariable("moduleId") String moduleId, @PathVariable("id") Integer id) {
        return baseModuleService.delete(moduleId, id);
    }

}
