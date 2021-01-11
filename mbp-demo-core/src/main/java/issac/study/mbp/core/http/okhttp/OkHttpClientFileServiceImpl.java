package issac.study.mbp.core.http.okhttp;

import issac.study.mbp.core.http.HttpClientFileService;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;

import java.io.File;
import java.io.IOException;
import java.util.Map;

/**
 * HttpClientService的okhttp实现
 *
 * @author issac.hu
 */
@Slf4j
public class OkHttpClientFileServiceImpl extends OkHttpClientBaseImpl implements HttpClientFileService {

    private OkHttpClient okHttpClient = OkHttpClientFactory.getFileInstance();

    @Override
    public boolean upload(String url, String formFileName, File... files) {
        return upload(url, null, formFileName, files);
    }

    @Override
    public boolean upload(String url, Map<String, Object> params, String formFileName, File... files) {
        return upload(url, params, null, formFileName, files);
    }

    @Override
    public boolean upload(String url, Map<String, Object> params, Map<String, String> headers, String formFileName, File... files) {
        url = checkUrl(url);
        HttpUrl httpUrl = convertParamMapToHttpUrl(url, params).build();
        Request.Builder requestBuilder = new Request.Builder();
        requestBuilder.url(httpUrl);
        setHeader(headers, requestBuilder);
        MultipartBody.Builder multiPartBodyBuilder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        for (File file : files) {
            multiPartBodyBuilder.addFormDataPart(formFileName, file.getName(), RequestBody.create(MULTIPART_FORM_DATA, file));
        }
        Request request = requestBuilder.post(multiPartBodyBuilder.build()).build();
        long begin = System.currentTimeMillis();
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

    @Override
    public Response download(String url) {
        return download(url, null);
    }

    @Override
    public Response download(String url, Map<String, Object> params) {
        return download(url, params, null);
    }

    @Override
    public Response download(String url, Map<String, Object> params, Map<String, String> headers) {
        url = checkUrl(url);
        HttpUrl httpUrl = convertParamMapToHttpUrl(url, params).build();
        Request.Builder requestBuilder = new Request.Builder();
        requestBuilder.url(httpUrl);
        setHeader(headers, requestBuilder);
        Request request = requestBuilder.get().build();
        long begin = System.currentTimeMillis();
        try {
            return okHttpClient.newCall(request).execute();
        } catch (IOException e) {
            log.error("okHttpClient downloadFiles error:", e);
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
