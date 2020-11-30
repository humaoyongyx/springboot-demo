package issac.study.mybatisjpa.service.impl;

import issac.study.mybatisjpa.core.jpa.JpaQuerySpecification;
import issac.study.mybatisjpa.model.UserEntity;
import issac.study.mybatisjpa.repository.UserRepository;
import issac.study.mybatisjpa.req.UserReq;
import issac.study.mybatisjpa.utils.ConvertUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * @author issac.hu
 */
@Service
public class UserServiceImpl {

    @Autowired
    UserRepository userRepository;

    public Object save() {
        UserEntity userEntity = new UserEntity();
        userEntity.setName(UUID.randomUUID().toString());
        return userRepository.save(userEntity);
    }

    public Object update(UserReq userReq) {
        UserEntity userEntity = userRepository.findById(userReq.getId()).orElseThrow(() -> new RuntimeException("id 不存在"));
        ConvertUtils.copyNotNullProperties(userReq, userEntity);
        return userRepository.save(userEntity);
    }

    public Object page(UserReq userReq, Pageable pageable) {
        Page<UserEntity> all = userRepository.findAll((JpaQuerySpecification<UserEntity>) query -> {
            query.and(userReq, UserEntity.class);
        }, pageable);
        return all;
    }


}
