package issac.study.mbp.mro.http;

import java.util.Map;

/**
 * @author issac.hu
 */
public interface HttpClientHelper {

    /**
     * post 带json请求请求
     *
     * @param url      请求地址
     * @param params   请求的url参数
     * @param headers  请求的header头部
     * @param jsonBody 请求的json字符串body
     * @return
     */
    String postWithJsonBody(String url, Map<String, Object> params, Map<String, String> headers, String jsonBody);

    /**
     * get 请求
     *
     * @param url     请求地址
     * @param params  请求的url参数
     * @param headers 请求的header头部
     * @return
     */
    String get(String url, Map<String, Object> params, Map<String, String> headers);
}
