package issac.study.mbp.core.utils;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
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
        long contentLength = file.length();
        String fileName = file.getName();
        return writeFileToResponse(new FileInputStream(file), fileName, contentLength);
    }

    /**
     * 输出文件到浏览器
     *
     * @param inputStream 输入流
     * @param fileName    输出文件名称
     * @return
     * @throws IOException
     */
    public static ResponseEntity<InputStreamResource> writeFileToResponse(InputStream inputStream, String fileName) throws IOException {
        long contentLength = inputStream.available();
        return writeFileToResponse(inputStream, fileName, contentLength);
    }

    private static ResponseEntity<InputStreamResource> writeFileToResponse(InputStream inputStream, String fileName, long contentLength) {
        InputStreamResource inputStreamResource = new InputStreamResource(inputStream);
        HttpHeaders headers = new HttpHeaders();
        //设置客户端不要缓存
        headers.add(HttpHeaders.CACHE_CONTROL, "no-cache, no-store, must-revalidate");
        headers.add(HttpHeaders.PRAGMA, "no-cache");
        headers.add(HttpHeaders.EXPIRES, "0");
        String encodeFileName = null;
        try {

            encodeFileName = URLEncoder.encode(fileName, "UTF-8");// 编码解决乱码问题
        } catch (UnsupportedEncodingException e) {
            //ignore
        }
        //https://tools.ietf.org/html/rfc6266 协议规范
        //headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename*=utf-8''" + encodeFileName);
        //兼容 RFC 5987
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + encodeFileName + ";filename*=utf-8''" + encodeFileName);
        return ResponseEntity.ok().headers(headers).contentLength(contentLength).contentType(MediaType.parseMediaType("application/octet-stream")).body(inputStreamResource);
    }

    public static void setResponse(HttpServletResponse response, String fileName) {
        response.setHeader(HttpHeaders.CACHE_CONTROL, "no-cache, no-store, must-revalidate");
        response.setHeader(HttpHeaders.PRAGMA, "no-cache");
        response.setHeader(HttpHeaders.EXPIRES, "0");
        response.setContentType("application/octet-stream");
        String encodeFileName = null;
        try {
            encodeFileName = URLEncoder.encode(fileName, "UTF-8");// 编码解决乱码问题
        } catch (UnsupportedEncodingException e) {
            //ignore
        }
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + encodeFileName + ";filename*=utf-8''" + encodeFileName);
    }
}
