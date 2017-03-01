package com.constantsoft.invoice.pdf;


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
    }
}