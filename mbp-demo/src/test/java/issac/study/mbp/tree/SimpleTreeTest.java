package issac.study.mbp.tree;


import issac.study.mbp.base.BaseServiceTest;
import issac.study.mbp.core.utils.ConvertUtils;
import issac.study.mbp.model.OrganizationModel;
import issac.study.mbp.req.OrganizationReq;
import issac.study.mbp.service.OrganizationService;
import issac.study.mbp.vo.OrganizationVo;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author issac.hu
 */
public class SimpleTreeTest extends BaseServiceTest {

    @Autowired
    OrganizationService organizationService;

    @Test
    public void testSave() {
        OrganizationReq organizationReq = new OrganizationReq();
        organizationReq.setName("root");
        OrganizationVo root = organizationService.save(organizationReq);
        organizationReq = new OrganizationReq();
        organizationReq.setName("leaf1");
        organizationReq.setParentId(root.getId());
        OrganizationVo leaf1 = organizationService.save(organizationReq);
        organizationReq = new OrganizationReq();
        organizationReq.setName("leaf2");
        organizationReq.setParentId(root.getId());
        OrganizationVo leaf2 = organizationService.save(organizationReq);
        organizationReq = new OrganizationReq();
        organizationReq.setName("leaf2_sub1");
        organizationReq.setParentId(leaf2.getId());
        OrganizationVo leaf2_sub1 = organizationService.save(organizationReq);
        organizationReq = new OrganizationReq();
        organizationReq.setName("leaf2_sub2");
        organizationReq.setParentId(leaf2_sub1.getId());
        OrganizationVo leaf2_sub2 = organizationService.save(organizationReq);
        organizationReq = new OrganizationReq();
        organizationReq.setName("leaf2_sub2_3");
        organizationReq.setParentId(leaf2_sub2.getId());
        OrganizationVo leaf2_sub2_3 = organizationService.save(organizationReq);
    }

    @Test
    public void testDel() {
        organizationService.deleteById(43);
    }

    @Test
    public void testDelDeeper() {
        organizationService.deleteById(46, true);
    }

    @Test
    public void testUpdate() {
        OrganizationReq organizationReq = new OrganizationReq();
        organizationReq.setName("null");
        organizationReq.setId(74);
        OrganizationVo update = organizationService.update(organizationReq, true);
        System.out.println(ConvertUtils.toJsonString(update, true));

    }

    @Test
    public void testGetAll() {
        List<OrganizationModel> organizationModels = organizationService.getBaseMapper().selectList(null);
        System.out.println(ConvertUtils.toJsonString(organizationModels, true));
    }

}
