package issac.study.mybatis.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import issac.study.mybatis.domain.InfoTest;
import issac.study.mybatis.req.InfoTestReq;
import issac.study.mybatis.service.InfoTestService;
import issac.study.mybatis.utils.ConvertUtils;
import issac.study.mybatis.vo.InfoTestVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * info表的简单增删该查
 *
 * @author issac.hu
 */
@RestController
@RequestMapping("infoTest")
public class InfoTestController {

    @Autowired
    InfoTestService infoTestService;

    @GetMapping("/id")
    public Object getById(Integer id) {
        return infoTestService.getById(id);
    }

    @GetMapping("/list")
    public Object getList() {
        return infoTestService.list();
    }

    @GetMapping("/page")
    public Object getPage(@PageableDefault(page = 0, size = 50, sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable, InfoTestReq infoTestReq) {
        QueryWrapper<InfoTest> queryWrapper = ConvertUtils.convertReqToQueryWrapper(infoTestReq, InfoTest.class);
        if (infoTestReq.getCreateTimeBegin() != null) {
            queryWrapper.ge("create_time", infoTestReq.getCreateTimeBegin());
        }
        Page<InfoTest> page = infoTestService.page(ConvertUtils.pageableToPage(pageable), queryWrapper);
        return ConvertUtils.toJpaPage(page, pageable, InfoTestVo.class);
    }

    /**
     * save or update 不会过滤null值
     *
     * @param infoTest
     * @return
     */
    @GetMapping("/save")
    public Object save(InfoTest infoTest) {
        infoTestService.save(infoTest);
        return infoTest;
    }

    @GetMapping("/update")
    public Object update(InfoTest infoTest) {
        infoTestService.saveOrUpdate(infoTest);
        return infoTest;
    }


}
