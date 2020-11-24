package issac.study.mybatis.controller;

import issac.study.mybatis.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author issac.hu
 */
@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    TestService testService;

    @GetMapping
    public Object test() {
        return testService.test();

    }

    @GetMapping("/id")
    public Object test(Integer id) {
        return testService.test(id);
    }


    @GetMapping("/save")
    public void test2() {
        testService.test2();
    }

    @GetMapping("/page")
    public Object testPage(Integer pageNum, Integer pageSize) {
        return testService.page(pageNum, pageSize);
    }

    @GetMapping("/pagePlus")
    public Object testPagePlus(Integer pageNum, Integer pageSize) {
        return testService.pagePlus(pageNum, pageSize);
    }

    @GetMapping("/pagePlus2")
    public Object testPagePlus2(@PageableDefault(page = 0, size = 50, sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable) {
        return testService.pagePlus(pageable);
    }
}
