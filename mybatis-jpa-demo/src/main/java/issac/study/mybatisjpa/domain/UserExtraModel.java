package issac.study.mybatisjpa.domain;

import issac.study.mybatisjpa.model.UserEntity;
import issac.study.mybatisjpa.model.UserExtraEntity;
import lombok.Data;

/**
 * @author issac.hu
 */
@Data
public class UserExtraModel extends UserEntity {
    private UserExtraEntity userExtra;
}
