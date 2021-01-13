package issac.study.mbp.controller;

import issac.study.mbp.core.locale.MessageUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

/**
 * @author issac.hu
 */
@RestController
@RequestMapping("/locale")
@Validated
public class LocaleController {

    @GetMapping("/")
    public String test(@NotBlank(message = "{validator.notBlank}") String code, @Min(2) Integer num) {
        return MessageUtils.get("hello");
    }
}
