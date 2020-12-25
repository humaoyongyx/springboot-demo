package issac.study.mbp.test;

import issac.study.mbp.core.http.HttpHelper;
import issac.study.mbp.core.utils.MapParam;
import okhttp3.Response;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * @author issac.hu
 */
public class SimpleTest {

    public static void main(String[] args) {

//          upload();
//          post();
        download();
    }

    private static void download() {
        Response response = HttpHelper.fileClient().download("http://localhost:8080/api/file/download");

        if (response != null) {
            Map<String, List<String>> stringListMap = response.headers().toMultimap();
            String fileNameFromDisposition = HttpHelper.getFileNameFromDisposition(stringListMap);
            System.out.println(fileNameFromDisposition);
            if (response.body() != null) {
                response.close();
            }
        }
    }

    private static void upload() {
        File file = new File("/Users/issac/Downloads/ZB416A技术规格表(2019版)_最新.doc");
        // FileUtil.appendUtf8String("hello world!", file);
        boolean result = HttpHelper.fileClient().upload("http://localhost:8080/api/file/upload", MapParam.build().put("param", "我是测试参数").toMap(), "files", file);
        System.out.println(result);
    }

    private static void post() {
        HttpHelper.client().post("http://localhost:8080/api/file/upload", MapParam.build().put("test", "你好").put("param", "我是测试参数").toMap());
    }
}
