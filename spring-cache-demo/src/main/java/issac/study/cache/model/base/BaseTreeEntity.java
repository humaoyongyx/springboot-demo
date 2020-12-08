package issac.study.cache.model.base;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

/**
 * @author issac.hu
 */
@Data
@MappedSuperclass
public class BaseTreeEntity extends BaseEntity {

    @Column(columnDefinition = "int DEFAULT NULL COMMENT '跟节点的id'")
    private Integer rootId;
    @Column(columnDefinition = "int DEFAULT NULL COMMENT '父节点的id'")
    private Integer parentId;
    @Column(columnDefinition = "varchar(255) DEFAULT NULL COMMENT '节点的层级路径'")
    private String idPath;
    @Column(columnDefinition = "int DEFAULT '1' COMMENT '节点的深度'")
    private Integer depth;
    @Column(columnDefinition = "bit(1) DEFAULT b'0' COMMENT '是否是叶子节点'")
    private Boolean leaf;
    @Column(columnDefinition = "int DEFAULT '1' COMMENT '同级节点的顺序'")
    private Integer seq;
    @Column(columnDefinition = "int DEFAULT '0' COMMENT '子节点的当前顺序索引'")
    private Integer childSeq;
}
