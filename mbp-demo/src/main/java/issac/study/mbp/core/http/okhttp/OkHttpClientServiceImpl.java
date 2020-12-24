package issac.study.mbp.core.http.okhttp;

import issac.study.mbp.core.http.HttpClientService;
import issac.study.mbp.core.http.HttpMethod;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

/**
 * HttpClientService的okhttp实现
 *
 * @author issac.hu
 */
@Slf4j
public class OkHttpClientServiceImpl implements HttpClientService {

    /**
     * 慢请求时间 单位毫秒ms
     */
    private static final long SLOW_TIME = 200L;

    private static final MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json; charset=utf-8");
    private static final MediaType MEDIA_TYPE_URL = MediaType.parse("application/x-www-form-urlencoded; charset=utf-8");
    private static final MediaType MULTIPART_FORM_DATA = MediaType.parse("multipart/form-data; charset=utf-8");
    private OkHttpClient okHttpClient;

    public OkHttpClientServiceImpl() {
        this.okHttpClient = OkHttpClientFactory.getInstance();
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
        try {
            return okHttpClient.newCall(request).execute().body().string();
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

    private void setHeader(Map<String, String> headers, Request.Builder requestBuilder) {
        if (headers != null && !headers.isEmpty()) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                if (StringUtils.isNotBlank(key) && StringUtils.isNotBlank(value)) {
                    requestBuilder.addHeader(key, value);
                }
            }
        }
    }

    private String checkUrl(String url) {
        Objects.requireNonNull(url);
        url = url.trim();
        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            throw new RuntimeException("非法的url参数");
        }
        return url;
    }

    private RequestBody emptyRequestBody() {
        return RequestBody.create(MEDIA_TYPE_URL, "");
    }

    private RequestBody convertParamMapToRequestBody(Map<String, Object> params) {
        if (params != null && !params.isEmpty()) {
            StringBuilder paramUrl = new StringBuilder();
            Iterator<Map.Entry<String, Object>> iterator = params.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, Object> entry = iterator.next();
                String key = entry.getKey();
                Object value = entry.getValue();
                if (StringUtils.isNotBlank(key) && value != null) {
                    paramUrl.append(entry.getKey()).append("=").append(entry.getValue());
                    if (iterator.hasNext()) {
                        paramUrl.append("&");
                    }
                }
            }
            return RequestBody.create(MEDIA_TYPE_URL, paramUrl.toString());
        }
        return null;
    }

    private HttpUrl.Builder convertParamMapToHttpUrl(String url, Map<String, Object> params) {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(url).newBuilder();
        if (params != null && !params.isEmpty()) {
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();
                if (StringUtils.isNotBlank(key) && value != null) {
                    String val = String.valueOf(value);
                    urlBuilder.addQueryParameter(key, val);
                }
            }
        }
        return urlBuilder;
    }

    @Override
    public boolean uploadFiles(String url, Map<String, Object> params, Map<String, String> headers, String formFileName, File... files) {
        url = checkUrl(url);
        HttpUrl httpUrl = convertParamMapToHttpUrl(url, params).build();
        Request.Builder requestBuilder = new Request.Builder();
        requestBuilder.url(httpUrl);
        setHeader(headers, requestBuilder);
        MultipartBody.Builder multiPartBodyBuilder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        for (File file : files) {
            multiPartBodyBuilder.addFormDataPart(formFileName, file.getName(), RequestBody.create(MULTIPART_FORM_DATA, file));
        }
        long begin = System.currentTimeMillis();
        Request request = requestBuilder.post(multiPartBodyBuilder.build()).build();
        try {
            okHttpClient.newCall(request).execute();
            return true;
        } catch (IOException e) {
            log.error("okHttpClient uploadFiles error:", e);
        } finally {
            long end = System.currentTimeMillis();
            long cost = end - begin;
            if (cost > SLOW_TIME) {
                log.warn("slow request: {} , cost: {}ms ", request, cost);
            }
        }
        return false;
    }

}
