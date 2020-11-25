package issac.study.mybatis.req;

import lombok.Data;

import java.util.List;

/**
 * @author issac.hu
 */
@Data
public class InfoTestReq {
    private List<Integer> id;

    private String name;

    private String descp;

    private String createTimeBegin;
}
