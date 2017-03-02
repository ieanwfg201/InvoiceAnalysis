package com.constantsoft.invoice.praser;

import org.apache.pdfbox.cos.COSDictionary;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.cos.COSString;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.interactive.digitalsignature.PDSignature;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.pdfbox.text.PDFTextStripper;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cms.*;
import org.bouncycastle.cms.jcajce.JcaSimpleSignerInfoVerifierBuilder;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.util.Store;
import org.bouncycastle.util.StoreException;
import sun.security.x509.X509CertImpl;

import java.awt.image.BufferedImage;
import java.io.*;
import java.security.MessageDigest;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.List;

/**
 * Created by walter.xu on 2016/12/12.
 */
public class PdfTextPraser {
    public static String[] praseNumberAndCode(File file, boolean checkingSignature) throws Exception{
        String[] billCodeAndNumberArray = new String[]{"-","-"};
        if (file==null||!file.exists()||file.isDirectory()) return billCodeAndNumberArray;
        PDDocument doc = null;
        InputStream is = null;
        try {
            is = new FileInputStream(file);
            doc = PDDocument.load(file);
            billCodeAndNumberArray = praseNumberAndCode(doc, is, checkingSignature);
        } finally {
            if (doc!=null) doc.close();
            if (is!=null) is.close();
        }
        return billCodeAndNumberArray;
    }
    public static String[] praseNumberAndCode(byte[] bytes, boolean checkingSignature) throws Exception{
        String[] billCodeAndNumberArray = new String[]{"-","-"};
        if (bytes==null) return billCodeAndNumberArray;
        PDDocument doc = null;
        try {
            doc = PDDocument.load(bytes);
            InputStream is = new ByteArrayInputStream(bytes);
            billCodeAndNumberArray = praseNumberAndCode(doc, is, checkingSignature);
        } finally {
            if (doc!=null) doc.close();
        }
        return billCodeAndNumberArray;
    }



