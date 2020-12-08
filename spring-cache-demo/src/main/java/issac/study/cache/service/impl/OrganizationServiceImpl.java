package issac.study.cache.service.impl;

import issac.study.cache.core.jpa.base.BaseTreeJpaRepository;
import issac.study.cache.core.service.AbstractBaseTreeServiceImpl;
import issac.study.cache.model.OrganizationEntity;
import issac.study.cache.model.base.BaseTreeEntity;
import issac.study.cache.repository.OrganizationRepository;
import issac.study.cache.service.OrganizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author issac.hu
 */
@Service
public class OrganizationServiceImpl extends AbstractBaseTreeServiceImpl implements OrganizationService {

    @Autowired
    OrganizationRepository organizationRepository;

    @Override
    protected Class<? extends BaseTreeEntity> entityClass() {
        return OrganizationEntity.class;
    }

    @Override
    public BaseTreeJpaRepository baseJpaRepository() {
        return organizationRepository;
    }
}
