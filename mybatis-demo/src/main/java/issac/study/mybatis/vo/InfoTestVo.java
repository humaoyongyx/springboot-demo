package issac.study.mybatis.vo;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author issac.hu
 */
public class InfoTestVo {

    private Integer id;

    private String name;

    private String descp;

    private Date createTime;

    private String createTimeFmt;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescp() {
        return descp;
    }

    public void setDescp(String descp) {
        this.descp = descp;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getCreateTimeFmt() {
        if (this.createTime != null) {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(this.createTime);
        }
        return createTimeFmt;
    }

    public void setCreateTimeFmt(String createTimeFmt) {
        this.createTimeFmt = createTimeFmt;
    }
}