    public static String[] praseNumberAndCode(PDDocument doc, InputStream file,  boolean checkSignature) throws Exception{
        if (doc == null) return null;
        String[] billCodeAndNumberArray = new String[]{"-","-"};
        PDFTextStripper stripper = new PDFTextStripper();
        stripper.setSortByPosition(true);
        String text = stripper.getText(doc);


        String date = null;

        String[] itemArray = text.split("\r\n");
        for(String item: itemArray){
            item = item.trim();
            String code = findNextStringByPattern(item, "发票代码", -1, null);
            if (code!=null&&!"".equals(code)) billCodeAndNumberArray[0] = code;
            String number = findNextStringByPattern(item, "发票号码", -1, null);
            if (number!=null&&!"".equals(number)) billCodeAndNumberArray[1] = number;
            // 日期
            if (date==null&&item.contains("日期")){
                String dateInfo = item.substring(item.indexOf("日期")+2);
                date = dateInfo.replaceAll(":","").replaceAll("：", "").replaceAll("年","")
                        .replaceAll("月", "").replaceAll("日", "");
            }
        }
        // 是否需要校验签名
        if (checkSignature) checkingSignaure(doc, file, text, date);
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

    public static BufferedImage prasePdfToImage(File file, int dpi) throws Exception{
        if (dpi==0) dpi = 300;
        PDDocument document = null;
        try {
            document = PDDocument.load(file);
            PDFRenderer renderer = new PDFRenderer(document);
            // only return the first page.
            if (document.getNumberOfPages()>0) return renderer.renderImageWithDPI(0, dpi);
        }finally {
            if (document!=null) document.close();
        }
        return null;
    }

    private static void checkingSignaure(PDDocument doc, InputStream infile, String text, String date) throws Exception{
        // 1 当前文档不能被更改
//        if (doc.getCurrentAccessPermission().canModify()) throw new Exception("Current pdf document could not modification");
        List<PDSignature> signList = doc.getSignatureDictionaries();
        // 2 证书不能为空
        if (signList==null||signList.size()==0) throw new Exception("No signature found for current pdf document");
        // 3 校验时间

        boolean checkTrue = false;
        for (PDSignature sig : signList) {
            if (!date.contains(new SimpleDateFormat("yyyyMMdd").format(sig.getSignDate().getTime())))
                throw new Exception("Cannot modify pdf file: signature date is not match");
            COSDictionary sigDict = sig.getCOSObject();
            COSString contents = (COSString) sigDict.getDictionaryObject(COSName.CONTENTS);

            // download the signed content
            byte[] buf = null;
            try
            {
                buf = sig.getSignedContent(infile);
            }
            finally
            {
                infile.close();
            }

            String subFilter = sig.getSubFilter();
            if (subFilter != null)
            {
                if (subFilter.equals("adbe.pkcs7.detached"))
                {
                    String certInfos = verifyPKCS7(buf, contents, sig);
                    checkTrue = validatePdfInfos(text, certInfos);
                    //TODO check certificate chain, revocation lists, timestamp...
                }
                else if (subFilter.equals("adbe.pkcs7.sha1"))
                {
                    // example: PDFBOX-1452.pdf
                    COSString certString = (COSString) sigDict.getDictionaryObject(
                            COSName.CONTENTS);
                    byte[] certData = certString.getBytes();
                    CertificateFactory factory = CertificateFactory.getInstance("X.509");
                    ByteArrayInputStream certStream = new ByteArrayInputStream(certData);
                    Collection<? extends Certificate> certs = factory.generateCertificates(certStream);
                    System.out.println("certs=" + certs);

                    byte[] hash = MessageDigest.getInstance("SHA1").digest(buf);
                    String certInfos = verifyPKCS7(hash, contents, sig);
                    checkTrue = validatePdfInfos(text, certInfos);
                    //TODO check certificate chain, revocation lists, timestamp...
                }
                else if (subFilter.equals("adbe.x509.rsa_sha1"))
                {
                    // example: PDFBOX-2693.pdf
                    COSString certString = (COSString) sigDict.getDictionaryObject(
                            COSName.getPDFName("Cert"));
                    byte[] certData = certString.getBytes();
                    CertificateFactory factory = CertificateFactory.getInstance("X.509");
                    ByteArrayInputStream certStream = new ByteArrayInputStream(certData);
                    Collection<? extends Certificate> certs = factory.generateCertificates(certStream);
//                    System.out.println("certs=" + certs);

                    //TODO verify signature, do other function to check.
                }
                else
                {
//                    System.err.println("Unknown certificate type: " + subFilter);
                    throw new Exception("Validate failed, unknown certificate type: "+subFilter);
                }

            }
            else
            {
//                throw new IOException("Missing subfilter for cert dictionary");
                throw new IOException("Missing subfiler for cert dictionary");
            }

            if (checkTrue) break;
        }
        if (!checkTrue) throw new Exception("Signaure validation failed.");

    }

    private static boolean validatePdfInfos(String text, String certInfos){
        if (certInfos==null||"".equals(certInfos)) return false;
        // 获取cn
        String[] arrays = certInfos.toLowerCase().split(",");
        String companyName = null;
        for(String keyValue: arrays){
            if (keyValue.trim().startsWith("cn"))
                companyName = keyValue.trim().substring(keyValue.trim().indexOf("=") + 1);
        }
        if (companyName==null||!text.contains(companyName)) return false;
        return true;
    }

    /**
     * Verify a PKCS7 signature.
     *
     * @param byteArray the byte sequence that has been signed
     * @param contents the /Contents field as a COSString
     * @param sig the PDF signature (the /V dictionary)
     * @throws CertificateException
     * @throws CMSException
     * @throws StoreException
     * @throws OperatorCreationException
     */
    private static String verifyPKCS7(byte[] byteArray, COSString contents, PDSignature sig)
            throws Exception
    {
        // inspiration:
        // http://stackoverflow.com/a/26702631/535646
        // http://stackoverflow.com/a/9261365/535646
        CMSProcessable signedContent = new CMSProcessableByteArray(byteArray);
        CMSSignedData signedData = new CMSSignedData(signedContent, contents.getBytes());
        Store certificatesStore = signedData.getCertificates();
        Collection<SignerInformation> signers = signedData.getSignerInfos().getSigners();
        SignerInformation signerInformation = signers.iterator().next();
        Collection matches = certificatesStore.getMatches(signerInformation.getSID());
        X509CertificateHolder certificateHolder = (X509CertificateHolder) matches.iterator().next();
        X509Certificate certFromSignedData = new JcaX509CertificateConverter().getCertificate(certificateHolder);
//        System.out.println("certFromSignedData: " + certFromSignedData);
        certFromSignedData.checkValidity(sig.getSignDate().getTime());

        if (!signerInformation.verify(new JcaSimpleSignerInfoVerifierBuilder().build(certFromSignedData)))
        {
//            System.out.println("Signature verified");
            throw new Exception("Signature verified failed");
        }
        return ((X509CertImpl)certFromSignedData).getSubjectDN().getName();
    }
}
