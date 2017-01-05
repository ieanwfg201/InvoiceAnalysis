package com.constantsoft.invoice.praser.util;

import com.constantsoft.invoice.praser.PdfTextPraser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;

import javax.imageio.ImageIO;
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
}
