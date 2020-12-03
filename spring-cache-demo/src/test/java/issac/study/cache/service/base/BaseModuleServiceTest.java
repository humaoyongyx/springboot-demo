package issac.study.cache.service.base;

import issac.study.cache.base.BaseServiceTest;
import issac.study.cache.utils.ConvertUtils;
import issac.study.cache.utils.MapParam;
import issac.study.cache.vo.response.ResponseVo;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;

import java.util.HashMap;

/**
 * @author issac.hu
 */
public class BaseModuleServiceTest extends BaseServiceTest {

    @Autowired
    BaseModuleService baseModuleService;

    @Test
    public void save() {

        ResponseVo result = baseModuleService.save("userInfo", MapParam.build().put("name", "测试").toMap());
        System.out.println(ConvertUtils.toJsonString(result, true));

    }

    @Test
    public void update() {
    }

    @Test
    public void delete() {
    }

    @Test
    public void getById() {
    }

    @Test
    public void page() {
        ResponseVo responseVo = baseModuleService.page("userInfo", PageRequest.of(0, 50), new HashMap<>());
        System.out.println(ConvertUtils.toJsonString(responseVo, true));
    }
}