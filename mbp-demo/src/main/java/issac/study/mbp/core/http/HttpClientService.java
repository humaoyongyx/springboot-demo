package issac.study.mbp.core.http;

import java.util.Map;

/**
 * @author issac.hu
 */
public interface HttpClientService {

    /**
     * get请求
     *
     * @param url 请求地址
     * @return
     */
    String get(String url);

    /**
     * get请求
     *
     * @param url    请求地址
     * @param params 请求参数 参数最终拼接在url上
     * @return
     */
    String get(String url, Map<String, Object> params);

    /**
     * get请求
     *
     * @param url     请求地址
     * @param params  请求参数 参数最终拼接在url上
     * @param headers 请求头
     * @return
     */
    String get(String url, Map<String, Object> params, Map<String, String> headers);

    /**
     * post请求
     *
     * @param url 请求地址
     * @return
     */
    String post(String url);

    /**
     * post请求
     *
     * @param url    请求地址
     * @param params 请求参数 参数拼接在body中
     * @return
     */
    String post(String url, Map<String, Object> params);

    /**
     * post请求
     *
     * @param url     请求地址
     * @param params  请求参数 参数拼接在body中
     * @param headers 请求头
     * @return
     */
    String post(String url, Map<String, Object> params, Map<String, String> headers);


    /**
     * post 带json请求请求
     *
     * @param url      请求地址
     * @param params   请求参数 参数最终拼接在url上，而不是body中
     * @param headers  请求头
     * @param jsonBody 请求的json字符串 拼接在body中
     * @return
     */
    String post(String url, Map<String, Object> params, Map<String, String> headers, String jsonBody);

    /**
     * 请求通用方法
     *
     * @param httpMethod 请求方法
     * @param url        请求地址
     * @param params     请求参数
     * @param headers    请求头
     * @param jsonBody   请求jsonBody体 如果此项不为空，params将会被拼接到url参数上，而且此项只允许使用在POST和PUT请求上
     * @return
     */
    String execute(HttpMethod httpMethod, String url, Map<String, Object> params, Map<String, String> headers, String jsonBody);
}
