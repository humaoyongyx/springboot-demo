package issac.study.mbp.mro.demo;


import com.alibaba.fastjson.JSON;
import issac.study.mbp.core.utils.ConvertUtils;
import issac.study.mbp.mro.client.BaseApiClient;

import java.util.HashMap;
import java.util.Map;

/**
 * @author issac.hu
 */
public class ApiTest {

    /**
     * 由MRO系统提供,请求的apiId
     */
    private static String apiId = "1";
    /**
     * 由MRO系统提供，请求的apiSecret，用于签名
     */
    private static String apiSecret = "111111";
    /**
     * 由MRO系统提供，请求的地址
     */
    private static String url = "http://webdev08.shanghai-electric.com/dce-api/mro-gateway";

    /**
     * 测试Get方法的请求
     */
    public static void testGet() {

        //由MRO系统提供,具体的调用接口
        String apiPath = "/mro-base/config/all";
        Map<String, Object> params = new HashMap<>();
        // 请求的url参数
        params.put("xxx", "测试参数");
        String result = BaseApiClient.get(url, apiPath, params, apiId, apiSecret);
        // 返回json格式的字符串结果
        System.out.println(ConvertUtils.toJsonString(JSON.parseObject(result),true));
    }

    /**
     * 测试post方法的请求
     * 有json参数的请求
     */
    public static void testPost() {

        //由MRO系统提供,具体的调用接口
        String apiPath = "/mro-base/equipment/page";
        //请求的json参数【需要json格式的】
        String jsonBody = "{\n" +
                "    \"value\": \"测试\",\n" +
                "    \"ext\": [\n" +
                "        {\n" +
                "            \"key\": \"key1\",\n" +
                "            \"value\": \"%字段%\",\n" +
                "            \"operator\": \"like\"\n" +
                "        }\n" +
                "    ]\n" +
                "}";
        Map<String, Object> params = new HashMap<>();
        // 请求的url参数
        params.put("xxx", "测试参数");
        String result = BaseApiClient.post(url, apiPath, params, jsonBody, apiId, apiSecret);
        // 返回json格式的字符串结果
        System.out.println(result);
    }

    public static void main(String[] args) {
        testGet();
       // testPost();
    }
}
