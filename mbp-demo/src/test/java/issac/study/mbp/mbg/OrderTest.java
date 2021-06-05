package issac.study.mbp.mbg;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import issac.study.mbp.base.BaseServiceTest;
import issac.study.mbp.core.mapper.OrderDynamicSqlSupport;
import issac.study.mbp.core.mapper.OrderMapper;
import issac.study.mbp.core.model.Order;
import org.junit.Test;
import org.mybatis.dynamic.sql.SqlBuilder;
import org.mybatis.dynamic.sql.render.RenderingStrategies;
import org.mybatis.dynamic.sql.select.SelectDSLCompleter;
import org.mybatis.dynamic.sql.select.render.SelectStatementProvider;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
/**
 * @author issac.hu
 */
public class OrderTest extends BaseServiceTest {

    @Autowired
    OrderMapper orderMapper;


    @Test
    public void testList() {
        List<Order> select = orderMapper.select(SelectDSLCompleter.allRows());
        System.out.println(JSON.toJSONString(select));
        System.out.println("-----------------------");
        SelectStatementProvider render = SqlBuilder.select(OrderMapper.selectList).from(OrderDynamicSqlSupport.order).build().render(RenderingStrategies.MYBATIS3);
        //从1开始
        PageHelper.startPage(2, 1);
        List<Order> orders = orderMapper.selectMany(render);
        System.out.println(JSON.toJSONString(orders));

    }

    @Test
    public void testWhere() {
        SelectStatementProvider render = SqlBuilder.select(OrderMapper.selectList).from(OrderDynamicSqlSupport.order)
                .where(OrderDynamicSqlSupport.id, SqlBuilder.isGreaterThan(1))
                .build().render(RenderingStrategies.MYBATIS3);
        //从1开始
        PageHelper.startPage(2, 1);
        List<Order> orders = orderMapper.selectMany(render);
        System.out.println(JSON.toJSONString(orders));
        System.out.println("---------------");
        PageHelper.startPage(1, 1);
        List<Order> select = orderMapper.select(q -> q.where(OrderDynamicSqlSupport.id, SqlBuilder.isGreaterThan(1)));
        System.out.println(JSON.toJSONString(select));
    }















}
