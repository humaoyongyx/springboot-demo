package issac.study.pureweb.demo.controller;

import issac.study.pureweb.demo.utils.SpringResponseUtils;
import issac.study.pureweb.demo.utils.ZipUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
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

    @GetMapping("/2")
    public void test2(HttpServletResponse response) throws Exception {
        SpringResponseUtils.setResponse(response, "测试.zip");
        ZipUtils.doZip("D:\\release", response.getOutputStream());
    }

}
