package issac.study.mbp.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import issac.study.mbp.core.annotation.RespVo;
import issac.study.mbp.core.req.BasePageReq;
import issac.study.mbp.req.OrganizationReq;
import issac.study.mbp.service.OrganizationService;
import issac.study.mbp.vo.OrganizationVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @author issac.hu
 */
@Api(tags = "组织管理模块")
@RestController
@RespVo
@RequestMapping("/api/org")
public class OrganizationController {

    @Autowired
    OrganizationService organizationService;

    @ApiOperation("保存")
    @PostMapping("/save")
    public OrganizationVo save(@Valid OrganizationReq organizationReq) {
        organizationReq.setId(null);
        return organizationService.save(organizationReq);
    }

    @ApiOperation("更新")
    @PutMapping("/{id}")
    public OrganizationVo update(@PathVariable("id") Integer id, OrganizationReq organizationReq) {
        organizationReq.setId(id);
        return organizationService.update(organizationReq);
    }

    @ApiOperation("删除")
    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Integer id, @RequestParam(value = "deeper", required = false, defaultValue = "false") Boolean deeper) {
        organizationService.deleteById(id, deeper);
    }

    @ApiOperation("id查询")
    @GetMapping("/get")
    public OrganizationVo get(Integer id) {
        return organizationService.getById(id);
    }


    @ApiOperation("分页")
    @GetMapping("/page")
    public Page<OrganizationVo> page(OrganizationReq organizationReq, BasePageReq basePageReq) {
        return organizationService.page(organizationReq, basePageReq);
    }

    @ApiOperation("树形查询")
    @GetMapping("/tree")
    public List<OrganizationVo> tree(OrganizationReq organizationReq) {
        return organizationService.tree(organizationReq);
    }

}
