package issac.study.pureweb.demo.utils;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.graphics.xobject.PDXObjectImage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;

/**
 * 识别pdf中的二维码
 * <p>
 * 第一步，引入maven包
 * <dependency>
 *     <groupId>com.google.zxing</groupId>
 *     <artifactId>javase</artifactId>
 *     <version>3.5.0</version>
 * </dependency>
 *
 * <dependency>
 *    <groupId>com.google.zxing</groupId>
 *    <artifactId>core</artifactId>
 *    <version>3.5.0</version>
 * </dependency>
 *
 * <dependency>
 *    <groupId>org.apache.pdfbox</groupId>
 *    <artifactId>pdfbox</artifactId>
 *    <version>1.8.13</version>
 * </dependency>
 * <p>
 * 第二步：
 * 调用 parseQrCodeFromPdf(String path)
 */
public class QRCodeUtils {


    /**
     * 从pdf中解析二维码
     *
     * @param pdfFilePath pdf文件的路径
     * @return 如果pdf中只有一个二维码，那么列表的长度为1，否则列表会解析出多个
     */
    public static List<String> parseQrCodeFromPdf(String pdfFilePath) {
        List<String> result = new ArrayList<>();
        try {
            PDDocument document = PDDocument.load(new File(pdfFilePath));
            List<PDPage> pages = document.getDocumentCatalog().getAllPages();
            Iterator<PDPage> pageIterator = pages.iterator();
            while (pageIterator.hasNext()) {
                PDPage page = pageIterator.next();
                PDResources resources = page.getResources();
                Map<String, PDXObjectImage> images = resources.getImages();
                if (images != null) {
                    Iterator<String> imageIter = images.keySet().iterator();
                    while (imageIter.hasNext()) {
                        String key = imageIter.next();
                        PDXObjectImage image = images.get(key);
                        File file = new File("tmp.png");
                        if (file.exists()) {
                            file.delete();
                        }
                        image.write2file(file);
                        String content = parseQrCodeFromImage(file);
                        if (content != null) {
                            result.add(content);
                        }
                    }
                }
            }
        } catch (IOException ex) {
            // ex.printStackTrace();
        }
        return result;
    }

    /**
     * 解析图片中的二维码
     *
     * @param imageFile 图片文件
     * @return
     */
    private static String parseQrCodeFromImage(File imageFile) {
        String content = null;
        BufferedImage image;
        try {
            image = ImageIO.read(imageFile);
            LuminanceSource source = new BufferedImageLuminanceSource(image);
            Binarizer binarizer = new HybridBinarizer(source);
            BinaryBitmap binaryBitmap = new BinaryBitmap(binarizer);
            Map<DecodeHintType, Object> hints = new HashMap<>();
            hints.put(DecodeHintType.CHARACTER_SET, "UTF-8");
            //解码
            Result result = new MultiFormatReader().decode(binaryBitmap, hints);
            content = result.getText();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NotFoundException e) {
            //这里判断如果识别不了带LOGO的图片，重新添加上一个属性
            try {
                image = ImageIO.read(imageFile);
                LuminanceSource source = new BufferedImageLuminanceSource(image);
                Binarizer binarizer = new HybridBinarizer(source);
                BinaryBitmap binaryBitmap = new BinaryBitmap(binarizer);
                Map<DecodeHintType, Object> hints = new HashMap<>();
                //设置编码格式
                hints.put(DecodeHintType.CHARACTER_SET, "UTF-8");
                //设置优化精度
                hints.put(DecodeHintType.TRY_HARDER, Boolean.TRUE);
                //设置复杂模式开启（我使用这种方式就可以识别微信的二维码了）
                hints.put(DecodeHintType.PURE_BARCODE, Boolean.TYPE);
                Result result = new MultiFormatReader().decode(binaryBitmap, hints);//解码
                content = result.getText();
            } catch (IOException e2) {
                // e2.printStackTrace();
            } catch (NotFoundException e2) {
                // e2.printStackTrace();
            }
        }
        return content;
    }


    public static void main(String[] args) {
        List<String> qrCodeFromPdfContent = parseQrCodeFromPdf("D:\\test\\pdf\\A.pdf");
        System.out.println(qrCodeFromPdfContent);

    }

}
