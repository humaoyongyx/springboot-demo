package issac.study.mbp.core.mapper;

import org.mybatis.dynamic.sql.SqlColumn;
import org.mybatis.dynamic.sql.SqlTable;

import javax.annotation.Generated;
import java.sql.JDBCType;
import java.util.Date;

public final class OrderDynamicSqlSupport {
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2021-06-05T12:31:38.379+08:00", comments="Source Table: order")
    public static final Order order = new Order();

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2021-06-05T12:31:38.379+08:00", comments="Source field: order.id")
    public static final SqlColumn<Integer> id = order.id;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2021-06-05T12:31:38.38+08:00", comments="Source field: order.name")
    public static final SqlColumn<String> name = order.name;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2021-06-05T12:31:38.38+08:00", comments="Source field: order.no")
    public static final SqlColumn<String> no = order.no;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2021-06-05T12:31:38.38+08:00", comments="Source field: order.user_id")
    public static final SqlColumn<Integer> userId = order.userId;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2021-06-05T12:31:38.38+08:00", comments="Source field: order.lock_version")
    public static final SqlColumn<Long> lockVersion = order.lockVersion;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2021-06-05T12:31:38.38+08:00", comments="Source field: order.create_time")
    public static final SqlColumn<Date> createTime = order.createTime;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2021-06-05T12:31:38.38+08:00", comments="Source field: order.update_time")
    public static final SqlColumn<Date> updateTime = order.updateTime;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2021-06-05T12:31:38.379+08:00", comments="Source Table: order")
    public static final class Order extends SqlTable {
        public final SqlColumn<Integer> id = column("id", JDBCType.INTEGER);

        public final SqlColumn<String> name = column("name", JDBCType.VARCHAR);

        public final SqlColumn<String> no = column("no", JDBCType.VARCHAR);

        public final SqlColumn<Integer> userId = column("user_id", JDBCType.INTEGER);

        public final SqlColumn<Long> lockVersion = column("lock_version", JDBCType.BIGINT);

        public final SqlColumn<Date> createTime = column("create_time", JDBCType.TIMESTAMP);

        public final SqlColumn<Date> updateTime = column("update_time", JDBCType.TIMESTAMP);

        public Order() {
            super("`order`");
        }
    }
}