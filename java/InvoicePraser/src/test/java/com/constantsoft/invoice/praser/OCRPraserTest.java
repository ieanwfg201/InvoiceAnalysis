package com.constantsoft.invoice.praser;


import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Arrays;

/**
 * Created by walter.xu on 2016/12/16.
 * tesseract invoice.fontyp.exp0.tif invoice.fontyp.exp0 -l chi_sim -c tessedit_char_whitelist=1234567890.：:-普通发票发票代码发票号码开票日期检验码年月日机器编号密码区购买方名称纳税人识别码地址电话开户行及账号合计货物或应税劳务服务名称规格型号单位数量单价金额税率税额税额合计（）大壹贰叁肆伍陆柒捌玖拾佰仟万亿小写销售方备注收款人符合开票人 -psm 3 batch.nochop makebox

 */
public class OCRPraserTest {

    public static void main(String[] args) throws Exception {
        double xStart = 0.7;
        double xEnd = 1;
        double yStart = 0;
        double yEnd = 0.2;
        String file = "D:\\log\\pdf\\10.pdf.300.jpg";
        long start = System.currentTimeMillis();
        System.out.println(Arrays.toString(OCRPraser.praseNumberAndCodeByPart(new File(file),xStart,yStart,xEnd,yEnd)));
        System.out.println("Cost time = "+(System.currentTimeMillis()-start)+"ms");
        start = System.currentTimeMillis();
        System.out.println(Arrays.toString(OCRPraser.praseNumberAndCodeByPart(new File(file), xStart, yStart, xEnd, yEnd)));
        System.out.println("Cost time = "+(System.currentTimeMillis()-start)+"ms");
        start = System.currentTimeMillis();
//        String text = OCRPraser.praseText(new File(file));

        BufferedImage image = ImageIO.read(new File(file));
        Rectangle rect = new Rectangle((int)(0.7*image.getWidth()), 0, (int)(0.3*image.getWidth()), (int)(0.2*image.getHeight()));
        String text = OCRPraser.praseText(new File(file),rect);
        System.out.println(text);
        System.out.println("Total cost: "+(System.currentTimeMillis()-start)+"ms");
    }
}