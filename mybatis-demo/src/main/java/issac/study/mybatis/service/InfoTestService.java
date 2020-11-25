package issac.study.mybatis.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import issac.study.mybatis.domain.InfoTest;
import issac.study.mybatis.mapper.InfoTestMapper;
import org.springframework.stereotype.Service;

/**
 * 直接继承
 *
 * @author issac.hu
 */
@Service
public class InfoTestService extends ServiceImpl<InfoTestMapper, InfoTest> {

}
