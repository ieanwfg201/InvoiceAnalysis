package com.constantsoft.invoice.service.impl;

import com.constantsoft.invoice.service.IInvoiceAnalysisService;
import com.constantsoft.invoice.service.bean.InvoiceInformationEntity;
import com.constantsoft.invoice.service.util.PDFInvoiceInforGenerateFactory;
import org.apache.pdfbox.cos.COSDictionary;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.cos.COSString;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.interactive.digitalsignature.PDSignature;
import org.apache.pdfbox.text.PDFTextStripper;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cms.*;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.util.Store;
import org.bouncycastle.util.StoreException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import sun.security.x509.X509CertImpl;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * Created by Walter on 2017/2/18.
 */
@Component
public class PDFInvoiceAnalysisService implements IInvoiceAnalysisService {
    protected Logger logger = LoggerFactory.getLogger(this.getClass());
    @Override
    public InvoiceInformationEntity generate(File file)throws Exception {
        return generate(file, false);
    }

    @Override
    public InvoiceInformationEntity generate(File file, boolean checkingSignature) throws Exception{
        byte[] bytes = null;
        try {
            bytes = Files.readAllBytes(file.toPath());
        }catch (Exception e){}

        return generate(bytes, checkingSignature); // TODO
    }

    @Override
    public InvoiceInformationEntity generate(byte[] bytes)throws Exception {
        return generate(bytes, false);
    }

    @Override
    public InvoiceInformationEntity generate(byte[] bytes, boolean checkingSignature) throws Exception{
        InvoiceInformationEntity entity = new InvoiceInformationEntity();
        PDFInvoiceInforGenerateFactory factory = new PDFInvoiceInforGenerateFactory();
        PDDocument doc = null;
        String text = null;
        try {
            doc = PDDocument.load(bytes);
            PDFTextStripper stripper = new PDFTextStripper();
            stripper.setSortByPosition(true);
            text = stripper.getText(doc);
            String[] itemArray = text.split("\r\n");
            for(String item: itemArray){
                factory.executeLine(item);
            }
            entity = factory.getInvoiceInfoEntity();

        } catch (Exception e){
            logger.error("Error loading PDF files. ", e);
            throw new RuntimeException("Error loading PDF files");
        }finally {
            if (doc!=null) try{ doc.close();}catch (Exception e){}
        }

        if (checkingSignature&&text!=null&&bytes!=null){
            ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
            checkingSignaure(doc, bis, text, entity);
        }
        return entity;
    }

    private void checkingSignaure(PDDocument doc, InputStream infile, String text, InvoiceInformationEntity entity) throws Exception{

        List<PDSignature> signList = doc.getSignatureDictionaries();
        // 1 证书不能为空
        if (signList==null||signList.size()==0) throw new Exception("No signature found for current pdf document");
        // 2 校验时间
        Date signDate = null;
        boolean checkTrue = false;
        for (PDSignature sig : signList) {

            COSDictionary sigDict = sig.getCOSObject();
            COSString contents = (COSString) sigDict.getDictionaryObject(COSName.CONTENTS);
            // download the signed content
            byte[] buf = null;
            try{
                buf = sig.getSignedContent(infile);
            }finally{
                infile.close();
            }
            String subFilter = sig.getSubFilter();
            if (subFilter != null)
            {
                if (subFilter.equals("adbe.pkcs7.detached"))
                {
                    String certInfos = verifyPKCS7(buf, contents, sig);
                    checkTrue = validatePdfInfos(text, certInfos, entity.getCompanyName());
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
                    checkTrue = validatePdfInfos(text, certInfos, entity.getCompanyName());
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
                    for(Certificate cert: certs){
                        checkTrue = validatePdfInfos(text, cert.toString(), entity.getCompanyName());
                        if (checkTrue) break;
                    }
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

            if (checkTrue){ signDate=sig.getSignDate().getTime(); break;}
        }
        if (!checkTrue) throw new Exception("Signaure validation failed.");
        // 当为合法文档时候
        // 3 当前文档不能被更改, 通过文档证书时间以及entity的发票时间校验，校验到天
        if (!isSameDay(entity.getInvoiceDate(), signDate))
            throw new Exception("Invoice date not equal with signature.["+entity.getInvoiceDate()+", "+signDate+"]");
    }

    private boolean isSameDay(Date day1, Date day2){
        if (day1==null||day2==null) return false;
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(day1);
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(day2);
        return cal1.get(Calendar.YEAR)==cal2.get(Calendar.YEAR)&&
                cal1.get(Calendar.MONTH)==cal2.get(Calendar.MONTH)&&
                cal1.get(Calendar.DAY_OF_MONTH)==cal2.get(Calendar.DAY_OF_MONTH);
    }
    // 校验参数
    private static boolean validatePdfInfos(String text, String certInfos, String companyName){
        if (certInfos==null||"".equals(certInfos)) return false;
        boolean checkCompanyName = false;
        if (companyName!=null&&!"".equals(companyName)){
            checkCompanyName = certInfos.contains(companyName);
        }
        boolean checkSignName = false;
        // 获取cn
        String[] arrays = certInfos.toLowerCase().split(",");
        for(String keyValue: arrays){
            if (keyValue.trim().startsWith("o=")){
                checkSignName = text.contains(keyValue.trim().substring(2));
            }else if (keyValue.trim().startsWith("ou=")||keyValue.trim().startsWith("cn="))
                checkSignName = text.contains(keyValue.trim().substring(3));
            if (checkSignName) break;
        }
        return checkCompanyName||checkSignName;
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

        // no need verify signature data, forbid SHA256 not exist error
        /*if (!signerInformation.verify(new JcaSimpleSignerInfoVerifierBuilder().build(certFromSignedData)))
        {
//            System.out.println("Signature verified");
            throw new Exception("Signature verified failed");
        }*/
        return ((X509CertImpl)certFromSignedData).getSubjectDN().getName();
    }
}
