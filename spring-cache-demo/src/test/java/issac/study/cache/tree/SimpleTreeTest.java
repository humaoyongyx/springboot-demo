package issac.study.cache.tree;

import issac.study.cache.base.BaseServiceTest;
import issac.study.cache.req.OrganizationReq;
import issac.study.cache.service.OrganizationService;
import issac.study.cache.vo.OrganizationVo;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

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
        OrganizationVo root = organizationService.save(organizationReq, OrganizationVo.class);
        organizationReq = new OrganizationReq();
        organizationReq.setName("leaf1");
        organizationReq.setParentId(root.getId());
        OrganizationVo leaf1 = organizationService.save(organizationReq, OrganizationVo.class);
        organizationReq = new OrganizationReq();
        organizationReq.setName("leaf2");
        organizationReq.setParentId(root.getId());
        OrganizationVo leaf2 = organizationService.save(organizationReq, OrganizationVo.class);
        organizationReq = new OrganizationReq();
        organizationReq.setName("leaf2_sub1");
        organizationReq.setParentId(leaf2.getId());
        OrganizationVo leaf2_sub1 = organizationService.save(organizationReq, OrganizationVo.class);
        organizationReq = new OrganizationReq();
        organizationReq.setName("leaf2_sub2");
        organizationReq.setParentId(leaf2_sub1.getId());
        OrganizationVo leaf2_sub2 = organizationService.save(organizationReq, OrganizationVo.class);
    }

    @Test
    public void testDel() {
        organizationService.deleteById(44);
    }

    @Test
    public void testDelDeeper() {
        organizationService.deleteById(43, true);
    }
}
