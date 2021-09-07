package issac.study.mbp.core.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 此工具，压缩文件输出目录最好不要和被压缩文件在同一个目录，因为多线程下可能会出现问题
 */
public class ZipUtils {
    /**
     * 压缩文件
     *
     * @param sourceDir 原文件目录
     * @param zipFile   压缩后的文件名称
     * @throws Exception
     */
    public static void doZip(String sourceDir, File zipFile) throws Exception {
        OutputStream os = new FileOutputStream(zipFile);
        BufferedOutputStream bos = new BufferedOutputStream(os);
        ZipOutputStream zos = new ZipOutputStream(bos);
        File file = new File(sourceDir);
        String basePath = null;
        if (file.isDirectory()) {
            basePath = file.getPath();
        } else {
            basePath = file.getParent();
        }
        zipFile(file, basePath, zos, zipFile);
        zos.closeEntry();
        zos.close();
    }

    /**
     * 压缩文件
     *
     * @param source
     * @param basePath
     * @param zos
     * @param zipFile
     * @throws Exception
     */
    private static void zipFile(File source, String basePath, ZipOutputStream zos, File zipFile) throws Exception {
        File[] files = new File[0];
        if (source.isDirectory()) {
            files = source.listFiles();
        } else {
            files = new File[1];
            files[0] = source;
        }
        String pathName;
        byte[] buf = new byte[1024];
        int length = 0;
        for (File file : files) {
            if (file.isDirectory()) {
                pathName = file.getPath().substring(basePath.length() + 1) + "/";
                zos.putNextEntry(new ZipEntry(pathName));
                zipFile(file, basePath, zos, zipFile);
            } else {
                if (file.equals(zipFile)) {
                    continue;
                }
                pathName = file.getPath().substring(basePath.length() + 1);
                InputStream is = new FileInputStream(file);
                BufferedInputStream bis = new BufferedInputStream(is);
                zos.putNextEntry(new ZipEntry(pathName));
                while ((length = bis.read(buf)) > 0) {
                    zos.write(buf, 0, length);
                }
                is.close();
            }
        }
    }

//    public static void main(String[] args) throws Exception {
//        ZipUtils.doZip("D:\\release", new File("D:\\release\\test.zip"));
//    }

}