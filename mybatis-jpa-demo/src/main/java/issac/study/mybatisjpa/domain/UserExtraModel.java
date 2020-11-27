package issac.study.mybatisjpa.domain;

import issac.study.mybatisjpa.model.OrderEntity;
import issac.study.mybatisjpa.model.UserEntity;
import issac.study.mybatisjpa.model.UserExtraEntity;
import lombok.Data;

import java.util.List;

/**
 * @author issac.hu
 */
@Data
public class UserExtraModel extends UserEntity {
    private UserExtraEntity userExtra;
    private List<OrderEntity> orders;
}
