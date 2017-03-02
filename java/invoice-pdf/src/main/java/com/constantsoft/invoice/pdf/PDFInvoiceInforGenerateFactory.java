package com.constantsoft.invoice.pdf;

import com.constantsoft.invoice.pdf.entity.InvoiceInfosEntity;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Walter on 2017/2/18.
 */
public final class PDFInvoiceInforGenerateFactory {
    private static final String CHAR_NUMBER = "1234567890";
    private static final String CHAR_NUMBER_SPACE = "1234567890 ";
    private static final String CHAR_AMOUNT = "1234567890.";

    // available data
    private List<String> availableInvoiceCodeList = new ArrayList<String>();
    private List<String> avaialbleInvoiceNumberList = new ArrayList<String>();
    private String availableInvoiceDateStr;
    private List<String> availableCheckingCodeList = new ArrayList<String>();
    private List<String> availableMachineCodeList = new ArrayList<String>();
    private List<Double> availableAmountList = new ArrayList<Double>();
    // 公司名称
    private List<String> availableCampanyNameList = new ArrayList<String>();


    public InvoiceInfosEntity getInvoiceInfoEntity() {
        InvoiceInfosEntity entity = new InvoiceInfosEntity();
        if (availableInvoiceCodeList.size() > 0) entity.setInvoiceCode(availableInvoiceCodeList.get(0));
        if (avaialbleInvoiceNumberList.size() > 0) entity.setInvoiceNumber(avaialbleInvoiceNumberList.get(0));

        entity.setInvoiceDateStr(availableInvoiceDateStr);
        if (availableCheckingCodeList.size() > 0) entity.setCheckingCode(availableCheckingCodeList.get(0));
        if (availableMachineCodeList.size() > 0) entity.setMachineCode(availableMachineCodeList.get(0));
        if (availableAmountList.size()>0){
            Collections.sort(availableAmountList);
            entity.setAmount(availableAmountList.get(availableAmountList.size() - 1));
        }
        if (availableCampanyNameList.size()>0) entity.setCompanyName(availableCampanyNameList.get(0));
        return entity;
    }

    public void executeLine(String item) {
        if (item == null || "".equals(item.trim())) return;

        executeInvoiceCodeAndNumber(item);
        executeInvoiceDate(item);
        executeCheckingCode(item);
        executeAmount(item);
        executeCompanyName(item);
    }

    private void executeInvoiceCodeAndNumber(String item) {
        int startIndex = 0;
        String matchItem = findStrByAvailableWords(item, startIndex, 0, CHAR_NUMBER);
        while (matchItem != null && !"".equals(matchItem)) {
            matchItem = matchItem.trim();
            // code
            if (matchItem.length() == 10 || matchItem.length() == 12) {
                if (item.indexOf("发票代码") > 0) availableInvoiceCodeList.add(0, matchItem);
                else availableInvoiceCodeList.add(matchItem);
            }
            // number
            if (matchItem.length() == 8) {
                if (item.indexOf("发票号码") > 0) avaialbleInvoiceNumberList.add(0, matchItem);
                else avaialbleInvoiceNumberList.add(matchItem);
            }
            startIndex = item.indexOf(matchItem, startIndex) + matchItem.length()+1;
            matchItem = findStrByAvailableWords(item, startIndex, 0, CHAR_NUMBER);
        }
    }

