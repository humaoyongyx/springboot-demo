package issac.study.mbp.core.http.okhttp;

import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.apache.commons.lang3.StringUtils;

import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

/**
 * @author issac.hu
 */
public class OkHttpClientBaseImpl {

    /**
     * 慢请求时间 单位毫秒ms
     */
    protected static final long SLOW_TIME = 200L;
    protected static final MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json; charset=utf-8");
    protected static final MediaType MEDIA_TYPE_URL = MediaType.parse("application/x-www-form-urlencoded; charset=utf-8");
    protected static final MediaType MULTIPART_FORM_DATA = MediaType.parse("multipart/form-data; charset=utf-8");


    protected void setHeader(Map<String, String> headers, Request.Builder requestBuilder) {
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

    protected String checkUrl(String url) {
        Objects.requireNonNull(url);
        url = url.trim();
        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            throw new RuntimeException("非法的url参数");
        }
        return url;
    }

    protected RequestBody emptyRequestBody() {
        return RequestBody.create(MEDIA_TYPE_URL, "");
    }

    protected RequestBody convertParamMapToRequestBody(Map<String, Object> params) {
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

    protected HttpUrl.Builder convertParamMapToHttpUrl(String url, Map<String, Object> params) {
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
}
