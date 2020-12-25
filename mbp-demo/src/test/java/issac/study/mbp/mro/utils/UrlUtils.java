package issac.study.mbp.mro.utils;

import org.apache.commons.lang3.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

/**
 * @author issac.hu
 */
public class UrlUtils {
    
    /**
     * 将多个url拼接成合法的http请求url
     *
     * @param urls
     * @return
     */
    public static String join(String... urls) {
        if (urls == null || urls.length == 0) {
            return "";
        }
        StringBuffer result = new StringBuffer();
        for (int i = 0; i < urls.length; i++) {
            String url = urls[i];
            String temp;
            if (StringUtils.isNotBlank(url)) {
                temp = url;
                if (url.endsWith("/")) {
                    temp = url.substring(0, url.length() - 1);
                }
                if (i > 0) {
                    if (!temp.startsWith("/")) {
                        temp = "/" + temp;
                    }
                }
                result.append(temp);
            }
        }
        return result.toString();
    }

    public static String convertMapToUrl(Map<String, Object> params, String charset) {
        StringBuffer urlBuffer = new StringBuffer();
        if (params != null && !params.isEmpty()) {
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                try {
                    urlBuffer.append(URLEncoder.encode(entry.getKey(), charset));
                    urlBuffer.append("=");
                    urlBuffer.append(URLEncoder.encode(String.valueOf(entry.getValue()), charset));
                    urlBuffer.append("&");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
            return urlBuffer.substring(0, urlBuffer.length() - 1);
        }
        return "";
    }

    public static String joinUrl(String url, Map<String, Object> paramMap) {
        String param = convertMapToUrl(paramMap, "UTF-8");
        if (StringUtils.isBlank(param)) {
            return url;
        }
        if (url.contains("?")) {
            return url + "&" + param;
        } else {
            return url + "?" + param;
        }
    }

}
