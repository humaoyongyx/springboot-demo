package issac.study.cache.core.page;

import lombok.Data;

import java.util.List;

/**
 * @author issac.hu
 */
@Data
public class PageParam {
    private Long offset;
    private Integer size;
    private List<String> sorts;
}
