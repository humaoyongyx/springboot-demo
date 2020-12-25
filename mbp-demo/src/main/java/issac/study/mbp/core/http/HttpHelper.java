package issac.study.mbp.core.http;

import cn.hutool.core.map.CaseInsensitiveMap;
import issac.study.mbp.core.http.okhttp.OkHttpClientFileServiceImpl;
import issac.study.mbp.core.http.okhttp.OkHttpClientServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;
import java.util.Map;

/**
 * httpClient工具类
 *
 * @author issac.hu
 */
@Slf4j
public class HttpHelper {

    private static HttpClientService httpClientService = new OkHttpClientServiceImpl();

    private static HttpClientFileService httpClientFileService = new OkHttpClientFileServiceImpl();


    /**
     * 获取普通的HttpService请求
     *
     * @return
     */
    public static HttpClientService client() {
        return httpClientService;
    }

    /**
     * 获取文件相关的http请求
     *
     * @return
     */
    public static HttpClientFileService fileClient() {
        return httpClientFileService;
    }

    /**
     * 获取下载文件的文件名称
     *
     * @param headers
     * @return
     */
    public static String getFileNameFromDisposition(Map<String, List<String>> headers) {
        if (headers == null) {
            return null;
        }
        String fileName = null;
        CaseInsensitiveMap<String, List<String>> headersIgnoreCase = new CaseInsensitiveMap<>(headers);
        List<String> dispositionList = headersIgnoreCase.get("Content-Disposition");
        String disposition = null;
        if (dispositionList != null && dispositionList.size() > 0) {
            disposition = dispositionList.get(0);
        }
        if (StringUtils.isNotBlank(disposition)) {
            disposition = disposition.replace("attachment;", "").trim();
            if (disposition.indexOf(";") > 0) {
                fileName = disposition.split(";")[0];
            } else {
                fileName = disposition;
            }
            fileName = fileName.replace("filename=", "").replace("filename*=utf-8''", "");
        }
        if (StringUtils.isNotBlank(fileName)) {
            try {
                return URLDecoder.decode(fileName, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                log.error("getFileNameFromDisposition error:", e);
                return null;
            }
        }
        return null;
    }

}
