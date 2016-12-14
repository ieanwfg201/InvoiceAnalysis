package com.constantsoft.bill.qrcode;


import com.google.zxing.BinaryBitmap;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.multi.qrcode.QRCodeMultiReader;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Arrays;

/**
 * Created by walter.xu on 2016/12/1.
 */
public class QRCodeDecode {

    public static String decode(BufferedImage image) throws Exception{
        QRCodeMultiReader reader = new QRCodeMultiReader();
        Result[] results = reader.decodeMultiple(new BinaryBitmap(new HybridBinarizer(new BufferedImageLuminanceSource(image))));
        System.out.println(results==null?null:Arrays.toString(results));
        return null;
    }
}
