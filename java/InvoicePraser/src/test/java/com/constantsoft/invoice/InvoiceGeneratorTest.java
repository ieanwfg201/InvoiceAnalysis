package com.constantsoft.invoice;

import java.util.Arrays;

/**
 * Created by walter.xu on 2016/12/13.
 */
public class InvoiceGeneratorTest {

    public static void main(String[] args) throws Exception{
        String path = "D:\\log\\pdf\\031001600211_46627938(1)-KenDeJi.pdf";
        System.out.println(Arrays.toString(InvoiceGenerator.instance().generateInvoiceCodeAndNumberValidateByPdfAndQrcode(path)));
        path = "D:\\log\\pdf\\JingDongDianZiFaPiao-20150807.pdf";
        System.out.println(Arrays.toString(InvoiceGenerator.instance().generateInvoiceCodeAndNumberValidateByPdfAndQrcode(path)));
        path = "D:\\log\\pdf\\LianTong.pdf";
        System.out.println(Arrays.toString(InvoiceGenerator.instance().generateInvoiceCodeAndNumberValidateByPdfAndQrcode(path)));

        path = "D:\\log\\pdf\\test.jpg";
        System.out.println(Arrays.toString(InvoiceGenerator.instance().generateInvoiceCodeAndNumberValidateByPdfAndQrcode(path)));
    }


}