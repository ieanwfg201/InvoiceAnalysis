package com.constantsoft.bill.praser;

import com.constantsoft.bill.CommonLog;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Arrays;

/**
 * Created by walter.xu on 2016/11/30.
 * @author walter.xu
 */
@Deprecated
public class BillPraser {
    protected CommonLog logger = new CommonLog("BillPraser");
    private static BillPraser instance = new BillPraser();
    private BillPraser(){}

    public static BillPraser getInstance(){
        return instance;
    }

    public String[] generateBillCodeAndNumber(String pdfPath){
        return this.generateBillCodeAndNumber(new File(pdfPath));
    }

    public String[] generateBillCodeAndNumber(File pdfFile){
        if (pdfFile==null||!pdfFile.exists()){
            logger.error("target pdf file is not exist. file path: "+ (pdfFile==null?null:pdfFile.getPath()));
            return null;
        }
        String[] billCodeAndNumberArray = new String[2];
        PDDocument doc = null;
        try {
            doc = PDDocument.load(pdfFile);
            PDFTextStripper stripper = new PDFTextStripper();
            stripper.setSortByPosition(true);
            String text = stripper.getText(doc);

            String[] itemArray = text.split("\r\n");
            for(String item: itemArray){
                item = item.trim();
                String code = findNextStringByPattern(item, "发票代码", -1, null);
                if (code!=null) billCodeAndNumberArray[0] = code;
                String number = findNextStringByPattern(item, "发票号码", -1, null);
                if (number!=null) billCodeAndNumberArray[1] = number;
                /*String[] keyValue = item.replaceAll("：",":").split(":");
                if (keyValue.length==2){
                    // bill code
                    if (keyValue[0].contains("发票代码")) billCodeAndNumberArray[0] = keyValue[1].trim();
                    if (keyValue[0].contains("发票号码")) billCodeAndNumberArray[1] = keyValue[1].trim();
                }*/
            }
        }catch (Exception e){
            throw new RuntimeException(e);
        } finally {
            if (doc != null) try{ doc.close();}catch (Exception e){}
        }
        return billCodeAndNumberArray;
    }

    protected String findNextStringByPattern(String line, String prefix, int maxNextChars, String availableChars){
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
