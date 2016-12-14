package com.constantsoft.invoice.praser;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.awt.image.BufferedImage;
import java.io.File;

/**
 * Created by walter.xu on 2016/12/12.
 */
public class PdfTextPraser {
    public static String[] praseNumberAndCode(File file) throws Exception{
        String[] billCodeAndNumberArray = new String[]{"-","-"};
        if (file==null||!file.exists()||file.isDirectory()) return billCodeAndNumberArray;
        PDDocument doc = null;
        try {
            doc = PDDocument.load(file);
            billCodeAndNumberArray = praseNumberAndCode(doc);
        } finally {
            if (doc!=null) doc.close();
        }
        return billCodeAndNumberArray;
    }

    public static String[] praseNumberAndCode(PDDocument doc) throws Exception{
        if (doc == null) return null;
        String[] billCodeAndNumberArray = new String[]{"-","-"};
        PDFTextStripper stripper = new PDFTextStripper();
        stripper.setSortByPosition(true);
        String text = stripper.getText(doc);
        String[] itemArray = text.split("\r\n");
        for(String item: itemArray){
            item = item.trim();
            String code = findNextStringByPattern(item, "发票代码", -1, null);
            if (code!=null&&!"".equals(code)) billCodeAndNumberArray[0] = code;
            String number = findNextStringByPattern(item, "发票号码", -1, null);
            if (number!=null&&!"".equals(number)) billCodeAndNumberArray[1] = number;
        }
        return billCodeAndNumberArray;
    }

    protected static String findNextStringByPattern(String line, String prefix, int maxNextChars, String availableChars){
        if (availableChars == null||"".equals(availableChars.trim())) availableChars = "1234567890";
        if (maxNextChars < 0) maxNextChars = 5;
        if (line ==null ||prefix == null) return null;
        int startIndex = line.indexOf(prefix);
        if (startIndex>=0){
            int endIndex = 0;
            startIndex += prefix.length();
            for(int index = startIndex+1; index < line.length(); index++){
                if (endIndex==0&&availableChars.contains(String.valueOf(line.charAt(index)))){
                    startIndex = index; endIndex = index+1;
                }else if (availableChars.contains(String.valueOf(line.charAt(index)))){
                    endIndex = index+1;
                }else if (endIndex!=0)
                    break;
            }
            if (endIndex > startIndex) return line.substring(startIndex,endIndex);
        }
        return null;
    }
}
