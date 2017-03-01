package com.constantsoft.invoice.pdf;

import com.constantsoft.invoice.pdf.entity.InvoiceInfosEntity;
import com.constantsoft.invoice.pdf.exception.PDFAnalysisException;
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
import sun.security.x509.X509CertImpl;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
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
 * Created by walter.xu on 2017/2/28.
 * PDF invoice generator: generate pdf format invoice information
 */
public final class PDFInvoiceGenerator {
    public InvoiceInfosEntity generate(File file) throws PDFAnalysisException{
        return generate(file, false);
    }
    public InvoiceInfosEntity generate(File file, boolean checkingSignature) throws PDFAnalysisException {
        byte[] bytes = null;
        try {
            bytes = Files.readAllBytes(file.toPath());
        }catch (Exception e){}

        return generate(bytes, checkingSignature);
    }
    public InvoiceInfosEntity generate(byte[] bytes) throws PDFAnalysisException{
        return generate(bytes, false);
    }
    /**
     * @param bytes pdf file bytes
     * @param checkingSignature if need check signature.
     * @return invoice infos
     * @throws PDFAnalysisException
     */
    public InvoiceInfosEntity generate(byte[] bytes, boolean checkingSignature) throws PDFAnalysisException {
        InvoiceInfosEntity entity = null;
        PDDocument doc = null;
        try {
            doc = PDDocument.load(bytes);
            // 获取pdf发票信息
            entity = generateInvoiceInfosFromPdfBytes(doc);
            // 校验获取的参数是否合法
            checkInvoiceInfosValid(entity);
            // 需要时校验证书
            if (checkingSignature) checkingSignature(doc, bytes, entity);
        } catch (PDFAnalysisException e) {
            throw e;
        } catch (Exception e) {
            throw new PDFAnalysisException("Failed to analysis pdf file", e);
        } finally {
            if (doc != null) try {
                doc.close();
            } catch (Exception e) {
            }
        }
        return entity;
    }

    /**
     * 根据pdf文档获取到对应的相关发票信息
     * 不关闭pdf流
     * @param doc pdf document
     * @return invioce infos
     */
    private InvoiceInfosEntity generateInvoiceInfosFromPdfBytes(PDDocument doc) throws IOException {
        PDFTextStripper stripper = new PDFTextStripper();
        stripper.setSortByPosition(true);
        String text = stripper.getText(doc);
        String[] itemArray = text.split("\r\n");
        PDFInvoiceInforGenerateFactory factory = new PDFInvoiceInforGenerateFactory();
        for (String item : itemArray) {
            factory.executeLine(item);
        }
        InvoiceInfosEntity entity = factory.getInvoiceInfoEntity();
        entity.setText(text);
        return entity;
    }

    // check if invoice information is valid
    private void checkInvoiceInfosValid(InvoiceInfosEntity entity) throws PDFAnalysisException {
        String error= null;
        if (entity==null) error="Failed to analysis PDF file";
    }

    private void checkingSignature(PDDocument doc, byte[] bytes, InvoiceInfosEntity entity)
            throws PDFAnalysisException, IOException, Exception {
        List<PDSignature> signList = doc.getSignatureDictionaries();
        // 1 证书不能为空
        if (signList == null || signList.size() == 0)
            throw new PDFAnalysisException("No signature found for current pdf document");
        // 2 校验时间
        Date signDate = null;
        boolean checkTrue = false;
        for (PDSignature sig : signList) {

            COSDictionary sigDict = sig.getCOSObject();
            COSString contents = (COSString) sigDict.getDictionaryObject(COSName.CONTENTS);
            // download the signed content
            byte[] buf = sig.getSignedContent(bytes);

            String subFilter = sig.getSubFilter();
            if (subFilter != null) {
                if (subFilter.equals("adbe.pkcs7.detached")) {
                    String certInfos = verifyPKCS7(buf, contents, sig);
                    checkTrue = validatePdfInfos(certInfos, entity);
                    //TODO check certificate chain, revocation lists, timestamp...
                } else if (subFilter.equals("adbe.pkcs7.sha1")) {
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
                    checkTrue = validatePdfInfos(certInfos, entity);
                    //TODO check certificate chain, revocation lists, timestamp...
                } else if (subFilter.equals("adbe.x509.rsa_sha1")) {
                    // example: PDFBOX-2693.pdf
                    COSString certString = (COSString) sigDict.getDictionaryObject(
                            COSName.getPDFName("Cert"));
                    byte[] certData = certString.getBytes();
                    CertificateFactory factory = CertificateFactory.getInstance("X.509");
                    ByteArrayInputStream certStream = new ByteArrayInputStream(certData);
                    Collection<? extends Certificate> certs = factory.generateCertificates(certStream);
//                    System.out.println("certs=" + certs);
                    for (Certificate cert : certs) {
                        checkTrue = validatePdfInfos(cert.toString(), entity);
                        if (checkTrue) break;
                    }
                    //TODO verify signature, do other function to check.
                } else {
//                    System.err.println("Unknown certificate type: " + subFilter);
                    throw new PDFAnalysisException("Validate failed, unknown certificate type: " + subFilter);
                }

            } else {
//                throw new IOException("Missing subfilter for cert dictionary");
                throw new PDFAnalysisException("Missing subfiler for cert dictionary");
            }

            if (checkTrue) {
                signDate = sig.getSignDate().getTime();
                break;
            }
        }
        if (!checkTrue) throw new PDFAnalysisException("Signaure validation failed.");
        // 当为合法文档时候
        // 3 当前文档不能被更改, 通过文档证书时间以及entity的发票时间校验，校验到天
        if (!isSameDay(entity.getInvoiceDate(), signDate))
            throw new PDFAnalysisException("Invoice date not equal with signature.[" + entity.getInvoiceDate() + ", " + signDate + "]");
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
    private static boolean validatePdfInfos(String certInfos, InvoiceInfosEntity entity){
        if (certInfos==null||"".equals(certInfos)) return false;
        boolean checkCompanyName = false;
        if (entity.getCompanyName()!=null&&!"".equals(entity.getCompanyName())){
            checkCompanyName = certInfos.contains(entity.getCompanyName());
        }
        boolean checkSignName = false;
        // 获取cn
        String[] arrays = certInfos.toLowerCase().split(",");
        for(String keyValue: arrays){
            if (keyValue.trim().startsWith("o=")){
                checkSignName = entity.getText().contains(keyValue.trim().substring(2));
            }else if (keyValue.trim().startsWith("ou=")||keyValue.trim().startsWith("cn="))
                checkSignName = entity.getText().contains(keyValue.trim().substring(3));
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
