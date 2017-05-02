package com.constantsoft.invoice.pdf;

import com.constantsoft.invoice.pdf.entity.*;

import javax.annotation.Generated;
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
//        test.testGenerateAll();

//        test.testGenerateFile("/home/walter/Desktop/01100160011148231758.pdf");
    }

    public void testGenerateAll() {
//        String directory = "C:\\Users\\walter.xu\\Desktop\\exampleFile";
        String directory = "/services/git/GIthub/InvoiceAnalysis/testingFiles";
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
            str.append("InvoiceDate=").append(entity.getInvoiceDateStr()).append(", ");
            str.append("CompanyName=").append(entity.getCompanyName()).append(", ");
            str.append("Amount=").append(entity.getAmount()).append(", ");
            str.append("CheckCode=").append(entity.getCheckingCode()).append(", ");
        }
        return str.toString();
    }

    private void testGenerateFile(String filePath){
        StringBuilder str = new StringBuilder();
        str.append("Testing file: "+filePath).append("\n");
        InvoiceAllEntity entity = generator.generateAll(new File(filePath), true);
        str.append(formatStr(entity)).append("\n");
        str.append(formatStr(generator.getInvoiceCode(new File(filePath)))).append("\n");
        str.append(formatStr(generator.getInvoiceNumber(new File(filePath)))).append("\n");
        str.append(formatStr(generator.getInvoiceDateStr(new File(filePath)))).append("\n");
        str.append(formatStr(generator.getCheckingCode(new File(filePath)))).append("\n");
        str.append(formatStr(generator.checkingSignature(new File(filePath)))).append("\n");
        System.out.println(str);
    }

    private String formatStr(InvoiceAllEntity entity){
        StringBuilder str = new StringBuilder();
        str.append("InvoiceAllEntity: ")
                .append(entity.getErrorCode()).append(": ").append(entity.getErrorMessage()).append("\n");
        str.append("    code=").append(entity.getInvoiceCode())
                .append(", number=").append(entity.getInvoiceNumber())
                .append(", date=").append(entity.getInvoiceDateStr())
                .append(", Company=").append(entity.getCompanyName())
                .append(", checkCode=").append(entity.getCheckingCode())
                .append(", AMount=").append(entity.getAmount()).append("\n");
        str.append("    SignatureChecking=").append(entity.isCheckSignature())
                .append(", checkSignCode=").append(entity.getCheckSignatureCode())
                .append(", checkSignMessage=").append(entity.getCheckSignatureMessage());
        return str.toString();
    }
    private String formatStr(InvoiceCodeEntity entity){
        StringBuilder str = new StringBuilder();
        str.append("InvoiceCodeEntity: ")
                .append(entity.getErrorCode()).append(": ").append(entity.getErrorMessage()).append("| ");
        str.append("code=").append(entity.getInvoiceCode());
        return str.toString();
    }
    private String formatStr(InvoiceNumberEntity entity){
        StringBuilder str = new StringBuilder();
        str.append("InvoiceNumberEntity: ")
                .append(entity.getErrorCode()).append(": ").append(entity.getErrorMessage()).append("| ");
        str.append("code=").append(entity.getInvoiceNumber());
        return str.toString();
    }
    private String formatStr(InvoiceDateEntity entity){
        StringBuilder str = new StringBuilder();
        str.append("InvoiceDateEntity: ")
                .append(entity.getErrorCode()).append(": ").append(entity.getErrorMessage()).append("| ");
        str.append("code=").append(entity.getInvoiceDateStr());
        return str.toString();
    }
    private String formatStr(CheckingCodeEntity entity){
        StringBuilder str = new StringBuilder();
        str.append("CheckingCodeEntity: ")
                .append(entity.getErrorCode()).append(": ").append(entity.getErrorMessage()).append("| ");
        str.append("code=").append(entity.getCheckingCode());
        return str.toString();
    }
    private String formatStr(BaseEntity entity){
        StringBuilder str = new StringBuilder();
        str.append("BaseEntity: ")
                .append(entity.getErrorCode()).append(": ").append(entity.getErrorMessage()).append("| ");

        return str.toString();
    }
}