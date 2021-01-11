package issac.study.mbp.test;

import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * @author issac.hu
 */
public class Test {

    public static void main(String[] args) throws Exception {
        System.out.println(URLClassLoader.class.getClassLoader());
        System.out.println( Test.class.getClassLoader());
        URL resource = Test.class.getClassLoader().getResource("application.yml");
        System.out.println(resource);
        URL systemResource = ClassLoader.getSystemResource("application.yml");
        System.out.println(systemResource);
        URL resource1 = Test.class.getResource("/application.yml");
        System.out.println(resource1);
    }


    public static void test1() throws IOException {
        ClassPathResource classPathResource = new ClassPathResource("config/application.yml");
        URL url = classPathResource.getURL();
        System.out.println(url);
    }
}