package issac.study.mbp.core.utils;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * spring输出文件流工具类
 *
 * @author issac.hu
 */
public class SpringResponseUtils {
    /**
     * 输出文件到浏览器
     *
     * @param file 文件
     * @return
     */
    public static ResponseEntity<InputStreamResource> writeFileToResponse(File file) throws FileNotFoundException {
        HttpHeaders headers = new HttpHeaders();
        //设置客户端不要缓存
        headers.add(HttpHeaders.CACHE_CONTROL, "no-cache, no-store, must-revalidate");
        headers.add(HttpHeaders.PRAGMA, "no-cache");
        headers.add(HttpHeaders.EXPIRES, "0");
        String encodeFileName = null; //
        try {
            encodeFileName = URLEncoder.encode(file.getName(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            //ignore
        }
        //https://tools.ietf.org/html/rfc6266 协议规范
        //兼容 RFC 5987
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + encodeFileName + ";filename*=utf-8''" + encodeFileName);
        //headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename*=utf-8''" + encodeFileName);
        InputStreamResource inputStreamResource = new InputStreamResource(new FileInputStream(file));
        return ResponseEntity.ok().headers(headers).contentLength(file.length()).contentType(MediaType.parseMediaType("application/octet-stream")).body(inputStreamResource);
    }
}
