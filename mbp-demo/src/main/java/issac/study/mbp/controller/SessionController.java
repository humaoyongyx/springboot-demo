package issac.study.mbp.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

/**
 * @author issac.hu
 */
@RestController
@RequestMapping("/s")
public class SessionController {

    @RequestMapping("/set")
    public String set(HttpSession httpSession) {
        httpSession.setAttribute("t_session", "sss");
        return httpSession.getId();
    }

    @RequestMapping("/get")
    public String get(HttpSession httpSession) {
        return (String) httpSession.getAttribute("t_session");
    }
}
