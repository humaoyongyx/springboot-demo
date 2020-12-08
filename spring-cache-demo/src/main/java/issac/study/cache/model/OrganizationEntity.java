package issac.study.cache.model;

import issac.study.cache.model.base.BaseTreeEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author issac.hu
 */
@Entity
@Table(name = "organization")
public class OrganizationEntity extends BaseTreeEntity {
    private String name;
}
