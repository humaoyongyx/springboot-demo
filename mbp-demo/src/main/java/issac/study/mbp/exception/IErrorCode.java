package issac.study.mbp.exception;

/**
 * 通用错误接口
 * @author humy6
 * @Date: 2019/7/3 14:19
 */
public interface IErrorCode {

    /**
     *返回错误代码
     * @return
     */
    Integer getErrorCode();

    /**
     *返回错误消息
     * @return
     */
    String getMsg();

}
