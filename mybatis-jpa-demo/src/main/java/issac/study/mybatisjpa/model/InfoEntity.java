package issac.study.mybatisjpa.model;

import issac.study.mybatisjpa.model.base.BaseEntity;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "info")
@Data
public class InfoEntity extends BaseEntity {

    private String name;

    private String descp;

}
