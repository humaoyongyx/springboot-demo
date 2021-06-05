package issac.study.mbp.core.model;

import javax.annotation.Generated;
import java.io.Serializable;
import java.util.Date;

public class Order implements Serializable {
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2021-06-05T12:31:38.371+08:00", comments="Source field: order.id")
    private Integer id;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2021-06-05T12:31:38.375+08:00", comments="Source field: order.name")
    private String name;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2021-06-05T12:31:38.375+08:00", comments="Source field: order.no")
    private String no;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2021-06-05T12:31:38.375+08:00", comments="Source field: order.user_id")
    private Integer userId;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2021-06-05T12:31:38.375+08:00", comments="Source field: order.lock_version")
    private Long lockVersion;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2021-06-05T12:31:38.375+08:00", comments="Source field: order.create_time")
    private Date createTime;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2021-06-05T12:31:38.376+08:00", comments="Source field: order.update_time")
    private Date updateTime;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2021-06-05T12:31:38.376+08:00", comments="Source Table: order")
    private static final long serialVersionUID = 1L;

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2021-06-05T12:31:38.374+08:00", comments="Source field: order.id")
    public Integer getId() {
        return id;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2021-06-05T12:31:38.375+08:00", comments="Source field: order.id")
    public void setId(Integer id) {
        this.id = id;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2021-06-05T12:31:38.375+08:00", comments="Source field: order.name")
    public String getName() {
        return name;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2021-06-05T12:31:38.375+08:00", comments="Source field: order.name")
    public void setName(String name) {
        this.name = name;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2021-06-05T12:31:38.375+08:00", comments="Source field: order.no")
    public String getNo() {
        return no;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2021-06-05T12:31:38.375+08:00", comments="Source field: order.no")
    public void setNo(String no) {
        this.no = no;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2021-06-05T12:31:38.375+08:00", comments="Source field: order.user_id")
    public Integer getUserId() {
        return userId;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2021-06-05T12:31:38.375+08:00", comments="Source field: order.user_id")
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2021-06-05T12:31:38.375+08:00", comments="Source field: order.lock_version")
    public Long getLockVersion() {
        return lockVersion;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2021-06-05T12:31:38.375+08:00", comments="Source field: order.lock_version")
    public void setLockVersion(Long lockVersion) {
        this.lockVersion = lockVersion;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2021-06-05T12:31:38.375+08:00", comments="Source field: order.create_time")
    public Date getCreateTime() {
        return createTime;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2021-06-05T12:31:38.375+08:00", comments="Source field: order.create_time")
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2021-06-05T12:31:38.376+08:00", comments="Source field: order.update_time")
    public Date getUpdateTime() {
        return updateTime;
    }

    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2021-06-05T12:31:38.376+08:00", comments="Source field: order.update_time")
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2021-06-05T12:31:38.376+08:00", comments="Source Table: order")
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", name=").append(name);
        sb.append(", no=").append(no);
        sb.append(", userId=").append(userId);
        sb.append(", lockVersion=").append(lockVersion);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}