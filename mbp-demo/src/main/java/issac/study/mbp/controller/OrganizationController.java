package issac.study.mbp.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import issac.study.mbp.mapper.OrganizationMapper;
import issac.study.mbp.model.OrganizationModel;
import issac.study.mbp.req.OrganizationReq;
import issac.study.mbp.req.base.BasePageReq;
import issac.study.mbp.service.OrganizationService;
import issac.study.mbp.vo.OrganizationVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * @author issac.hu
 */
@RestController
@RequestMapping("/org")
public class OrganizationController {

    @Autowired
    OrganizationService organizationService;

    @GetMapping("/save")
    public OrganizationModel save(OrganizationModel organizationModel) {
        organizationModel.setCreatedTime(new Date());
        organizationService.save(organizationModel);
        return organizationModel;
    }

    @GetMapping("/save2")
    public OrganizationVo save2(OrganizationReq organizationReq) {
        return organizationService.saveGet(organizationReq);
    }

    @GetMapping("/update")
    public void update(OrganizationModel organizationModel) {
        organizationService.updateById(organizationModel);
    }

    @GetMapping("/update2")
    public OrganizationVo update2(OrganizationReq organizationReq, @RequestParam(defaultValue = "false", required = false) Boolean include) {
        return organizationService.updateGet(organizationReq, include);
    }


    @GetMapping("/get")
    public OrganizationModel get(Integer id) {
        return organizationService.getById(id);
    }

    @GetMapping("/page")
    public Page<OrganizationModel> page(OrganizationModel organizationModel, Page<OrganizationModel> pageReq) {
        Page<OrganizationModel> page = organizationService.page(pageReq);
        return page;
    }

    @GetMapping("/page2")
    public IPage<OrganizationVo> page2(OrganizationReq organizationReq, BasePageReq basePageReq) {
        Page<OrganizationVo> page = organizationService.page(organizationReq, basePageReq);
        return page;
    }


    @GetMapping("/cPage")
    public IPage<OrganizationModel> cPage(OrganizationModel organizationModel, Page<OrganizationModel> pageReq) {
        OrganizationMapper baseMapper = (OrganizationMapper) organizationService.getBaseMapper();
        IPage<OrganizationModel> organizationModelIPage = baseMapper.selectPageVo(pageReq, organizationModel);
        return organizationModelIPage;
    }

}
