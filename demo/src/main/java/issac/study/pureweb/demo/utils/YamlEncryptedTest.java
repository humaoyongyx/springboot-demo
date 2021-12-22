package issac.study.pureweb.demo.utils;

import org.jasypt.util.text.BasicTextEncryptor;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author issac.hu
 * @description
 * @date 2021/12/21 13:51
 */
public class YamlEncryptedTest {

    public static final String default_psd = "123456";

    public static void main(String[] args) throws FileNotFoundException {
        String pathname = "D:\\workcode\\src\\main\\resources\\application-xx.yml";
        String password = System.getenv().get("password");
        if (password == null) {
            password = default_psd;
        }
        System.out.println(password);
        printEncrypted(password, pathname);
        //  printEncrypted(password, "11111", pathname);
    }

    public static void printEncrypted(String password, String pathname) throws FileNotFoundException {
        Map<String, String> result = getMapFromYamlFile(pathname);
        for (Map.Entry<String, String> entry : result.entrySet()) {
            if (entry.getKey().contains("password") || entry.getKey().contains("secret")) {
                StringBuffer sb = new StringBuffer();
                String value = entry.getValue();
                sb.append("[初始配置]" + entry.getKey() + " : " + value);
                if (value.trim().startsWith("ENC")) {
                    String decrypt = decrypt(password, value.replace("ENC(", "").replace(")", ""));
                    sb.append(" ;解密之后: " + decrypt);
                } else {
                    String encrypt = encrypt(password, value);
                    sb.append(" ;加密之后: ENC(" + encrypt + ")");
                }
                System.out.println(sb);
            }
        }
    }

    public static void printEncrypted(String oldPassword, String newPassword, String pathname) throws FileNotFoundException {
        Map<String, String> result = getMapFromYamlFile(pathname);
        for (Map.Entry<String, String> entry : result.entrySet()) {
            if (entry.getKey().contains("password")) {
                StringBuffer sb = new StringBuffer();
                String value = entry.getValue();
                sb.append("[初始配置]" + entry.getKey() + " : " + value);
                if (value.trim().startsWith("ENC")) {
                    String decrypt = decrypt(oldPassword, value.replace("ENC(", "").replace(")", ""));
                    sb.append(" ;解密之后: " + decrypt);
                    String encrypt = encrypt(newPassword, value);
                    sb.append(" ;重新加密: ENC(" + encrypt + ")");
                } else {
                    String encrypt = encrypt(newPassword, value);
                    sb.append(" ;加密之后: ENC(" + encrypt + ")");
                }
                System.out.println(sb);
            }
        }
    }

    private static Map<String, String> getMapFromYamlFile(String pathname) throws FileNotFoundException {
        //初始化Yaml解析器
        Yaml yaml = new Yaml();
        File f = new File(pathname);
        //读入文件
        Map<String, Object> rawMap = yaml.load(new FileInputStream(f));
        Map<String, String> result = new HashMap<>();
        getPropVal(rawMap, new StringBuffer(), result);
        return result;
    }

    private static void getPropVal(Map<String, Object> rawMap, StringBuffer parentKey, Map<String, String> result) {
        for (Map.Entry<String, Object> entry : rawMap.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            StringBuffer newPk;
            if (parentKey.length() != 0) {
                newPk = new StringBuffer().append(parentKey).append(".").append(key);
            } else {
                newPk = new StringBuffer(key);
            }
            if (value instanceof Map) {
                getPropVal((Map<String, Object>) value, newPk, result);
            } else {
                //System.out.println(newPk + ":" + value);
                result.put(newPk.toString(), String.valueOf(value));
            }
        }
    }

    public static String encrypt(String password, String message) {
        // 加密
        BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
        //使用的是加密算法是:PBEWithMD5AndDES,注意maven不同版本算法可能不同
        textEncryptor.setPassword(password);
        String encrypt = textEncryptor.encrypt(message);
        return encrypt;
    }

    public static String decrypt(String password, String message) {
        BasicTextEncryptor textEncryptor2 = new BasicTextEncryptor();
        textEncryptor2.setPassword(password);
        String decrypt = textEncryptor2.decrypt(message);
        return decrypt;
    }
}
