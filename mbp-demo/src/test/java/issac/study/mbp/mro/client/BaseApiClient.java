package issac.study.mbp.mro.client;


import issac.study.mbp.mro.http.HttpClientHelper;
import issac.study.mbp.mro.http.HttpClientHelperSimpleImpl;
import issac.study.mbp.mro.utils.SignUtils;
import issac.study.mbp.mro.utils.UrlUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * api调用的基础类
 *
 * @author issac.hu
 */
public class BaseApiClient {

    /**
     * 获取httpClient,此sdk里面的实现为简单实现，用户可以根据情况自己实现
     *
     * @return
     */
    public static HttpClientHelper getHttpClientHelper() {
        return new HttpClientHelperSimpleImpl();
    }

    /**
     * post的基础方法
     *
     * @param baseUrl
     * @param relativeUrl
     * @param params
     * @param jsonBody
     * @param apiId
     * @param apiSecret
     * @return
     */
    public static String post(String baseUrl, String relativeUrl, Map<String, Object> params, String jsonBody, String apiId, String apiSecret) {
        params = handleNoneMap(params);
        Map<String, String> headers = new HashMap<>();
        headers.put(SignUtils.API_AUTH, SignUtils.API);
        setCommon(params, null, apiId, apiSecret);
        String url = getUrl(baseUrl, relativeUrl);
        String result = getHttpClientHelper().postWithJsonBody(url, params, headers, jsonBody);
        return result;
    }

    /**
     * get的基础方法
     *
     * @param baseUrl
     * @param relativeUrl
     * @param params
     * @param apiId
     * @param apiSecret
     * @return
     */
    public static String get(String baseUrl, String relativeUrl, Map<String, Object> params, String apiId, String apiSecret) {
        params = handleNoneMap(params);
        Map<String, String> headers = new HashMap<>();
        headers.put(SignUtils.API_AUTH, SignUtils.API);
        setCommon(params, null, apiId, apiSecret);
        String url = baseUrl + relativeUrl;
        String result = getHttpClientHelper().get(url, params, headers);
        return result;
    }

    private static String getUrl(String baseUrl, String relativeUrl) {
        return UrlUtils.join(baseUrl, relativeUrl);
    }

    private static Map<String, Object> handleNoneMap(Map<String, Object> params) {
        if (params == null) {
            params = new HashMap<>();
        }
        return params;
    }

    private static void setCommon(Map<String, Object> params, String jsonBody, String apiId, String apiSecret) {
        params.put(SignUtils.PARAM_TIMESTAMP, System.currentTimeMillis());
        params.put(SignUtils.PARAM_VERSION, SignUtils.PARAM_VERSION_VALUE);
        if (jsonBody != null) {
            params.put(SignUtils.PARAM_JSON, jsonBody);
        }
        if (apiId != null && apiSecret != null) {
            params.put(SignUtils.PARAM_API_ID, apiId);
            String sign = SignUtils.getSign(new TreeMap<>(params), apiSecret);
            params.put(SignUtils.PARAM_SIGN, sign);
        } else {
            throw new RuntimeException("未设置apiId或者apiSecret");
        }
    }

}
