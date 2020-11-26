package issac.study.mybatisjpa.repository;

import issac.study.mybatisjpa.model.UserExtraEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface UserExtraRepository extends JpaRepository<UserExtraEntity, Integer>, JpaSpecificationExecutor<UserExtraEntity> {

}