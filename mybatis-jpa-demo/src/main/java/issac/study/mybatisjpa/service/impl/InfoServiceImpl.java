package issac.study.mybatisjpa.service.impl;

import issac.study.mybatisjpa.core.jpa.base.BaseJpaRepository;
import issac.study.mybatisjpa.core.service.AbstractBaseCrudServiceImpl;
import issac.study.mybatisjpa.model.InfoEntity;
import issac.study.mybatisjpa.model.base.BaseEntity;
import issac.study.mybatisjpa.repository.InfoRepository;
import issac.study.mybatisjpa.service.InfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author issac.hu
 */
@Service
public class InfoServiceImpl extends AbstractBaseCrudServiceImpl implements InfoService {

    @Autowired
    InfoRepository infoRepository;

    @Override
    public BaseJpaRepository baseJpaRepository() {
        return infoRepository;
    }

    @Override
    public Class<? extends BaseEntity> entityClass() {
        return InfoEntity.class;
    }
    
}
