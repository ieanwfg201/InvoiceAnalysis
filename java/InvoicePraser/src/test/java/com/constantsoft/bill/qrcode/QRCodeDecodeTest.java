package com.constantsoft.bill.qrcode;


import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * Created by walter.xu on 2016/12/7.
 */
public class QRCodeDecodeTest {

    public static void main(String[] args) throws Exception {
        PDDocument doc = null;
        try {
            doc = PDDocument.load(new File("D:\\log\\pdf\\liantong.pdf"));
            PDFRenderer render = new PDFRenderer(doc);
            BufferedImage image = render.renderImageWithDPI(doc.getNumberOfPages()-1,300);
            ImageIO.write(image, "jpg", new File("D:\\log\\pdf\\test.jpg"));
//            QRCodeDecode.decode(image);
            QRCodeDecode.decode(ImageIO.read(new File("D:\\log\\pdf\\123.jpg")));
            QRCodeDecode.decode(ImageIO.read(new File("D:\\log\\pdf\\1234.png")));
            QRCodeDecode.decode(ImageIO.read(new File("D:\\log\\pdf\\12345.png")));
            QRCodeDecode.decode(ImageIO.read(new File("D:\\log\\pdf\\test.jpg")));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}