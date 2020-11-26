package issac.study.mybatisjpa.controller;

import issac.study.mybatisjpa.mapper.UserMapper;
import issac.study.mybatisjpa.req.UserReq;
import issac.study.mybatisjpa.service.UserServiceImpl;
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
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserServiceImpl userService;

    @Autowired
    UserMapper userMapper;

    @GetMapping("/save")
    public Object save() {
        return userService.save();
    }

    @GetMapping("/update")
    public Object update(UserReq userReq) {
        return userService.update(userReq);
    }

    @GetMapping("/page")
    public Object page(UserReq userReq, @PageableDefault(page = 0, size = 50, sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable) {
        return userService.page(userReq, pageable);
    }


    @GetMapping("/extra")
    public Object extra() {
        return userMapper.selectAll();
    }


}
