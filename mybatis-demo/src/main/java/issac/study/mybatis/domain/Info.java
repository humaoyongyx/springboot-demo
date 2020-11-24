package issac.study.mybatis.domain;

import java.io.Serializable;

/**
 * info
 * @author 
 */
public class Info implements Serializable {
    private Integer id;

    private String name;

    private String descp;

    private static final long serialVersionUID = 1L;

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
}