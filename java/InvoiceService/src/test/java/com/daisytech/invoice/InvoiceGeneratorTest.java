package com.daisytech.invoice;

import com.daisytech.invoice.generator.InvoiceGenerator;

import java.util.Arrays;

/**
 * Created by walter.xu on 2016/12/13.
 */
public class InvoiceGeneratorTest {

    public static void main(String[] args) throws Exception{
//        String path = "D:\\log\\pdf\\031001600211_46627938(1)-KenDeJi.pdf";
//        System.out.println(Arrays.toString(InvoiceGenerator.instance().generateInvoiceCodeAndNumberValidateByPdfAndQrcode(path)));
//        path = "D:\\log\\pdf\\JingDongDianZiFaPiao-20150807.pdf";
//        System.out.println(Arrays.toString(InvoiceGenerator.instance().generateInvoiceCodeAndNumberValidateByPdfAndQrcode(path)));
//        path = "D:\\log\\pdf\\LianTong.pdf";
//        System.out.println(Arrays.toString(InvoiceGenerator.instance().generateInvoiceCodeAndNumberValidateByPdfAndQrcode(path)));
//
//        path = "D:\\log\\pdf\\test.jpg";
//        System.out.println(Arrays.toString(InvoiceGenerator.instance().generateInvoiceCodeAndNumberValidateByPdfAndQrcode(path)));

        testGenerateInvoidCodeAndNumber(null);
    }


    public static void testGenerateInvoidCodeAndNumber(String path) throws Exception {
        System.out.println("###");
        path = "D:\\log\\pdf\\10.pdf.300.jpg";
        System.out.println("QR: "+Arrays.toString(InvoiceGenerator.instance(null).generateInvoiceCodeAndNumber(path, false, true, false, 0.65, 0, 1, 0.25,false)));
        System.out.println("OCR: "+Arrays.toString(InvoiceGenerator.instance(null).generateInvoiceCodeAndNumber(path, false, false, true, 0.65, 0, 1, 0.25,false)));
        path = "D:\\log\\pdf\\031001600211_46627938(1)-KenDeJi.pdf";
        System.out.println("PDF: "+Arrays.toString(InvoiceGenerator.instance(null).generateInvoiceCodeAndNumber(path, true, false, false, 0.65, 0, 1, 0.25,false)));
        System.out.println("QR: "+Arrays.toString(InvoiceGenerator.instance(null).generateInvoiceCodeAndNumber(path, false, true, false, 0.65, 0, 1, 0.25,false)));
        System.out.println("OCR: "+Arrays.toString(InvoiceGenerator.instance(null).generateInvoiceCodeAndNumber(path, false, false, true, 0.65, 0, 1, 0.25,false)));
        path = "D:\\log\\pdf\\20161228180310-0001.pdf";
        System.out.println("OCR: "+Arrays.toString(InvoiceGenerator.instance(null).generateInvoiceCodeAndNumber(path, false, false, true, 0.65, 0, 1, 0.25,false)));

    }
}