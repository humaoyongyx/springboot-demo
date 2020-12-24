package issac.study.mbp.core.http.okhttp;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

/**
 * https 域名校验 信任所有
 *
 * @author issac.hu
 */
public class TrustAnyHostnameVerifier implements HostnameVerifier {

    @Override
    public boolean verify(String hostname, SSLSession session) {
        return true;
    }
}