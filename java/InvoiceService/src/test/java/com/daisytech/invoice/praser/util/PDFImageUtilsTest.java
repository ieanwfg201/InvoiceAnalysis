package com.daisytech.invoice.praser.util;


import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * Created by walter.xu on 2016/12/19.
 */
public class PDFImageUtilsTest {

    public static void main(String[] args) throws Exception {
//        testTransPdfToImageOfDirectory();
        testRatate();
    }

    public static void testTransPdfToImageOfDirectory(){
        PDFImageUtils.transPdfToImageOfDirectory("D:\\log\\pdf\\source\\bak", "D:\\log\\pdf\\source\\tess", 150);
        PDFImageUtils.transPdfToImageOfDirectory("D:\\log\\pdf\\source\\bak", "D:\\log\\pdf\\source\\tess", 200);
        PDFImageUtils.transPdfToImageOfDirectory("D:\\log\\pdf\\source\\bak", "D:\\log\\pdf\\source\\tess", 250);
        PDFImageUtils.transPdfToImageOfDirectory("D:\\log\\pdf\\source\\bak", "D:\\log\\pdf\\source\\tess", 300);
    }

    public static void testRatate() throws Exception {
        BufferedImage targetImage = ImageIO.read(new File("D:\\log\\test.jpg"));

//        targetImage = PDFImageUtils.ratate(targetImage, 90);
//        ImageIO.write(targetImage, "jpg", new File("D:\\log\\test-1.jpg"));
//        targetImage = PDFImageUtils.ratate(targetImage, 90);
//        ImageIO.write(targetImage, "jpg", new File("D:\\log\\test-2.jpg"));
//        targetImage = PDFImageUtils.ratate(targetImage, 90);
//        ImageIO.write(targetImage, "jpg", new File("D:\\log\\test-3.jpg"));
//        targetImage = PDFImageUtils.ratate(targetImage, 90);
//        ImageIO.write(targetImage, "jpg", new File("D:\\log\\test-4.jpg"));
        int index = 0;
        long start = System.currentTimeMillis();
        while(index++<100000){
            targetImage = PDFImageUtils.ratate(targetImage, 90);
        }
        System.out.println("cost:"+(System.currentTimeMillis()-start)+"ms");
    }
}