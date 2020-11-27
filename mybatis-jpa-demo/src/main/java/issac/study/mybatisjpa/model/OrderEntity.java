package issac.study.mybatisjpa.model;

import issac.study.mybatisjpa.model.base.BaseEntity;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "order")
@Data
public class OrderEntity extends BaseEntity {

    private String name;

    private String no;

    private Integer userId;

}
