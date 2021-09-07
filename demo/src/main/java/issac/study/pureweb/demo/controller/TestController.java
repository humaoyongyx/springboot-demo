package issac.study.pureweb.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @author issac.hu
 * @description
 * @date 2021/9/7 16:10
 */
@RestController
@RequestMapping("/test")
public class TestController {

    @GetMapping
    public Object test() {
        Map<String, Object> map = new HashMap<>();
        map.put("test", 1);
        return map;
    }
}
