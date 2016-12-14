package com.constantsoft.invoice.praser;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.multi.qrcode.QRCodeMultiReader;

import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.regex.Pattern;

/**
 * Created by walter.xu on 2016/12/12.
 */
public class QRCodePraser {

    public static String[] praseNumberAndCode(BufferedImage image) throws Exception{
        QRCodeMultiReader reader = new QRCodeMultiReader();
        Result[] results = reader.decodeMultiple(new BinaryBitmap(new HybridBinarizer(new BufferedImageLuminanceSource(image))));
        if (results!=null&&results.length>0){
            for(Result result: results){
                String[] codeAndArray = praseLine(result.getText());
                if (!"-".equals(codeAndArray[0])&&!"-".equals(codeAndArray[1])) return codeAndArray;
            }
        }
        return new String[]{"-","-"};
    }

    private static String[] praseLine(String line){
        if (line==null||"".equals(line.trim())) return null;
        String[] arrays = line.split(",");
        String code = "-" , number = "-";
        for(String arg0: arrays){
            if (Pattern.matches("[0-9]{12}",arg0.trim())) code = arg0.trim();
            // forbid invoice date: yyyyMMdd
            if ("-".equals(number)&&Pattern.matches("[0-9]{8}",arg0.trim())) number = arg0.trim();
        }
        return new String[]{code, number};
    }
}
