package com.daisytech.invoice.pdf;


/**
 * Created by walter.xu on 2017/3/1.
 */
public class PDFInvoiceInforGenerateFactoryTest {

    public static void main(String[] args){
        PDFInvoiceInforGenerateFactoryTest test = new PDFInvoiceInforGenerateFactoryTest();

        test.testMethod();
    }

    public void testMethod(){
        PDFInvoiceInforGenerateFactory factory = new PDFInvoiceInforGenerateFactory();
        String item = "开户行及账号 ： 区 996<>>-<3*5<>355-043>327+*2";
        System.out.println(factory.findStrByAvailableWords(item, 0,0,"1234567890 "));
        factory.executeLine(item);
        System.out.println(factory);

        item = "         北京增值税统一电发票子监制 普通发票 发票号码:08750201";
        System.out.println(factory.findStrByAvailableWords(item, 0, 0, "0123456789"));
        item = "         北京增值税统一电发票子监制 普通发票 发票号码:08750201过";
        System.out.println(factory.findStrByAvailableWords(item, 0, 0, "0123456789"));
        item = "         北京增值税统一电发票子监制 普通发票 发票号码:08750201国家";
        System.out.println(factory.findStrByAvailableWords(item, 0, 0, "0123456789"));
        item = "         北京增值税统一电发票子监制 普通发票 发票号码:08750201ab";
        System.out.println(factory.findStrByAvailableWords(item, 0, 0, "0123456789"));
        item = "         北京增值税统一电发票子监制 普通发票 发票号码:08750201a123";
        System.out.println(factory.findStrByAvailableWords(item, 0, 0, "0123456789"));
    }
}