package com.constantsoft.invoice.praser;


import com.constantsoft.invoice.praser.util.PDFImageUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Arrays;

/**
 * Created by walter.xu on 2016/12/16.
 * tesseract invoice.fontyp.exp0.tif invoice.fontyp.exp0 -l chi_sim -c tessedit_char_whitelist=1234567890.：:-普通发票发票代码发票号码开票日期检验码年月日机器编号密码区购买方名称纳税人识别码地址电话开户行及账号合计货物或应税劳务服务名称规格型号单位数量单价金额税率税额税额合计（）大壹贰叁肆伍陆柒捌玖拾佰仟万亿小写销售方备注收款人符合开票人 -psm 3 batch.nochop makebox

 */
public class OCRPraserTest {

    public static void main(String[] args) throws Exception {
        System.out.println((int)'1');
        System.out.println((int)'2');
        System.out.println((int)'3');
        System.out.println((int)'4');
        System.out.println((int)'5');
        System.out.println((int)'6');
        System.out.println((int)'7');
        System.out.println((int)'8');
        System.out.println((int)'9');
        System.out.println((int)'0');

        testPraseNumberAndCodeByPartWithRatate();
    }

    private static void testPraseNumberAndCodeByPart() throws Exception{
        double xStart = 0.7;
        double xEnd = 1;
        double yStart = 0;
        double yEnd = 0.2;
//        String file = "D:\\log\\pdf\\10.pdf.300.jpg";
        String file = "D:\\log\\pdf\\10.pdf.300.jpg";
        BufferedImage image = PDFImageUtils.getBufferedImageFromFile(new File(file));
        long start = System.currentTimeMillis();
        System.out.println(Arrays.toString(OCRPraser.instance(null).praseNumberAndCodeByPart(image,xStart,yStart,xEnd,yEnd)));
        System.out.println("Cost time = "+(System.currentTimeMillis()-start)+"ms");
        start = System.currentTimeMillis();
        System.out.println(Arrays.toString(OCRPraser.instance(null).praseNumberAndCodeByPart(image, xStart, yStart, xEnd, yEnd)));
        System.out.println("Cost time = "+(System.currentTimeMillis()-start)+"ms");
        start = System.currentTimeMillis();
    }

    private static void testPrintImageText(String file) throws Exception{
        BufferedImage image = ImageIO.read(new File(file));
        long start = System.currentTimeMillis();
        Rectangle rect = new Rectangle((int)(0.7*image.getWidth()), 0, (int)(0.3*image.getWidth()), (int)(0.2*image.getHeight()));
        String text = OCRPraser.instance(null).praseText(new File(file),rect);
        System.out.println(text);
        System.out.println("Total cost: "+(System.currentTimeMillis()-start)+"ms");
    }

    public static void testPraseNumberAndCodeByPartWithRatate() throws Exception {
        String path = "D:\\log\\pdf\\20161228180508-0001.pdf";
        double xStart = 0.7;
        double xEnd = 1;
        double yStart = 0;
        double yEnd = 0.2;
        long start = System.currentTimeMillis();
        BufferedImage image = PDFImageUtils.getBufferedImageFromFile(new File(path));
        System.out.println(Arrays.toString(OCRPraser.instance("D:\\log\\ocr\\tessData").praseNumberAndCodeByPartWithRatate(image, xStart, yStart,xEnd,yEnd)));
        System.out.println("Cost time = "+(System.currentTimeMillis()-start)+"ms");

//        BufferedImage image = PDFImageUtils.getBufferedImageFromFile(new File(path));
//        long start = System.currentTimeMillis();
//        System.out.println(Arrays.toString(OCRPraser.instance("D:\\log\\ocr\\tessData").praseNumberAndCodeByPart(image,xStart,yStart,xEnd,yEnd)));
//        System.out.println("Cost time = "+(System.currentTimeMillis()-start)+"ms");
//
//        image = PDFImageUtils.ratate(image, 90);
//        start = System.currentTimeMillis();
//        System.out.println(Arrays.toString(OCRPraser.instance("D:\\log\\ocr\\tessData").praseNumberAndCodeByPart(image,xStart,yStart,xEnd,yEnd)));
//        System.out.println("Cost time = "+(System.currentTimeMillis()-start)+"ms");
//
//        image = PDFImageUtils.ratate(image, 90);
//        start = System.currentTimeMillis();
//        System.out.println(Arrays.toString(OCRPraser.instance("D:\\log\\ocr\\tessData").praseNumberAndCodeByPart(image,xStart,yStart,xEnd,yEnd)));
//        System.out.println("Cost time = "+(System.currentTimeMillis()-start)+"ms");
//
//        image = PDFImageUtils.ratate(image, 90);
//        start = System.currentTimeMillis();
//        System.out.println(Arrays.toString(OCRPraser.instance("D:\\log\\ocr\\tessData").praseNumberAndCodeByPart(image,xStart,yStart,xEnd,yEnd)));
//        System.out.println("Cost time = "+(System.currentTimeMillis()-start)+"ms");
    }
}