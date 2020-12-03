package issac.study.cache.model;

import issac.study.cache.model.base.BaseEntity;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author issac.hu
 */
@Data
@Entity
@Table(name = "config")
public class ConfigEntity extends BaseEntity {

    private String cKey;

    private String cVal;

    private String cDesc;

}
