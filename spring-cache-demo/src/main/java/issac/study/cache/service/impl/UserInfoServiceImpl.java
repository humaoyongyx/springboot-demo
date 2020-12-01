package issac.study.cache.service.impl;

import issac.study.cache.core.jpa.base.BaseJpaRepository;
import issac.study.cache.core.service.AbstractBaseCrudServiceImpl;
import issac.study.cache.model.UserInfoEntity;
import issac.study.cache.model.base.BaseEntity;
import issac.study.cache.repository.UserInfoRepository;
import issac.study.cache.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author issac.hu
 */
@Service
public class UserInfoServiceImpl extends AbstractBaseCrudServiceImpl implements UserInfoService {

    @Autowired
    UserInfoRepository userInfoRepository;

    @Override
    public Class<? extends BaseEntity> entityClass() {
        return UserInfoEntity.class;
    }

    @Override
    public BaseJpaRepository baseJpaRepository() {
        return userInfoRepository;
    }


}
