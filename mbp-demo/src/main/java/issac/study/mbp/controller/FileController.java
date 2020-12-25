package issac.study.mbp.controller;

import cn.hutool.core.io.FileUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import issac.study.mbp.core.annotation.PrintLog;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Enumeration;

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
        System.out.println(param);
        if (files != null) {
            for (MultipartFile file : files) {
                log.info("name:{},originalName:{}", file.getName(), file.getOriginalFilename());
            }
        }
    }


    @ApiOperation("下载")
    @PrintLog(showArgs = false)
    @GetMapping(value = "/download", produces = {MediaType.APPLICATION_OCTET_STREAM_VALUE})
    public ResponseEntity<InputStreamResource> download(String name) throws UnsupportedEncodingException {
        File file = new File("/Users/issac/Downloads/ZB416A技术规格表(2019版)_最新.doc");
        BufferedInputStream inputStream = FileUtil.getInputStream(file);
        HttpHeaders headers = new HttpHeaders();
        //设置文件下载
        headers.add(HttpHeaders.CONTENT_TYPE, "application/octet-stream");
        //设置客户端不要缓存
        headers.add(HttpHeaders.CACHE_CONTROL, "no-cache, no-store, must-revalidate");
        headers.add(HttpHeaders.PRAGMA, "no-cache");
        headers.add(HttpHeaders.EXPIRES, "0");
        String encodeFileName = URLEncoder.encode(file.getName(), "UTF-8"); //
        //https://tools.ietf.org/html/rfc6266 协议规范
        //兼容 RFC 5987
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + encodeFileName + ";filename*=utf-8''" + encodeFileName);
        //headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename*=utf-8''" + encodeFileName);
        InputStreamResource inputStreamResource = new InputStreamResource(inputStream);
        return ResponseEntity.ok().headers(headers).body(inputStreamResource);
    }

}
