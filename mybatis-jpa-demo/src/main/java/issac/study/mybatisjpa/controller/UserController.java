package issac.study.mybatisjpa.controller;

import issac.study.mybatisjpa.domain.UserExtraModel;
import issac.study.mybatisjpa.mapper.UserMapper;
import issac.study.mybatisjpa.req.UserReq;
import issac.study.mybatisjpa.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

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

    /**
     * 多表交叉查询的分页查询
     * <p>
     * http://localhost:8081/user/extraPage?name=cd&size=2&sort=id,asc&sort=name,desc
     *
     * @param userReq
     * @param pageable
     * @return
     */
    @GetMapping("/extraPage")
    public Object extraPage(UserReq userReq, @PageableDefault(page = 0, size = 50, sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable) {
        Sort sort = pageable.getSort();
        List<String> sorts = new ArrayList<>();
        for (Sort.Order order : sort) {
            if (order.isAscending()) {
                sorts.add(order.getProperty() + " ASC ");
            } else {
                sorts.add(order.getProperty() + " DESC ");
            }
        }
        List<UserExtraModel> userExtraModels = userMapper.selectPage(userReq, pageable.getOffset(), pageable.getPageSize(), sorts);
        long total = userMapper.selectPageTotal(userReq);
        return new PageImpl<>(userExtraModels, pageable, total);
    }


}
