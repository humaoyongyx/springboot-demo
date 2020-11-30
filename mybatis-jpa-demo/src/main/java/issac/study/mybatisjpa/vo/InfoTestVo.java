package issac.study.mybatisjpa.vo;

import issac.study.mybatisjpa.vo.base.BaseVo;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author issac.hu
 */
public class InfoTestVo extends BaseVo {

    private String name;

    private String descp;

    private Date createTime;

    private String createTimeFmt;

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
