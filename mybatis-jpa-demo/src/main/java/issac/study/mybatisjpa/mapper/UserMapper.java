package issac.study.mybatisjpa.mapper;

import issac.study.mybatisjpa.domain.UserExtraModel;
import issac.study.mybatisjpa.req.UserReq;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserMapper {

    List<UserExtraModel> selectPage(@Param("param") UserReq userReq, @Param("offset") Long offset, @Param("size") Integer size, @Param("sorts") List<String> sorts);

    long selectPageTotal(@Param("param") UserReq userReq);
}