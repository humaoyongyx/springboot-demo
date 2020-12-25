package issac.study.mbp.core.http;

import okhttp3.Response;

import java.io.File;
import java.util.Map;

/**
 * @author issac.hu
 */
public interface HttpClientFileService {

    /**
     * 上传文件
     *
     * @param url          请求地址
     * @param formFileName 表单中的文件名称
     * @param files        文件列表
     * @return
     */
    boolean upload(String url, String formFileName, File... files);

    /**
     * 上传文件
     *
     * @param url          请求地址
     * @param params       请求参数  参数拼接在url上
     * @param formFileName 表单中的文件名称
     * @param files        文件列表
     * @return
     */
    boolean upload(String url, Map<String, Object> params, String formFileName, File... files);

    /**
     * 文件上传接口
     *
     * @param url          请求地址
     * @param params       请求参数  参数拼接在url上
     * @param headers      请求头
     * @param formFileName 表单中的文件名称
     * @param files        文件列表
     * @return
     */
    boolean upload(String url, Map<String, Object> params, Map<String, String> headers, String formFileName, File... files);

    /**
     * 下载
     *
     * @param url 请求地址
     * @return
     */
    Response download(String url);

    /**
     * 下载
     *
     * @param url    请求地址
     * @param params 请求参数  参数拼接在url上
     * @return
     */
    Response download(String url, Map<String, Object> params);

    /**
     * 文件下载接口
     *
     * @param url     请求地址
     * @param params  请求参数  参数拼接在url上
     * @param headers 请求头
     * @return
     */
    Response download(String url, Map<String, Object> params, Map<String, String> headers);
}
