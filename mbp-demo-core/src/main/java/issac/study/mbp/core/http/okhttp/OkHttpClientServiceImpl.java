package issac.study.mbp.core.http.okhttp;

import issac.study.mbp.core.http.HttpClientService;
import issac.study.mbp.core.http.HttpMethod;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;

/**
 * HttpClientService的okhttp实现
 *
 * @author issac.hu
 */
@Slf4j
public class OkHttpClientServiceImpl extends OkHttpClientBaseImpl implements HttpClientService {

    private OkHttpClient okHttpClient = OkHttpClientFactory.getInstance();

    public OkHttpClientServiceImpl() {
    }

    public OkHttpClientServiceImpl(OkHttpClient okHttpClient) {
        this.okHttpClient = okHttpClient;
    }

    @Override
    public String get(String url) {
        return get(url, null);
    }

    @Override
    public String get(String url, Map<String, Object> params) {
        return get(url, params, null);
    }

    @Override
    public String get(String url, Map<String, Object> params, Map<String, String> headers) {
        return execute(HttpMethod.GET, url, params, headers, null);
    }

    @Override
    public String post(String url) {
        return post(url, null);
    }

    @Override
    public String post(String url, Map<String, Object> params) {
        return post(url, params, null);
    }

    @Override
    public String post(String url, Map<String, Object> params, Map<String, String> headers) {
        return post(url, params, headers, null);
    }

    @Override
    public String post(String url, Map<String, Object> params, Map<String, String> headers, String jsonBody) {
        return execute(HttpMethod.POST, url, params, headers, jsonBody);
    }

    @Override
    public String execute(HttpMethod httpMethod, String url, Map<String, Object> params, Map<String, String> headers, String jsonBody) {
        Objects.requireNonNull(httpMethod);
        url = checkUrl(url);
        Request.Builder requestBuilder = new Request.Builder();
        RequestBody requestBody = null;
        //设置头信息
        setHeader(headers, requestBuilder);
        if (httpMethod == HttpMethod.GET || httpMethod == HttpMethod.DELETE) {
            HttpUrl.Builder urlBuilder = convertParamMapToHttpUrl(url, params);
            requestBuilder.url(urlBuilder.build());
        } else if (httpMethod == HttpMethod.POST || httpMethod == HttpMethod.PUT) {
            if (jsonBody == null) {
                requestBuilder.url(url);
                //如果body体不带有json，那么参数放在body中
                requestBody = convertParamMapToRequestBody(params);
            } else {
                //如果body体带有json，那么参数放在url中
                HttpUrl.Builder urlBuilder = convertParamMapToHttpUrl(url, params);
                requestBuilder.url(urlBuilder.build());
                requestBody = RequestBody.create(MEDIA_TYPE_JSON, jsonBody);
            }
            //POST和PUT必须要有body体
            if (requestBody == null) {
                requestBody = emptyRequestBody();
            }
        }

        requestBuilder.method(httpMethod.name(), requestBody);
        long begin = System.currentTimeMillis();
        Request request = requestBuilder.build();
        Call call = okHttpClient.newCall(request);
        //参考okhttp3.ResponseBody,关闭
        try (Response response = call.execute()) {
            return response.body().string();
        } catch (IOException e) {
            log.error("okHttpClient execute error:", e);
        } finally {
            long end = System.currentTimeMillis();
            long cost = end - begin;
            if (cost > SLOW_TIME) {
                log.warn("slow request: {} , cost: {}ms ", request, cost);
            }
        }
        return null;
    }

}
