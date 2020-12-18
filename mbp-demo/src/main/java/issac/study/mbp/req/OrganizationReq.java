package issac.study.mbp.req;

import issac.study.mbp.core.req.BaseTreeReq;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.Date;


/**
 * @author issac.hu
 */
@Data
public class OrganizationReq extends BaseTreeReq {
    @NotBlank
    private String name;

    private Date createdTime;
}
