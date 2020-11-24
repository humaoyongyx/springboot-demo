package issac.study.mybatis.controller;

import issac.study.mybatis.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @GetMapping("/id2")
    public Object test(Integer id) {
        return testService.test(id);
    }


    @GetMapping("/2")
    public void test2() {
        testService.test2();
    }
}
