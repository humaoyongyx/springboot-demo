package issac.study.mbp.model.cross;

import issac.study.mbp.model.SchoolModel;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author issac.hu
 */
@Data
public class SchoolCrossModel extends SchoolModel {
    private List<ClassCrossModel> classList = new ArrayList<>();
}