    private void executeInvoiceDate(String item) {
        // 普通增值税发票格式为: yyyy年MM月dd日
        if (availableInvoiceDateStr == null && item.contains("年") && item.contains("月") && item.contains("日")) {
            item = item.replaceAll(" ", "");
            int start = item.indexOf("年");
            try {
//                System.out.println("dateStr-old: "+item);
                String dateStr = item.substring(start - 4, item.indexOf("日", start)).replace("年", "").replace("月", "");
//                System.out.println("dateStr: "+dateStr);
                if (dateStr.length() == 8) {
                    try{
                        availableInvoiceDateStr = dateStr.trim();
                    }catch (Exception e){}
                }
            } catch (Exception e) {
            }
        }else if (availableInvoiceDateStr==null&&item.contains("开票日期")){
            // 国家税务局发票 格式为：开票日期：yyyy-MM-dd;
            String matchItem = findStrByAvailableWords(item, item.indexOf("开票日期")+4, 0, "1234567890-");
            if (matchItem!=null&&matchItem.trim().length()==10){
                try{
                    availableInvoiceDateStr = matchItem.trim().replaceAll("-","");
                }catch (Exception e){}
            }
        }
    }
    // 每隔 5个数字为一组，共计20个数字
    private void executeCheckingCode(String item){
        int startIndex = 0;
        String itemWithNoSpace = item.replaceAll(" ","");
        String matchItem = findStrByAvailableWords(item, startIndex, 20, CHAR_NUMBER_SPACE);
        while (matchItem != null && !"".equals(matchItem)) {
            matchItem = matchItem.trim();
            // check code
            if (matchItem.replaceAll(" ","").length()==20) {
                if (itemWithNoSpace.contains("校验码")) availableCheckingCodeList.add(0, matchItem);
                else availableCheckingCodeList.add(matchItem);
            }
            startIndex = item.indexOf(matchItem, startIndex) + matchItem.length()+1;
            matchItem = findStrByAvailableWords(item, startIndex, 0, CHAR_NUMBER_SPACE);
        }
    }
    // 格式应该为xxx.xx
    private void executeAmount(String item) {
        int startIndex = 0;
        String matchItem = findStrByAvailableWords(item, startIndex, 0, CHAR_AMOUNT);
        while (matchItem != null && !"".equals(matchItem)) {
            matchItem = matchItem.trim();
            // 仅仅获取到包含两位小数点位数的值
            if (matchItem.contains(".")&&matchItem.indexOf(".")+3==matchItem.length()){
                try {
                    availableAmountList.add(Double.valueOf(matchItem));
                }catch (Exception e){}
            }
            startIndex = item.indexOf(matchItem,startIndex) + matchItem.length()+1;
            matchItem = findStrByAvailableWords(item, startIndex, 0, CHAR_AMOUNT);
        }

        /*if (!item.contains("￥")) return;
        int start = 0;
        int end = item.indexOf("￥");
        while (end < item.length()) {
            if (CHAR_AMOUNT.indexOf(item.charAt(end)) < 0) {
                end++;
                if (start == 0) continue;
                else break;
            }
            if (start == 0) start = end;
            end++;
        }
        if (start > 0 && end > start) {
            try {
                availableAmountList.add(Double.valueOf(item.substring(start, end)));
            } catch (Exception e) {
            }
        }*/
    }

    private void executeCompanyName(String item){
        int startIndex = item.indexOf("名");
        int endIndex = item.indexOf("称");
        if (startIndex<0||endIndex<=startIndex) return ;
        String checkSpace = item.substring(startIndex+1, endIndex);
        if ("".equals(checkSpace.trim())){
            String[] arrays = item.substring(endIndex+1).split(" ");
            for(String str: arrays){
                if (!":".equals(str.trim())&&!"".equals(str.trim())&&!"：".equals(str.trim())){
                    String companyName = str.trim().replaceAll(":","").replaceAll("：", "");
                    if (companyName.endsWith("公司"))availableCampanyNameList.add(0, companyName);
                    else availableCampanyNameList.add(companyName);

                }

            }
        }
    }

    public String findStrByAvailableWords(String item, int startPostion, String availableWords) {
        if (item == null || "".equals(item.trim()) || startPostion >= item.length()) return null;
        int start = 0;
        int end = startPostion;
        while (end < item.length()) {
            if (availableWords.indexOf(item.charAt(end)) < 0) {
                end++;
                if (start == 0) continue;
                else break;
            }
            if (start == 0) start = end;
            end++;
        }
        if (start==0) return null;
        if (end == item.length()) end++;
        return item.substring(start, end-1);
    }

    public String findStrByAvailableWords(String item, int startPostion, int minLength, String availableWords) {
        if (item == null || "".equals(item.trim()) || startPostion >= item.length()) return null;
        int start = 0;
        int end = startPostion;
        while (end < item.length()) {
            if (availableWords.indexOf(item.charAt(end)) < 0) {
                end++;
                if (start==0) continue;
                else if (minLength>0&&end-start<minLength){
                    start = end; continue;
                }else {break;}
                /*if (end-start<minLength){
                    start = end;continue;
                }
                else {
                    break;
                }*/
            }
            if (start == 0) start = end;
            end++;
        }
        if (start==0) return null;
        if (end == item.length()) end++;
        return item.substring(start, end-1);
    }
}
