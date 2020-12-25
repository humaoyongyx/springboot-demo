package issac.study.mbp.mro.http;


import issac.study.mbp.core.http.HttpHelper;

import java.util.Map;

/**
 * 简单的httpclient实现类
 *
 * @author issac.hu
 */
public class HttpClientHelperSimpleImpl implements HttpClientHelper {

    @Override
    public String postWithJsonBody(String url, Map<String, Object> params, Map<String, String> headers, String jsonBody) {
        return HttpHelper.client().post(url, params,headers,jsonBody);
    }

    @Override
    public String get(String url, Map<String, Object> params, Map<String, String> headers) {
       return HttpHelper.client().get(url,params,headers);
    }
}
