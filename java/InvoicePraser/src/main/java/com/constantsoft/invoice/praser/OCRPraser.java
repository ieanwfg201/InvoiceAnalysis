package com.constantsoft.invoice.praser;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract1;

import java.io.File;

/**
 * Created by walter.xu on 2016/12/13.
 */
public class OCRPraser {
    private static final String INVOICE_LIMIT_CHARS = "1234567890.：:-普通发票发票代码发票号码开票日期检验码年月日机器编号密码区购买方名称纳税人识别码地址电话开户行及账号合计货物或应税劳务服务名称规格型号单位数量单价金额税率税额税额合计（）大壹贰叁肆伍陆柒捌玖拾佰仟万亿小写销售方备注收款人符合开票人";

    private static class OCRPraserHolder{
        private static ITesseract instance = new Tesseract1();
        static {
            instance.setLanguage("chi_sim");
            instance.setTessVariable("tessedit_char_whitelist", INVOICE_LIMIT_CHARS);
        }
    }

    public static String praseText(File imageFile) throws Exception {
        return OCRPraserHolder.instance.doOCR(imageFile);
    }

    public static String[] praseNumberAndCode(File imageFile) throws Exception{
        String text =OCRPraserHolder.instance.doOCR(imageFile);
        String[] billCodeAndNumberArray = new String[]{"-","-"};
        String[] itemArray = text.split("\n");
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
