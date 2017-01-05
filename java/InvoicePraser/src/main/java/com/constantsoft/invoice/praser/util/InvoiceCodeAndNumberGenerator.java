package com.constantsoft.invoice.praser.util;

/**
 * Created by walter.xu on 2016/12/29.
 */
public class InvoiceCodeAndNumberGenerator {
    public static void findAndSetInvoidNumberAndCodeByNumberLength(String line, String[] invoiceNumberAndCodeArray){
        if (line==null||"".equals(line.trim())) return ;
        line = line.trim();
        int startIndex = 0, endIndex = 0;
        String invoiceNumber = null;
        String invoiceCode = null;
        while(endIndex<line.length()){
            if (endIndex == startIndex&&line.charAt(endIndex)>='0'&&line.charAt(endIndex)<='9'){
                endIndex++;
            }else if (line.charAt(endIndex)>='0'&&line.charAt(endIndex)<='9'){
                endIndex++;
            }else{
                // 校验是否为所需的字符
                if ((endIndex-startIndex == 8)&&invoiceNumber==null) invoiceNumber = line.substring(startIndex, endIndex);
                if ((endIndex-startIndex == 12)&&invoiceCode==null) invoiceCode = line.substring(startIndex, endIndex);
                if ((endIndex-startIndex == 10)&&invoiceCode==null) invoiceCode = line.substring(startIndex, endIndex);
                endIndex++;startIndex=endIndex;
            }
        }
        if (invoiceNumber==null||invoiceCode==null){
            if ((endIndex-startIndex == 8)&&invoiceNumber==null) invoiceNumber = line.substring(startIndex, endIndex);
            if ((endIndex-startIndex == 12)&&invoiceCode==null) invoiceCode = line.substring(startIndex, endIndex);
            if ((endIndex-startIndex == 10)&&invoiceCode==null) invoiceCode = line.substring(startIndex, endIndex);
        }
        if (invoiceCode!=null) invoiceNumberAndCodeArray[0] = invoiceCode;
        if (invoiceNumber!=null) invoiceNumberAndCodeArray[1] = invoiceNumber;
    }

    public static String findNextStringByPattern(String line, String prefix, int maxNextChars, String availableChars){
//        long start =System.currentTimeMillis();
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
//        System.out.println("find: "+prefix+", cost="+(System.currentTimeMillis()-start)+"ms");
        return null;
    }
}
