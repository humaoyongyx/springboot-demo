package issac.study.mybatis.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import issac.study.mybatis.domain.Info;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface InfoMapper extends BaseMapper<Info> {
    int deleteByPrimaryKey(Integer id);

    int insert(Info record);

    int insertSelective(Info record);

    Info selectByPrimaryKey(Integer id);

    List<Info> selectPage(@Param("pageNum") Integer pageNum, @Param("pageSize") Integer pageSize);

    int updateByPrimaryKeySelective(Info record);

    int updateByPrimaryKey(Info record);
}