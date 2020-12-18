package issac.study.mbp.core.advice;

import issac.study.mbp.core.annotation.RespVo;
import issac.study.mbp.core.response.ResponseVo;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.lang.reflect.AnnotatedElement;

/**
 * @author issac
 */
@ControllerAdvice
public class GlobalResponseHandler implements ResponseBodyAdvice<Object> {

    /**
     * Description: 判断是否小视返回对象<br>
     *
     * @param returnType    返回类型
     * @param converterType 转换器
     * @return <br>
     */
    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        AnnotatedElement annotatedElement = returnType.getAnnotatedElement();
        RespVo respVo = annotatedElement.getAnnotation(RespVo.class);
        return respVo != null;
    }

    /**
     * Description: 重写响应体<br>
     *
     * @param body                  消息体
     * @param returnType            返回类型
     * @param selectedContentType   选择内容类型
     * @param selectedConverterType 选择转换类型
     * @param request               request
     * @param response              响应
     * @return <br>
     */
    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request,
                                  ServerHttpResponse response) {

        return ResponseVo.success(body);
    }
}
