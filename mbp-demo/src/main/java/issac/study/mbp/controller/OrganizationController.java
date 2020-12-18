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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    @GetMapping("/save")
    public OrganizationVo save(@Valid OrganizationReq organizationReq) {
        return organizationService.save(organizationReq);
    }

    @ApiOperation("更新")
    @GetMapping("/update")
    public OrganizationVo update(OrganizationReq organizationReq) {
        return organizationService.update(organizationReq);
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
