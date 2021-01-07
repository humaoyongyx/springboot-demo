package issac.study.mbp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author issac.hu
 */
@RequestMapping("/api/test")
@Controller
public class TestController {


    @GetMapping(value = "/")
    public String test(ModelMap modelMap) {
        modelMap.addAttribute("xx", "dsf");
        return "/index.html";
    }

}
