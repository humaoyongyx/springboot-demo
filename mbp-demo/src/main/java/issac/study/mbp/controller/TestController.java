package issac.study.mbp.controller;

import issac.study.mbp.req.OrganizationReq;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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

    @GetMapping(value = "/2")
    @ResponseBody
    public Object test2(@RequestBody OrganizationReq organizationReq) {
        return organizationReq;
    }

}
