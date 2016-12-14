package com.constantsoft.invoice;

import com.constantsoft.bill.CommonLog;
import com.constantsoft.invoice.praser.OCRPraser;
import com.constantsoft.invoice.praser.PdfTextPraser;
import com.constantsoft.invoice.praser.QRCodePraser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Arrays;

/**
 * Created by walter.xu on 2016/12/12.
 * @author walter.xu
 */
public class InvoiceGenerator {
    protected CommonLog logger = new CommonLog(this.getClass());
    private static InvoiceGenerator generator = new InvoiceGenerator();
    private InvoiceGenerator(){}
    public static InvoiceGenerator instance(){
        return generator;
    }
    public String[] generateInvoiceCodeAndNumber(String filePath, boolean loadFromPdfText, boolean loadFromQrcode, boolean loadFromOCR){
        if (filePath == null||"".equals(filePath.trim())) return new String[]{"-","-"};
        if (!loadFromPdfText&&!loadFromQrcode&&!loadFromOCR)
            return generateInvoiceCodeAndNumberValidateByPdfAndQrcode(filePath);
        String[] codeAndNumberArray = null;
        try {
            if (loadFromPdfText) codeAndNumberArray = PdfTextPraser.praseNumberAndCode(new File(filePath));
            if (!isCodeAndNumberValid(codeAndNumberArray)&&loadFromQrcode) codeAndNumberArray = QRCodePraser.praseNumberAndCode(ImageIO.read(new File(filePath)));
            if (!isCodeAndNumberValid(codeAndNumberArray)&&loadFromOCR) codeAndNumberArray = OCRPraser.praseNumberAndCode(new File(filePath));
        } catch (Exception e){
            logger.error("Error to prase file: "+filePath+", error="+e.getMessage());
        }
        return isCodeAndNumberValid(codeAndNumberArray)?codeAndNumberArray:new String[]{"-","-"};
    }

    public String[] generateInvoiceCodeAndNumberValidateByPdfAndQrcode(String filePath){
        PDDocument doc = null;
        try {
            if (isImage(filePath)) return QRCodePraser.praseNumberAndCode(ImageIO.read(new File(filePath)));
            if (isPdf(filePath)){
                doc = PDDocument.load(new File(filePath));
                String[] codeAndNumberArrayFromPdfText = PdfTextPraser.praseNumberAndCode(doc);

                PDFRenderer render = new PDFRenderer(doc);
                BufferedImage image = render.renderImageWithDPI(doc.getNumberOfPages() - 1, 300);
                String[] codeAndNumberArrayFromQrcode = QRCodePraser.praseNumberAndCode(image);

                return selectCorrectCodeAndNumberArray(codeAndNumberArrayFromPdfText, codeAndNumberArrayFromQrcode);
            }
        } catch (Exception e){
            logger.error("Error to prase file by pdf and qrcode. file: "+filePath+", message="+e.getMessage());
        } finally {
            if (doc!=null)try {
                doc.close();
            }catch (Exception e){}
        }
        return new String[]{"-","-"};
    }

    public String[] selectCorrectCodeAndNumberArray(String[] codeAndNumberArray1, String[] codeAndNumberArray2){
        boolean array1Valid = isCodeAndNumberValid(codeAndNumberArray1);
        boolean array2Valid = isCodeAndNumberValid(codeAndNumberArray2);
        if (array1Valid&&!array2Valid) return codeAndNumberArray1;
        else if (!array1Valid&&array2Valid) return codeAndNumberArray2;
        else if (!array1Valid&&!array2Valid) return codeAndNumberArray1;
        else{
            if (!codeAndNumberArray1[0].equals(codeAndNumberArray2[0])||codeAndNumberArray1[0].equals(codeAndNumberArray2[1]))
                logger.error("Error to match by text and qrcode, text: "+ Arrays.toString(codeAndNumberArray1)+", qrcode:"+Arrays.toString(codeAndNumberArray2));
            return codeAndNumberArray1;
        }
    }

    private boolean isCodeAndNumberValid(String[] codeAndNumberArray){
        return codeAndNumberArray!=null&&codeAndNumberArray.length==2&&!"-".equals(codeAndNumberArray[0])&&!"-".equals(codeAndNumberArray[1]);
    }

    private boolean isPdf(String filePath){
        return filePath!=null&&filePath.toLowerCase().endsWith(".pdf");
    }
    private boolean isImage(String filePath){
        if (filePath!=null){
            filePath = filePath.toLowerCase();
            // bmp,jpg,tiff,gif,pcx,tga,exif,fpx,svg,psd,cdr,pcd,dxf,ufo,eps,ai,raw,wmf
            return filePath.endsWith("bmp")||filePath.endsWith("jpg")||filePath.endsWith("gif")||
                    filePath.endsWith("jepg")||filePath.endsWith("gif");
        }
        return false;
    }
}
