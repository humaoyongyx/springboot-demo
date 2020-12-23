package issac.study.mbp.model.cross;

import issac.study.mbp.model.ClassModel;
import issac.study.mbp.model.StudentModel;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author issac.hu
 */
@Data
public class ClassCrossModel extends ClassModel {

    private List<StudentModel> studentList = new ArrayList<>();
}
