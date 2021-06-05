package issac.study.mbp.core.mapper;

import issac.study.mbp.core.model.Order;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;
import org.mybatis.dynamic.sql.BasicColumn;
import org.mybatis.dynamic.sql.delete.DeleteDSLCompleter;
import org.mybatis.dynamic.sql.delete.render.DeleteStatementProvider;
import org.mybatis.dynamic.sql.insert.render.InsertStatementProvider;
import org.mybatis.dynamic.sql.select.CountDSLCompleter;
import org.mybatis.dynamic.sql.select.SelectDSLCompleter;
import org.mybatis.dynamic.sql.select.render.SelectStatementProvider;
import org.mybatis.dynamic.sql.update.UpdateDSL;
import org.mybatis.dynamic.sql.update.UpdateDSLCompleter;
import org.mybatis.dynamic.sql.update.UpdateModel;
import org.mybatis.dynamic.sql.update.render.UpdateStatementProvider;
import org.mybatis.dynamic.sql.util.SqlProviderAdapter;
import org.mybatis.dynamic.sql.util.mybatis3.MyBatis3Utils;

import javax.annotation.Generated;
import java.util.List;
import java.util.Optional;

import static issac.study.mbp.core.mapper.OrderDynamicSqlSupport.*;
import static org.mybatis.dynamic.sql.SqlBuilder.isEqualTo;

