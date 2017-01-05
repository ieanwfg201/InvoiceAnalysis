package com.constantsoft.invoice.praser.util;


/**
 * Created by walter.xu on 2016/12/19.
 */
public class PDFImageUtilsTest {

    public static void main(String[] args) throws Exception {
        PDFImageUtils.transPdfToImageOfDirectory("D:\\log\\pdf\\source\\bak", "D:\\log\\pdf\\source\\tess", 150);
        PDFImageUtils.transPdfToImageOfDirectory("D:\\log\\pdf\\source\\bak", "D:\\log\\pdf\\source\\tess", 200);
        PDFImageUtils.transPdfToImageOfDirectory("D:\\log\\pdf\\source\\bak", "D:\\log\\pdf\\source\\tess", 250);
        PDFImageUtils.transPdfToImageOfDirectory("D:\\log\\pdf\\source\\bak", "D:\\log\\pdf\\source\\tess", 300);

    }

}