package issac.study.mybatis.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import issac.study.mybatis.domain.Info;
import issac.study.mybatis.mapper.InfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

    public Object page(Integer pageNum, Integer pageSize) {
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

    public Object pagePlus(Integer current, Integer size) {
        Page<Info> page = new Page<>(current, size);
        page.setOrders(OrderItem.descs("id"));
        Page<Info> infoPage = infoMapper.selectPage(page, new QueryWrapper<>());
        return infoPage;
    }

    public Object pagePlus(Pageable pageable) {
        Page<Info> page = new Page<>(pageable.getPageNumber(), pageable.getPageSize());
        Sort sort = pageable.getSort();
        List<OrderItem> orderItemList = new ArrayList<>();
        for (Sort.Order order : sort) {
            OrderItem orderItem;
            if (order.isAscending()) {
                orderItem = OrderItem.asc(order.getProperty());
            } else {
                orderItem = OrderItem.desc(order.getProperty());
            }
            orderItemList.add(orderItem);
        }
        if (!orderItemList.isEmpty()) {
            page.setOrders(orderItemList);
        }
        Page<Info> infoPage = infoMapper.selectPage(page, new QueryWrapper<>());
        return infoPage;
    }
}
