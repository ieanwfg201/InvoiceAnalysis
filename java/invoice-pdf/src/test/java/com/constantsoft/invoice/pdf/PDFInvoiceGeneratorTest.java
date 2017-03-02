package com.constantsoft.invoice.pdf;

import com.constantsoft.invoice.pdf.entity.InvoiceInfosEntity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by walter.xu on 2017/2/28.
 */
public class PDFInvoiceGeneratorTest {
    PDFInvoiceGenerator generator = new PDFInvoiceGenerator();

    public static void main(String[] args) throws Exception{
        PDFInvoiceGeneratorTest test = new PDFInvoiceGeneratorTest();
//        test.testGenerate();
        test.testGenerateAll();
    }

    public void testGenerateAll() {
        String directory = "C:\\Users\\walter.xu\\Desktop\\exampleFile";
        File[] fileArray = new File(directory).listFiles();
        List<String> resultList = new ArrayList<String>();
        int failed =0, success =0;
        for(File file: fileArray){
            try {
                InvoiceInfosEntity entity = generator.generate(file, true);
                success++;
                resultList.add(file.getName()+": " + printEntity(entity));
            }catch (Exception e){
                failed++;
                resultList.add(file.getName()+": " + e.getMessage());
            }
        }
        for (String result : resultList) {
            System.out.println(result);
        }
        System.out.println("Success: "+success+", failed: "+failed);
    }

    public void testGenerate() throws Exception {
        String file = "C:\\Users\\walter.xu\\Desktop\\exampleFile\\17.pdf";
        InvoiceInfosEntity entity = generator.generate(new File(file));
        System.out.println(printEntity(entity));
        entity = generator.generate(new File(file), true);
        System.out.println(printEntity(entity));
    }

    private String printEntity(InvoiceInfosEntity entity){
        StringBuilder str = new StringBuilder();

        str.append("Entity: ");
        if (entity==null) str.append("null");
        else {
            str.append("InvoiceCode=").append(entity.getInvoiceCode()).append(", ");
            str.append("InvoiceNumber=").append(entity.getInvoiceNumber()).append(", ");
            str.append("InvoiceDate=").append(entity.getInvoiceDate()).append(", ");
            str.append("CompanyName=").append(entity.getCompanyName()).append(", ");
            str.append("Amount=").append(entity.getAmount()).append(", ");
            str.append("CheckCode=").append(entity.getCheckingCode()).append(", ");
        }
        return str.toString();
    }

}