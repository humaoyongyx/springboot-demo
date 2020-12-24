package issac.study.mbp.core.http;

import issac.study.mbp.core.http.okhttp.OkHttpClientServiceImpl;

/**
 * httpClient工具类
 *
 * @author issac.hu
 */
public class HttpHelper {

    private static HttpClientService httpClientService = new OkHttpClientServiceImpl();

    /**
     * 获取HttpService请求
     *
     * @return
     */
    public static HttpClientService client() {
        return httpClientService;
    }


}
