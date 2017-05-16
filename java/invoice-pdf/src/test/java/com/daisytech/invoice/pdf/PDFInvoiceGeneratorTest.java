package com.daisytech.invoice.pdf;

import com.daisytech.invoice.pdf.entity.*;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by walter.xu on 2017/2/28.
 */
public class PDFInvoiceGeneratorTest {
    PDFInvoiceGenerator generator = new PDFInvoiceGenerator();

    public static void main(String[] args) throws Exception {
        PDFInvoiceGeneratorTest test = new PDFInvoiceGeneratorTest();
//        test.testGenerate();
        test.testGenerateAll();
//        test.testGenerateAllCount();

//        String address = "/home/walter/Desktop/2017050811175217524872680313146559236.pdf";
//          String address = "C:\\Users\\Walter\\Desktop\\exampleFile\\2017050811175617564872676813146559236.pdf";
        String address = "C:\\Users\\Walter\\Desktop\\exampleFile\\10.pdf.200.jpg";

//        test.testGenerateFile(address);
//        test.praseText(address);
    }

    public void testGenerateAll() {
        String directory = "C:\\Users\\Walter\\Desktop\\exampleFile";
//        String directory = "/services/git/GIthub/InvoiceAnalysis/testingFiles";
        File[] fileArray = new File(directory).listFiles();
        List<String> resultList = new ArrayList<String>();
        int failed = 0, success = 0;
        for (File file : fileArray) {
            try {
                InvoiceInfosEntity entity = generator.generate(file, true);
                success++;
                resultList.add(file.getName() + ": " + printEntity(entity));
            } catch (Exception e) {
                failed++;
                resultList.add(file.getName() + ": " + e.getMessage());
            }
        }
        for (String result : resultList) {
            System.out.println(result);
        }
        System.out.println("Success: " + success + ", failed: " + failed);
    }

    public void testGenerateAllCount() {
        String directory = "C:\\Users\\Walter\\Desktop\\exampleFile";
//        String directory = "/services/git/GIthub/InvoiceAnalysis/testingFiles";
        File[] fileArray = new File(directory).listFiles();
        Map<String, InvoiceAllEntity> map = new HashMap<>();
        for (File file : fileArray) {
            try {
                InvoiceAllEntity entity = generator.generateAll(file, true);
                map.put(file.getName(), entity);
            } catch (Exception e) {
                InvoiceAllEntity entity = new InvoiceAllEntity();
                entity.setErrorCode(1);
                entity.setErrorMessage(e.getMessage());
                map.put(file.getName(), entity);
            }
        }
        List<String> successList = new ArrayList<>();
        List<String> failedList = new ArrayList<>();
        List<String> checkSignFailList = new ArrayList<>();
        for (String key : map.keySet()) {
            InvoiceAllEntity entity = map.get(key);
            if (entity.getErrorCode() == 1) failedList.add(key);
            else if (entity.getCheckSignatureCode() == 1) checkSignFailList.add(key);
            else successList.add(key);
        }
        System.out.println("Success lines");
        for (String key : successList) {
            System.out.println("   " + key + ": " + formatStr(map.get(key)));
        }
        System.out.println("Failed lines");
        for (String key : failedList) {
            System.out.println("   " + key + ": " + formatStr(map.get(key)));
        }
        System.out.println("CheckSIgnFailed lines");
        for (String key : checkSignFailList) {
            System.out.println("   " + key + ": " + formatStr(map.get(key)));
        }
        System.out.println("Success: " + successList.size() + ", failed: " + failedList.size() + ", checkSignFailed: " + checkSignFailList.size());

    }

    public void testGenerate() throws Exception {
        String file = "C:\\Users\\walter.xu\\Desktop\\exampleFile\\17.pdf";
        InvoiceInfosEntity entity = generator.generate(new File(file));
        System.out.println(printEntity(entity));
        entity = generator.generate(new File(file), true);
        System.out.println(printEntity(entity));
    }

    private String printEntity(InvoiceInfosEntity entity) {
        StringBuilder str = new StringBuilder();

        str.append("Entity: ");
        if (entity == null) str.append("null");
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

    private void testGenerateFile(String filePath) {
        StringBuilder str = new StringBuilder();
        str.append("Testing file: " + filePath).append("\n");
        InvoiceAllEntity entity = generator.generateAll(new File(filePath), true);
        str.append(formatStr(entity)).append("\n");
        str.append(formatStr(generator.getInvoiceCode(new File(filePath)))).append("\n");
        str.append(formatStr(generator.getInvoiceNumber(new File(filePath)))).append("\n");
        str.append(formatStr(generator.getInvoiceDateStr(new File(filePath)))).append("\n");
        str.append(formatStr(generator.getCheckingCode(new File(filePath)))).append("\n");
        str.append(formatStr(generator.checkingSignature(new File(filePath)))).append("\n");
        System.out.println(str);
    }

    private String formatStr(InvoiceAllEntity entity) {
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

    private String formatStr(InvoiceCodeEntity entity) {
        StringBuilder str = new StringBuilder();
        str.append("InvoiceCodeEntity: ")
                .append(entity.getErrorCode()).append(": ").append(entity.getErrorMessage()).append("| ");
        str.append("code=").append(entity.getInvoiceCode());
        return str.toString();
    }

    private String formatStr(InvoiceNumberEntity entity) {
        StringBuilder str = new StringBuilder();
        str.append("InvoiceNumberEntity: ")
                .append(entity.getErrorCode()).append(": ").append(entity.getErrorMessage()).append("| ");
        str.append("code=").append(entity.getInvoiceNumber());
        return str.toString();
    }

    private String formatStr(InvoiceDateEntity entity) {
        StringBuilder str = new StringBuilder();
        str.append("InvoiceDateEntity: ")
                .append(entity.getErrorCode()).append(": ").append(entity.getErrorMessage()).append("| ");
        str.append("code=").append(entity.getInvoiceDateStr());
        return str.toString();
    }

    private String formatStr(CheckingCodeEntity entity) {
        StringBuilder str = new StringBuilder();
        str.append("CheckingCodeEntity: ")
                .append(entity.getErrorCode()).append(": ").append(entity.getErrorMessage()).append("| ");
        str.append("code=").append(entity.getCheckingCode());
        return str.toString();
    }

    private String formatStr(BaseEntity entity) {
        StringBuilder str = new StringBuilder();
        str.append("BaseEntity: ")
                .append(entity.getErrorCode()).append(": ").append(entity.getErrorMessage()).append("| ");

        return str.toString();
    }

    /*public void praseText(String file) throws Exception{
        PDFTextStripper stripper = new PDFTextStripper();
        PDDocument pd = PDDocument.load(new File(file));
        System.out.println(stripper.getText(pd));
    }*/
}