package issac.study.mbp.controller;

import issac.study.mbp.core.annotation.RespVo;
import issac.study.mbp.mapper.SchoolMapper;
import issac.study.mbp.model.cross.SchoolCrossModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author issac.hu
 */
@RestController
@RespVo
@RequestMapping("/api/school")
public class SchoolController {

    @Autowired
    SchoolMapper schoolMapper;

    @GetMapping
    public Object test(){
        SchoolCrossModel crossById = schoolMapper.findCrossById(1);
        return crossById;
    }

}
