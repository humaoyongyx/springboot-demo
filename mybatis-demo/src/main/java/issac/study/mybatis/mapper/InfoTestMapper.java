package issac.study.mybatis.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import issac.study.mybatis.domain.InfoTest;

/**
 * 使用MybatisPlus 直接继承BaseMapper类，然后写InfoTest对应的表model
 * <p>
 * 这里InfoTest和表名称一致
 *
 * @author issac.hu
 */
public interface InfoTestMapper extends BaseMapper<InfoTest> {

}
