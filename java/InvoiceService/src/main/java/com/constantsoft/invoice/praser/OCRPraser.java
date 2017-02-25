package com.constantsoft.invoice.praser;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.concurrent.*;

import com.lowagie.text.pdf.PdfImage;
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;

import com.constantsoft.invoice.praser.util.InvoiceCodeAndNumberGenerator;
import com.constantsoft.invoice.praser.util.PDFImageUtils;

/**
 * Created by walter.xu on 2016/12/13.
 */
public class OCRPraser {
//    private static final String INVOICE_LIMIT_CHARS = "1234567890.：:-普通发票发票代码发票号码开票日期检验码年月日机器编号密码区购买方名称纳税人识别码地址电话开户行及账号合计货物或应税劳务服务名称规格型号单位数量单价金额税率税额税额合计（）大壹贰叁肆伍陆柒捌玖拾佰仟万亿小写销售方备注收款人符合开票人";
    // not thread-safe
    private static ITesseract instance;
    
    private static OCRPraser praser;
    
    public static OCRPraser instance(String tessDataPath){
    	if (praser==null) {
			synchronized (OCRPraser.class) {
				if (praser==null) {
					praser = new OCRPraser();
					instance = new Tesseract();
                    instance.setLanguage("eng");
                    if (tessDataPath!=null&&!"".equals(tessDataPath)) {
                    	instance.setDatapath(tessDataPath);
					}
				}
			}
		}
    	return praser;
    }

    public String praseText(File imageFile) throws Exception {
        return instance.doOCR(imageFile);
    }
    public String praseText(File imageFile, Rectangle rect) throws Exception {
        return instance.doOCR(imageFile, rect);
    }

    public String[] praseNumberAndCode(File imageFile) throws Exception{
        String text = instance.doOCR(imageFile);
        String[] billCodeAndNumberArray = new String[]{"-","-"};
        String[] itemArray = text.split("\n");
        for(String item: itemArray){
            InvoiceCodeAndNumberGenerator.findAndSetInvoidNumberAndCodeByNumberLength(item.trim(), billCodeAndNumberArray);
            if (!"-".equals(billCodeAndNumberArray[0])&&!"-".equals(billCodeAndNumberArray[1])) break;
        }
        return billCodeAndNumberArray;
    }



    public String[] praseNumberAndCodeByPart(File imageFile, double xStart, double yStart, double xEnd, double yEnd, boolean isRatate) throws Exception{
        BufferedImage image = PDFImageUtils.getBufferedImageFromFile(imageFile);
        if (isRatate) return praseNumberAndCodeByPartWithRatate(image,xStart,yStart,xEnd,yEnd);
        return praseNumberAndCodeByPart(image,xStart,yStart,xEnd,yEnd);
    }

    public String[] praseNumberAndCodeByPart(BufferedImage image, double xStart, double yStart, double xEnd, double yEnd) throws Exception {
        if (xStart<0||xStart>1) xStart = 0;
        if (yStart<0||yStart>1) yStart = 0;
        if (xEnd<=xStart||xEnd>1) xEnd = 1;
        if (yEnd<=yStart||yEnd>1) yEnd = 1;
        Rectangle rect = new Rectangle((int)(xStart*image.getWidth()), (int)(yStart*image.getHeight()),
                (int)((xEnd-xStart)*image.getWidth()), (int)((yEnd-yStart)*image.getHeight()));
//        String text = praseText(image, rect);
        String text = instance.doOCR(image, rect);
        String[] billCodeAndNumberArray = new String[]{"-","-"};
        String[] itemArray = text.split("\n");
        for(String item: itemArray){
//            item = item.trim();
//            String code = findNextStringByPattern(item, "发票代码", -1, null);
//            if (code!=null&&!"".equals(code)) billCodeAndNumberArray[0] = code;
//            String number = findNextStringByPattern(item, "发票号码", -1, null);
//            if (number!=null&&!"".equals(number)) billCodeAndNumberArray[1] = number;

            // 使用模式匹配
            InvoiceCodeAndNumberGenerator.findAndSetInvoidNumberAndCodeByNumberLength(item.trim(), billCodeAndNumberArray);
            if (!"-".equals(billCodeAndNumberArray[0])&&!"-".equals(billCodeAndNumberArray[1])) break;
        }
        return billCodeAndNumberArray;
    }
    public String[] praseNumberAndCodeByPartWithRatate(BufferedImage image, final double xStart, final double yStart, final double xEnd, final double yEnd) throws Exception {
        String[] result = this.praseNumberAndCodeByPart(image, xStart, yStart, xEnd, yEnd);
        if (isCodeAndNumberValid(result)) return result;
        // ratate 90 angle
        image = PDFImageUtils.ratate(image, 90);
        result = this.praseNumberAndCodeByPart(image, xStart, yStart, xEnd, yEnd);
        if (isCodeAndNumberValid(result)) return result;
        // ratate 90 angle
        image = PDFImageUtils.ratate(image, 90);
        result = this.praseNumberAndCodeByPart(image, xStart, yStart, xEnd, yEnd);
        if (isCodeAndNumberValid(result)) return result;
        // ratate 90 angle
        image = PDFImageUtils.ratate(image, 90);
        result = this.praseNumberAndCodeByPart(image, xStart, yStart, xEnd, yEnd);
        if (isCodeAndNumberValid(result)) return result;
        // 如果360度旋转后依然无法获取到代码信息，则表示无法获取到
        return new String[]{"-","-"};
    }
    private boolean isCodeAndNumberValid(String[] codeAndNumberArray){
        return codeAndNumberArray!=null&&codeAndNumberArray.length==2&&!"-".equals(codeAndNumberArray[0])&&!"-".equals(codeAndNumberArray[1]);
    }

}
