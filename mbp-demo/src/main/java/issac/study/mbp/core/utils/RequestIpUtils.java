package issac.study.mbp.core.utils;

import org.springframework.http.HttpHeaders;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * @author 10100586
 */
public class RequestIpUtils {

    private static final String UNKNOWN_CONTENT = "unknown";

    private RequestIpUtils() {
        throw new AssertionError("HttpRequestUtil can't be an instance");
    }

    public static String getIpAddress(HttpServletRequest request) {
        //X-Forwarded-For：Squid 服务代理
        String ipAddresses = request.getHeader("X-Forwarded-For");
        if (StringUtils.isEmpty(ipAddresses) || UNKNOWN_CONTENT.equalsIgnoreCase(ipAddresses)) {
            //Proxy-Client-IP：apache 服务代理
            ipAddresses = request.getHeader("Proxy-Client-IP");
        }
        if (StringUtils.isEmpty(ipAddresses) || UNKNOWN_CONTENT.equalsIgnoreCase(ipAddresses)) {
            //WL-Proxy-Client-IP：weblogic 服务代理
            ipAddresses = request.getHeader("WL-Proxy-Client-IP");
        }
        if (StringUtils.isEmpty(ipAddresses) || UNKNOWN_CONTENT.equalsIgnoreCase(ipAddresses)) {
            //HTTP_CLIENT_IP：有些代理服务器
            ipAddresses = request.getHeader("HTTP_CLIENT_IP");
        }
        if (StringUtils.isEmpty(ipAddresses) || UNKNOWN_CONTENT.equalsIgnoreCase(ipAddresses)) {
            //X-Real-IP：nginx服务代理
            ipAddresses = request.getHeader("X-Real-IP");
        }
        if (StringUtils.hasLength(ipAddresses)) {
            //有些网络通过多层代理，那么获取到的ip就会有多个，一般都是通过逗号（,）分割开来，并且第一个ip为客户端的真实IP
            return ipAddresses.split(",")[0];
        } else {
            //还是不能获取到，最后再通过request.getRemoteAddr();获取
            return request.getRemoteAddr();
        }
    }

    public static String getHost(HttpServletRequest request) {
        // 从请求头获取HOST
        String host = request.getHeader(HttpHeaders.HOST);
        if (StringUtils.hasLength(host)) {
            return host;
        } else {
            // 如请求头没有HOST参数，则获取远程主机名
            return request.getRemoteHost();
        }
    }

    public static String getUserAgent(HttpServletRequest request) {
        return request.getHeader(HttpHeaders.USER_AGENT);
    }
}
