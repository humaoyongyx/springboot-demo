package issac.study.mybatis.mapper;

import issac.study.mybatis.domain.Info;

public interface InfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Info record);

    int insertSelective(Info record);

    Info selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Info record);

    int updateByPrimaryKey(Info record);
}