package issac.study.mybatisjpa.mapper;

import issac.study.mybatisjpa.domain.UserExtraModel;

import java.util.List;

public interface UserMapper {

    List<UserExtraModel> selectAll();

}