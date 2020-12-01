package issac.study.cache.model;

import issac.study.cache.model.base.BaseEntity;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author issac.hu
 */
@Entity
@Table(name = "user_info")
@Data
public class UserInfoEntity extends BaseEntity {
    private String name;
    private Integer age;
    private String idCardNo;

}
