package issac.study.mybatisjpa.model;

import issac.study.mybatisjpa.model.base.BaseEntity;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Data
@Table(name = "user_extra")
public class UserExtraEntity extends BaseEntity {

    private Integer userId;

    private String name;

    private String descp;

}
