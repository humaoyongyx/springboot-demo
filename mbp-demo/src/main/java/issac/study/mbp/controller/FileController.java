package issac.study.mbp.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import issac.study.mbp.core.annotation.PrintLog;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Enumeration;

import static issac.study.mbp.core.utils.SpringResponseUtils.writeFileToResponse;

/**
 * @author issac.hu
 */
@Api(tags = "文件")
@Slf4j
@RestController
@RequestMapping("/api/file")
public class FileController {

    @PrintLog(showArgs = false)
    @PostMapping("/upload")
    public void upload(@RequestParam(value = "files", required = false) MultipartFile[] files, String param, HttpServletRequest request) {

        Enumeration<String> parameterNames = request.getParameterNames();
        while (parameterNames.hasMoreElements()) {
            String key = parameterNames.nextElement();
            System.out.println(key + "->" + request.getParameter(key));
        }
        System.out.println("param:" + param);
        if (files != null) {
            for (MultipartFile file : files) {
                log.info("name:{},originalName:{}", file.getName(), file.getOriginalFilename());
            }
        }
    }


    @ApiOperation("下载")
    @PrintLog(showArgs = false)
    @GetMapping(value = "/download", produces = {MediaType.APPLICATION_OCTET_STREAM_VALUE})
    public ResponseEntity<InputStreamResource> download(String name) throws FileNotFoundException {
        File file = new File("/Users/issac/Downloads/ZB416A技术规格表(2019版)_最新.doc");
        return writeFileToResponse(file);
    }


}
