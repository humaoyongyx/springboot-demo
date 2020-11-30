package issac.study.mybatisjpa.service.impl;

import issac.study.mybatisjpa.repository.UserExtraRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author issac.hu
 */
@Service
public class UserExtraServiceImpl {

    @Autowired
    UserExtraRepository userExtraRepository;


}
