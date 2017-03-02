package com.constantsoft.invoice.service.impl;

import com.constantsoft.invoice.service.bean.InvoiceInformationEntity;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Walter on 2017/2/19.
 */
public class PDFInvoiceAnalysisServiceTest {

    @Test
    public void testaaa() throws Exception {
        PDFInvoiceAnalysisService service = new PDFInvoiceAnalysisService();
        String filePath = "C:\\Users\\Walter\\Desktop\\InvoiceExampleHtml\\exampleFile\\17.pdf";
        InvoiceInformationEntity entity = service.generate(new File(filePath), true);
        print(entity);
    }

    private void print(InvoiceInformationEntity entity) {
        if (entity == null) System.out.println("null");
        else {
            System.out.println(entity.getInvoiceCode() + "|" + entity.getInvoiceNumber() + "|" +
                    entity.getAmount() + "|" + entity.getInvoiceDate() + "|" + entity.getMachineCode() + "|" + entity.getCheckingCode());
        }
    }

    @Test
    public void testAll() {
        PDFInvoiceAnalysisService service = new PDFInvoiceAnalysisService();
        String path = "C:\\Users\\walter.xu\\Desktop\\exampleFile";
//        String path = "C:\\Users\\Walter\\Desktop\\InvoiceExampleHtml\\exampleFile";
        File directory = new File(path);
        File[] subFile = directory.listFiles();
        int totalCount = 0;
        int successCount = 0;
        int exceptionCount = 0;
        int failedCount = 0;
        List<String> printList = new ArrayList<String>();
        for (File target : subFile) {
            if (target.isFile() && target.getName().toLowerCase().endsWith("pdf")) {
                try {
                    InvoiceInformationEntity entity = service.generate(target, true);
                    if (entity != null && !"-".equals(entity.getInvoiceCode()) && !"".equals(entity.getInvoiceNumber())) {
                        printList.add("[SUCCESS]" + target.getPath() + ":     " + entity.getInvoiceCode() + "|" + entity.getInvoiceNumber()
                        +"|"+entity.getCheckingCode()+"|"+entity.getInvoiceDate()+"|"+entity.getCompanyName());
                        successCount++;
                    } else {
                        printList.add("[FAILED]" + target.getPath() + ":     " + entity.getInvoiceCode() + "|" + entity.getInvoiceNumber());
                        failedCount++;
                    }
                } catch (Exception e) {
                    exceptionCount++;
                    printList.add("[EXCEPTION]" + target.getPath() + ":     " + e.getMessage());
                }
                totalCount++;
            }
        }
        for(String str: printList)
            System.out.println(str);
        System.out.println(totalCount+"["+successCount+","+failedCount+","+exceptionCount+"]");
    }
}