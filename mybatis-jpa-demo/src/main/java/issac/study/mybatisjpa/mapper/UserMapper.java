package issac.study.mybatisjpa.mapper;

import issac.study.mybatisjpa.core.page.PageParam;
import issac.study.mybatisjpa.domain.UserExtraModel;
import issac.study.mybatisjpa.req.UserReq;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserMapper {

    List<UserExtraModel> selectPage(@Param("param") UserReq userReq, @Param("page") PageParam pageParam);

    long selectPageTotal(@Param("param") UserReq userReq);
}