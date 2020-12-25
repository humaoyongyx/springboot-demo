package issac.study.mbp.mro.utils;

import org.apache.commons.codec.digest.DigestUtils;

import java.util.Map;
import java.util.TreeMap;

/**
 * @author issac.hu
 */
public class SignUtils {

    public static final String PARAM_VERSION = "_version";
    public static final String PARAM_VERSION_VALUE = "1.0";
    public static final String PARAM_TIMESTAMP = "_timestamp";
    public static final String PARAM_SIGN = "_sign";
    public static final String PARAM_API_ID = "_apiId";
    public static final String PARAM_JSON = "_json";
    public static final String API_AUTH = "apiAuth";
    public static final String API = "API";

    /**
     * 计算sign
     *
     * @param treeMap
     * @param appSecret
     * @return
     */
    public static String getSign(TreeMap<String, Object> treeMap, String appSecret) {
        StringBuffer calcSign = new StringBuffer();
        for (Map.Entry<String, Object> entry : treeMap.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            if (PARAM_SIGN.equals(key) || value == null) {
                continue;
            }
            calcSign.append(key + value);
        }
        calcSign.append(appSecret);
        return DigestUtils.md5Hex(calcSign.toString());
    }

}