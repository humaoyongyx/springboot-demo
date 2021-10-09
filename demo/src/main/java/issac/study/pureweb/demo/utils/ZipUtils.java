package issac.study.pureweb.demo.utils;

import java.io.*;
import java.util.Arrays;
import java.util.List;
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
     * 支持多个目录或者文件压缩
     *
     * @param sourceDirs
     * @param zipFile
     * @throws Exception
     */
    public static void doZip(List<String> sourceDirs, File zipFile) throws Exception {
        OutputStream os = new FileOutputStream(zipFile);
        BufferedOutputStream bos = new BufferedOutputStream(os);
        doZip(sourceDirs, bos);
    }

    /**
     * 这种方式，下载速度快，但是迅雷下载有问题
     *
     * @param sourceDir
     * @param outputStream
     * @throws Exception
     */
    public static void doZip(String sourceDir, OutputStream outputStream) throws Exception {
        BufferedOutputStream bos = new BufferedOutputStream(outputStream);
        ZipOutputStream zos = new ZipOutputStream(bos);
        File file = new File(sourceDir);
        String basePath = null;
        if (file.isDirectory()) {
            basePath = file.getPath();
        } else {
            basePath = file.getParent();
        }
        zipFile(file, basePath, zos);
        zos.closeEntry();
        zos.close();
    }

    /**
     * 这种方式，下载速度快，但是迅雷下载有问题
     *
     * @param sourceDirs
     * @param outputStream
     * @throws Exception
     */
    public static void doZip(List<String> sourceDirs, OutputStream outputStream) throws Exception {
        BufferedOutputStream bos = new BufferedOutputStream(outputStream);
        ZipOutputStream zos = new ZipOutputStream(bos);
        zipFiles(sourceDirs, zos);
    }


    /**
     * 效率很低，但是迅雷和浏览器都可以下载，可以结合SpringResponseUtils使用
     *
     * @param sourceDir
     * @return
     * @throws Exception
     */
    public static byte[] doZip(String sourceDir) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ZipOutputStream zos = new ZipOutputStream(baos);
        File file = new File(sourceDir);
        String basePath = null;
        if (file.isDirectory()) {
            basePath = file.getPath();
        } else {
            basePath = file.getParent();
        }
        zipFile(file, basePath, zos);
        zos.closeEntry();
        zos.close();
        return baos.toByteArray();
    }

    /**
     * 支持多个目录或者文件压缩
     *
     * @param sourceDirs
     * @return
     * @throws Exception
     */
    public static byte[] doZip(List<String> sourceDirs) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ZipOutputStream zos = new ZipOutputStream(baos);
        zipFiles(sourceDirs, zos);
        return baos.toByteArray();
    }

    private static void zipFiles(List<String> sourceDirs, ZipOutputStream zos) throws Exception {
        for (String sourceDir : sourceDirs) {
            File file = new File(sourceDir);
            if (!file.exists()) {
                continue;
            }
            String basePath = null;
            if (file.isDirectory()) {
                basePath = file.getPath();
            } else {
                basePath = file.getParent();
            }
            zipFile(file, basePath, zos);
        }
        zos.closeEntry();
        zos.close();
    }

    private static void zipFile(File source, String basePath, ZipOutputStream zos) throws Exception {
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
                zipFile(file, basePath, zos);
            } else {
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
//        ZipUtils.doZip(Arrays.asList("D:\\test\\test.txt", "D:\\test\\test\\1.txt","D:\\release"), new File("D:\\tmp\\test.zip"));
//    }

}