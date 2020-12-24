package issac.study.mbp.core.http.okhttp;

import lombok.extern.slf4j.Slf4j;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import okhttp3.internal.platform.Platform;
import okhttp3.logging.HttpLoggingInterceptor;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import java.security.KeyManagementException;
import java.security.SecureRandom;
import java.util.concurrent.TimeUnit;

/**
 * @author issac.hu
 */
@Slf4j
public class OkHttpClientFactory {

    private static final OkHttpClient instance = buildInstance();

    private OkHttpClientFactory() {

    }

    public static OkHttpClient getInstance() {
        return instance;
    }

    private static OkHttpClient buildInstance() {
        SSLContext sslContext = Platform.get().getSSLContext(); //SSLContext.getInstance("TLS");
        DefaultTrustManager defaultTrustManager = new DefaultTrustManager();
        try {
            sslContext.init(null, new TrustManager[]{defaultTrustManager}, new SecureRandom());
        } catch (KeyManagementException e) {
            e.printStackTrace();
            throw new RuntimeException("okHttpClient实例化失败");
        }
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor(log::debug);
        //打印请求链路，debug级别
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                //设置https请求不校验证书
                .sslSocketFactory(sslContext.getSocketFactory(), defaultTrustManager)
                .hostnameVerifier(new TrustAnyHostnameVerifier())
                .retryOnConnectionFailure(true) // 连接失败是否重试
                //连接池设置
                .connectionPool(new ConnectionPool(10, 5, TimeUnit.MINUTES)) //默认5个连接 5分钟
                //连接时间设置 对应socket的connect / read / write
                .connectTimeout(10L, TimeUnit.SECONDS) //默认10s
                .readTimeout(10L, TimeUnit.SECONDS)  //默认10s
                .writeTimeout(10L, TimeUnit.SECONDS) //默认10s
                //请求日志打印
                .addInterceptor(httpLoggingInterceptor)
                .build();

        return okHttpClient;
    }


}
