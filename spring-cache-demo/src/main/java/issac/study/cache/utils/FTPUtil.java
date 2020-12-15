package issac.study.cache.utils;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.extra.ftp.Ftp;
import cn.hutool.extra.ftp.FtpMode;
import issac.study.cache.config.FtpConfig;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class FTPUtil {

    private static FtpConfig ftpConfig = new FtpConfig();

    public static void main(String[] args) throws FileNotFoundException {

        FtpMode ftpMode = FtpMode.Active;
        if (!ftpConfig.getActiveMode()) {
            ftpMode = FtpMode.Passive;
        }
        Ftp ftp = new Ftp(ftpConfig.getHost(), ftpConfig.getPort(), ftpConfig.getUsername(), ftpConfig.getPassword(), CharsetUtil.CHARSET_UTF_8, ftpMode);
        File outFile = new File("ftp.txt");
        ftp.download("/test", "test.txt", outFile);
        FileInputStream fileInputStream = new FileInputStream(outFile);
        BufferedInputStream in = new BufferedInputStream(new FileInputStream(outFile));
        System.out.println(outFile.getName());
        outFile.delete();


    }


}
