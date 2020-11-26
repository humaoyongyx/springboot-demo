package issac.study.mybatisjpa.model;

import issac.study.mybatisjpa.model.base.BaseEntity;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author issac.hu
 */
@Data
@Entity
@Table(name = "user")
public class UserEntity extends BaseEntity {
    private String name;
    private String descp;
}
