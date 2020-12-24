package issac.study.mbp.controller;

import issac.study.mbp.core.annotation.PrintLog;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author issac.hu
 */
@Slf4j
@RestController
@RequestMapping("/api/file")
public class FileController {

    @PrintLog
    @PostMapping("/upload")
    public void upload(@RequestParam("files") MultipartFile[] files) {
        if (files != null) {
            for (MultipartFile file : files) {
                log.info("name:{},originalName:{}", file.getName(), file.getOriginalFilename());
            }
        }
    }


}
