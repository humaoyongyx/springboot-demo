package issac.study.cache.interceptor;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.util.*;

/**
 * @author humy6
 * @Date: 2019/8/2 8:36
 */
@Component
public class LogInterceptor implements HandlerInterceptor {

    private static final Logger LOGGER = LoggerFactory.getLogger(LogInterceptor.class);

    private static final String CONTENT_TYPE_JSON = "application/json";
    private static final Set<String> INCLUDE_METHODS = new HashSet<>();
    private static final String MASK_VALUE = "******";

    @Value("${log.sensitive.words:}")
    private String sensitiveWords;

    /**
     * 密码日志过滤
     */
    private static final Set<String> SENSITIVE_WORDS = new HashSet<>();

    static {
        INCLUDE_METHODS.add("POST");
        INCLUDE_METHODS.add("PUT");
        /**
         * 这里添加需要小写
         */
        SENSITIVE_WORDS.add("password");
        SENSITIVE_WORDS.add("secret");
    }

    @PostConstruct
    public void init() {
        if (StringUtils.isNotBlank(sensitiveWords)) {
            String[] words = sensitiveWords.split(",");
            LOGGER.info("sensitiveWords:{}", sensitiveWords);
            for (String word : words) {
                SENSITIVE_WORDS.add(word.trim().toLowerCase());
            }
        }
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        try {
            printLog(request, handler);
        } catch (Exception e) {
            LOGGER.error("printLog ex", e);
        }

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }


    private Map<String, String> printLog(HttpServletRequest request, Object handler) throws IOException {
        String methodName = "";
        if (handler instanceof HandlerMethod) {
            Method method = ((HandlerMethod) handler).getMethod();
            methodName = method.getDeclaringClass().getSimpleName() + "." + method.getName();
        }
        String method = request.getMethod();
        LOGGER.info("request : {} {} [{}]", method, request.getRequestURI(), methodName);
        LOGGER.info("params : {}", getParameters(request));
        String contentType = request.getContentType();
        Map<String, String> jsonMap = new HashMap<>();
        if (StringUtils.isNotBlank(contentType) && contentType.contains(CONTENT_TYPE_JSON)) {
            if (INCLUDE_METHODS.contains(method.toUpperCase())) {
                String jsonBody = StreamUtils.copyToString(request.getInputStream(), Charset.forName("utf-8"));
                jsonMap.put("jsonRaw", jsonBody);
                if (StringUtils.isBlank(jsonBody)) {
                    return null;
                }
                try {
                    JSONObject jsonObject = JSON.parseObject(jsonBody);
                    handleJsonObject(jsonObject);
                    LOGGER.info("params json : \n{}", jsonObject.toString());
                    jsonMap.put("json", jsonObject.toJSONString());
                    return jsonMap;
                } catch (Exception e) {
                    LOGGER.warn(e.getMessage() + " will try jsonArray");
                    JSONArray jsonArray = JSON.parseArray(jsonBody);
                    JSONArray result = new JSONArray();
                    LOGGER.info("params json : \n{}", jsonArray.toString());
                    if (jsonArray.size() > 0) {
                        try {
                            jsonArray.getJSONObject(0);
                        } catch (Exception ex) {
                            jsonMap.put("json", jsonArray.toJSONString());
                            return jsonMap;
                        }
                    }
                    for (int i = 0; i < jsonArray.size(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        handleJsonObject(jsonObject);
                        result.add(jsonObject);
                    }
                    jsonMap.put("json", result.toJSONString());
                    return jsonMap;
                }
            }
        }
        return jsonMap;
    }

    private void handleJsonObject(JSONObject jsonObject) {
        if (jsonObject == null) {
            return;
        }
        Set<Map.Entry<String, Object>> entries = jsonObject.entrySet();
        for (Map.Entry<String, Object> entry : entries) {
            if (SENSITIVE_WORDS.contains(entry.getKey().toLowerCase())) {
                jsonObject.put(entry.getKey(), MASK_VALUE);
            }
        }
    }


    private static String getParameters(HttpServletRequest request) {
        return getParameterAndValue(request).get("log");
    }

    private static final String PARAM_DELIMITER = ";";

    private static Map<String, String> getParameterAndValue(HttpServletRequest request) {
        Enumeration<String> parameterNames = request.getParameterNames();
        Map<String, String> result = new HashMap<>();
        StringBuffer sb = new StringBuffer();
        StringBuffer keys = new StringBuffer();
        StringBuffer values = new StringBuffer();
        Map<String, String> paramMap = new HashMap<>();
        while (parameterNames.hasMoreElements()) {
            String key = parameterNames.nextElement();
            String value = request.getParameter(key);
            if (SENSITIVE_WORDS.contains(key.toLowerCase())) {
                sb.append(key + "->" + MASK_VALUE);
                values.append(MASK_VALUE + PARAM_DELIMITER);
                paramMap.put(key, MASK_VALUE);
            } else {
                sb.append(key + "->" + value + " ");
                values.append(value + PARAM_DELIMITER);
                paramMap.put(key, value);
            }
            keys.append(key + PARAM_DELIMITER);
        }
        result.put("log", sb.toString());
        String paramKey = "";
        String paramValue = "";
        if (StringUtils.isNotBlank(keys)) {
            paramKey = keys.substring(0, keys.lastIndexOf(PARAM_DELIMITER));
        }
        if (StringUtils.isNotBlank(values)) {
            paramValue = values.substring(0, values.lastIndexOf(PARAM_DELIMITER));
        }

        result.put("paramKey", paramKey);
        result.put("paramValue", paramValue);
        result.put("paramJson", JSON.toJSONString(paramMap));
        return result;
    }

}
