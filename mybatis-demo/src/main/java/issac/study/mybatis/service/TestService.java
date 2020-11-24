package issac.study.mybatis.service;

import issac.study.mybatis.domain.Info;
import issac.study.mybatis.mapper.InfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * @author issac.hu
 */
@Service
public class TestService {

    @Autowired
    InfoMapper infoMapper;

    public Object test() {
        Info info = infoMapper.selectByPrimaryKey(1);
        return info;
    }

    public Object page(Integer pageNum,Integer pageSize) {
        List<Info> infos = infoMapper.selectPage(pageNum, pageSize);
        return infos;
    }

    public Object test(Integer id) {
        Info info = infoMapper.selectByPrimaryKey(id);
        return info;
    }

    public void test2() {
        Info info = new Info();
        info.setName(UUID.randomUUID().toString());
        infoMapper.insert(info);
    }
}
