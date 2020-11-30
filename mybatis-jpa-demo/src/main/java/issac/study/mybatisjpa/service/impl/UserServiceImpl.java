package issac.study.mybatisjpa.service.impl;

import issac.study.mybatisjpa.core.jpa.JpaQuerySpecification;
import issac.study.mybatisjpa.model.UserEntity;
import issac.study.mybatisjpa.repository.UserRepository;
import issac.study.mybatisjpa.req.UserReq;
import issac.study.mybatisjpa.service.InfoService;
import issac.study.mybatisjpa.utils.ConvertUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * @author issac.hu
 */
@Service
public class UserServiceImpl {

    @Autowired
    UserRepository userRepository;

    @Autowired
    InfoService infoService;

    /**
     * 带有事务的处理
     *
     * @return
     */
    @Transactional
    public Object save() {
        UserEntity userEntity = new UserEntity();
        String name = UUID.randomUUID().toString();
        userEntity.setName(name);
        UserEntity save = userRepository.save(userEntity);
//        InfoEntity entity = new InfoEntity();
//        entity.setName(name);
//        infoService.baseJpaRepository().save(entity);
//        int i = 1 / 0;
        return save;
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
