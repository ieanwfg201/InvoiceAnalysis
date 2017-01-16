package com.constantsoft.invoice.praser.util;

import com.constantsoft.invoice.praser.PdfTextPraser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by walter.xu on 2016/12/19.
 */
public class PDFImageUtils {

    public static BufferedImage getBufferedImageFromFile(File imageFile) throws Exception{
        if (imageFile.getName().toLowerCase().endsWith("pdf"))
            return transPDFToImage(imageFile, 300);
        else
            return ImageIO.read(imageFile);
    }

    public static BufferedImage transPDFToImage(File pdfFile, int dpi){
        if (pdfFile==null) return null;
        if (dpi ==0) dpi = 200;
        PDDocument doc = null;
        try {
            doc = PDDocument.load(pdfFile);
            PDFRenderer render = new PDFRenderer(doc);
            return render.renderImageWithDPI(0, dpi);
        } catch (Exception e){
        } finally{
            if (doc!=null)try{doc.close();}catch(Exception e1){}
        }
        return null;
    }

    public static String transPDFToImageFilePath(File pdfFile, String targetFileName, int dpi){
        BufferedImage image = transPDFToImage(pdfFile, dpi);
        if (image!=null)
            try{
                if (targetFileName==null||"".equals(targetFileName.trim())) targetFileName = pdfFile+"."+dpi+".jpg";
                ImageIO.write(image,"jpg", new File(targetFileName)); return targetFileName ;}catch(Exception e){}
        return null;
    }

    public static List<String> transPdfToImageOfDirectory(String filePath, String targetFilePath, int dpi){
        File directory = new File(filePath);
        if (directory.isDirectory()){
            List<String> resList = new ArrayList<String>();
            File[] subFileArray = directory.listFiles();
            for(File subFile: subFileArray){
                if (subFile.getPath().toLowerCase().endsWith(".pdf")){
                    String imagePath = transPDFToImageFilePath(subFile, targetFilePath+File.separator+subFile.getName()+"."+dpi+".jpg", dpi);
                    if (imagePath!=null) resList.add(imagePath);
                }
            }
            return resList;
        }
        return null;
    }

    /**
     * 将一个图片无损角度顺时针旋转
     * @param image 待旋转图片
     * @param angel 选装角度,
     * @return 旋转后的角度
     */
    public static BufferedImage ratate(BufferedImage image, int angel){
        int src_width = image.getWidth();
        int src_height = image.getHeight(null);
        // calculate the new image size
        Rectangle rect_des = calcRotatedSize(new Rectangle(new Dimension(
                src_width, src_height)), angel);

        BufferedImage res = null;
        res = new BufferedImage(rect_des.width, rect_des.height,
                BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = res.createGraphics();
        // transform
        g2.translate((rect_des.width - src_width) / 2,
                (rect_des.height - src_height) / 2);
        g2.rotate(Math.toRadians(angel), src_width / 2, src_height / 2);

        g2.drawImage(image, null, null);
        return res;
    }
    private static Rectangle calcRotatedSize(Rectangle src, int angel) {
        // if angel is greater than 90 degree, we need to do some conversion
        if (angel >= 90) {
            if(angel / 90 % 2 == 1){
                int temp = src.height;
                src.height = src.width;
                src.width = temp;
            }
            angel = angel % 90;
        }

        double r = Math.sqrt(src.height * src.height + src.width * src.width) / 2;
        double len = 2 * Math.sin(Math.toRadians(angel) / 2) * r;
        double angel_alpha = (Math.PI - Math.toRadians(angel)) / 2;
        double angel_dalta_width = Math.atan((double) src.height / src.width);
        double angel_dalta_height = Math.atan((double) src.width / src.height);

        int len_dalta_width = (int) (len * Math.cos(Math.PI - angel_alpha
                - angel_dalta_width));
        int len_dalta_height = (int) (len * Math.cos(Math.PI - angel_alpha
                - angel_dalta_height));
        int des_width = src.width + len_dalta_width * 2;
        int des_height = src.height + len_dalta_height * 2;
        return new java.awt.Rectangle(new Dimension(des_width, des_height));
    }
}
