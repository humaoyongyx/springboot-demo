package issac.study.mbp.core.controller;

import issac.study.mbp.core.response.ResponseVo;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

/**
 * 错误处理类
 *
 * @author issac.hu
 */
@RestController
public class BaseErrorController implements ErrorController {

    @RequestMapping("/error")
    public ResponseVo handleError(HttpServletRequest request) {
        Integer statusCode = (Integer) request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        if (statusCode == null) {
            return ResponseVo.fail(HttpStatus.NOT_FOUND.getReasonPhrase());
        } else {
            HttpStatus resolve = HttpStatus.resolve(statusCode);
            return ResponseVo.fail(resolve.value(), resolve.getReasonPhrase());
        }
    }

    @Override
    public String getErrorPath() {
        return "/error";
    }
}