@Mapper
public interface OrderMapper {
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2021-06-05T12:31:38.39+08:00", comments="Source Table: order")
    BasicColumn[] selectList = BasicColumn.columnList(id, name, no, userId, lockVersion, createTime, updateTime);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2021-06-05T12:31:38.381+08:00", comments="Source Table: order")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    long count(SelectStatementProvider selectStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2021-06-05T12:31:38.382+08:00", comments="Source Table: order")
    @DeleteProvider(type=SqlProviderAdapter.class, method="delete")
    int delete(DeleteStatementProvider deleteStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2021-06-05T12:31:38.382+08:00", comments="Source Table: order")
    @InsertProvider(type=SqlProviderAdapter.class, method="insert")
    @SelectKey(statement="SELECT LAST_INSERT_ID()", keyProperty="record.id", before=false, resultType=Integer.class)
    int insert(InsertStatementProvider<Order> insertStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2021-06-05T12:31:38.385+08:00", comments="Source Table: order")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    @ResultMap("OrderResult")
    Optional<Order> selectOne(SelectStatementProvider selectStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2021-06-05T12:31:38.386+08:00", comments="Source Table: order")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    @Results(id="OrderResult", value = {
        @Result(column="id", property="id", jdbcType=JdbcType.INTEGER, id=true),
        @Result(column="name", property="name", jdbcType=JdbcType.VARCHAR),
        @Result(column="no", property="no", jdbcType=JdbcType.VARCHAR),
        @Result(column="user_id", property="userId", jdbcType=JdbcType.INTEGER),
        @Result(column="lock_version", property="lockVersion", jdbcType=JdbcType.BIGINT),
        @Result(column="create_time", property="createTime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="update_time", property="updateTime", jdbcType=JdbcType.TIMESTAMP)
    })
    List<Order> selectMany(SelectStatementProvider selectStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2021-06-05T12:31:38.386+08:00", comments="Source Table: order")
    @UpdateProvider(type=SqlProviderAdapter.class, method="update")
    int update(UpdateStatementProvider updateStatement);

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2021-06-05T12:31:38.387+08:00", comments="Source Table: order")
    default long count(CountDSLCompleter completer) {
        return MyBatis3Utils.countFrom(this::count, order, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2021-06-05T12:31:38.387+08:00", comments="Source Table: order")
    default int delete(DeleteDSLCompleter completer) {
        return MyBatis3Utils.deleteFrom(this::delete, order, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2021-06-05T12:31:38.388+08:00", comments="Source Table: order")
    default int deleteByPrimaryKey(Integer id_) {
        return delete(c -> 
            c.where(id, isEqualTo(id_))
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2021-06-05T12:31:38.388+08:00", comments="Source Table: order")
    default int insert(Order record) {
        return MyBatis3Utils.insert(this::insert, record, order, c ->
            c.map(name).toProperty("name")
            .map(no).toProperty("no")
            .map(userId).toProperty("userId")
            .map(lockVersion).toProperty("lockVersion")
            .map(createTime).toProperty("createTime")
            .map(updateTime).toProperty("updateTime")
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2021-06-05T12:31:38.389+08:00", comments="Source Table: order")
    default int insertSelective(Order record) {
        return MyBatis3Utils.insert(this::insert, record, order, c ->
            c.map(name).toPropertyWhenPresent("name", record::getName)
            .map(no).toPropertyWhenPresent("no", record::getNo)
            .map(userId).toPropertyWhenPresent("userId", record::getUserId)
            .map(lockVersion).toPropertyWhenPresent("lockVersion", record::getLockVersion)
            .map(createTime).toPropertyWhenPresent("createTime", record::getCreateTime)
            .map(updateTime).toPropertyWhenPresent("updateTime", record::getUpdateTime)
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2021-06-05T12:31:38.391+08:00", comments="Source Table: order")
    default Optional<Order> selectOne(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectOne(this::selectOne, selectList, order, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2021-06-05T12:31:38.391+08:00", comments="Source Table: order")
    default List<Order> select(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectList(this::selectMany, selectList, order, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2021-06-05T12:31:38.392+08:00", comments="Source Table: order")
    default List<Order> selectDistinct(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectDistinct(this::selectMany, selectList, order, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2021-06-05T12:31:38.392+08:00", comments="Source Table: order")
    default Optional<Order> selectByPrimaryKey(Integer id_) {
        return selectOne(c ->
            c.where(id, isEqualTo(id_))
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2021-06-05T12:31:38.392+08:00", comments="Source Table: order")
    default int update(UpdateDSLCompleter completer) {
        return MyBatis3Utils.update(this::update, order, completer);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2021-06-05T12:31:38.393+08:00", comments="Source Table: order")
    static UpdateDSL<UpdateModel> updateAllColumns(Order record, UpdateDSL<UpdateModel> dsl) {
        return dsl.set(name).equalTo(record::getName)
                .set(no).equalTo(record::getNo)
                .set(userId).equalTo(record::getUserId)
                .set(lockVersion).equalTo(record::getLockVersion)
                .set(createTime).equalTo(record::getCreateTime)
                .set(updateTime).equalTo(record::getUpdateTime);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2021-06-05T12:31:38.393+08:00", comments="Source Table: order")
    static UpdateDSL<UpdateModel> updateSelectiveColumns(Order record, UpdateDSL<UpdateModel> dsl) {
        return dsl.set(name).equalToWhenPresent(record::getName)
                .set(no).equalToWhenPresent(record::getNo)
                .set(userId).equalToWhenPresent(record::getUserId)
                .set(lockVersion).equalToWhenPresent(record::getLockVersion)
                .set(createTime).equalToWhenPresent(record::getCreateTime)
                .set(updateTime).equalToWhenPresent(record::getUpdateTime);
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2021-06-05T12:31:38.394+08:00", comments="Source Table: order")
    default int updateByPrimaryKey(Order record) {
        return update(c ->
            c.set(name).equalTo(record::getName)
            .set(no).equalTo(record::getNo)
            .set(userId).equalTo(record::getUserId)
            .set(lockVersion).equalTo(record::getLockVersion)
            .set(createTime).equalTo(record::getCreateTime)
            .set(updateTime).equalTo(record::getUpdateTime)
            .where(id, isEqualTo(record::getId))
        );
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2021-06-05T12:31:38.395+08:00", comments="Source Table: order")
    default int updateByPrimaryKeySelective(Order record) {
        return update(c ->
            c.set(name).equalToWhenPresent(record::getName)
            .set(no).equalToWhenPresent(record::getNo)
            .set(userId).equalToWhenPresent(record::getUserId)
            .set(lockVersion).equalToWhenPresent(record::getLockVersion)
            .set(createTime).equalToWhenPresent(record::getCreateTime)
            .set(updateTime).equalToWhenPresent(record::getUpdateTime)
            .where(id, isEqualTo(record::getId))
        );
    }
}