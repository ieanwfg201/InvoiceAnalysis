package com.daisytech.invoice.pdf;

import com.daisytech.invoice.pdf.entity.*;
import com.daisytech.invoice.pdf.exception.PDFAnalysisException;
import org.apache.pdfbox.cos.*;
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

import java.io.*;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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
    private boolean throwException = true;

    public PDFInvoiceGenerator() {
    }

    public PDFInvoiceGenerator(boolean throwException) {
        this.throwException = throwException;
    }

    public InvoiceInfosEntity generate(File file) throws PDFAnalysisException {
        return generate(file, false);
    }

    public InvoiceInfosEntity generate(File file, boolean checkingSignature) throws PDFAnalysisException {
        byte[] bytes = readBytesFromFile(file);
        return generate(bytes, checkingSignature);
    }

    private byte[] readBytesFromFile(File file) {
        /*byte[] bytes = null;
        ByteArrayOutputStream bis;
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
            bis = new ByteArrayOutputStream();
            byte[] temp = new byte[1024];
            while(fis.read(temp, 0 ,temp.length)!=-1){
                bis.write(temp);
            }
            bytes = bis.toByteArray();
        } catch (Exception e) {
        }
        return bytes;*/
        byte[] bytes = null;
        try {
            bytes = Files.readAllBytes(file.toPath());
        } catch (Exception e) {
        }
        return bytes;
    }

    public InvoiceInfosEntity generate(byte[] bytes) throws PDFAnalysisException {
        return generate(bytes, false);
    }

    /************************** 新建方法 ***************************/

    public InvoiceAllEntity generateAll(File file, boolean checkSignature) {
        return generateAll(readBytesFromFile(file), checkSignature);
    }
    public InvoiceAllEntity generateAll(File file, boolean checkSignature, boolean checkSignatureDateValid) {
        return generateAll(readBytesFromFile(file), checkSignature, checkSignatureDateValid);
    }

    public InvoiceCodeEntity getInvoiceCode(byte[] bytes) {
        InvoiceAllEntity entity = generateAll(bytes, false);
        return new InvoiceCodeEntity(entity.getErrorCode(), entity.getErrorMessage(), entity.getInvoiceCode());
    }

    public InvoiceCodeEntity getInvoiceCode(File file) {
        return getInvoiceCode(readBytesFromFile(file));
    }

    public InvoiceNumberEntity getInvoiceNumber(byte[] bytes) {
        InvoiceAllEntity entity = generateAll(bytes, false);
        return new InvoiceNumberEntity(entity.getErrorCode(), entity.getErrorMessage(), entity.getInvoiceNumber());
    }

    public InvoiceNumberEntity getInvoiceNumber(File file) {
        return getInvoiceNumber(readBytesFromFile(file));
    }

    public InvoiceDateEntity getInvoiceDateStr(byte[] bytes) {
        InvoiceAllEntity entity = generateAll(bytes, false);
        return new InvoiceDateEntity(entity.getErrorCode(), entity.getErrorMessage(), entity.getInvoiceDateStr());
    }

    public InvoiceDateEntity getInvoiceDateStr(File file) {
        return getInvoiceDateStr(readBytesFromFile(file));
    }

    public CheckingCodeEntity getCheckingCode(byte[] bytes) {
        InvoiceAllEntity entity = generateAll(bytes, false);
        return new CheckingCodeEntity(entity.getErrorCode(), entity.getErrorMessage(), entity.getCheckingCode());
    }

    public CheckingCodeEntity getCheckingCode(File file) {
        return getCheckingCode(readBytesFromFile(file));
    }

    public BaseEntity checkingSignature(byte[] bytes) {
        return checkingSignature(bytes, false);
    }
    public BaseEntity checkingSignature(byte[] bytes, boolean checkSignatureDateValid) {
        InvoiceAllEntity entity = generateAll(bytes, true, checkSignatureDateValid);
        BaseEntity result = new BaseEntity();
        result.setErrorCode(entity.getCheckSignatureCode());
        result.setErrorMessage(entity.getCheckSignatureMessage());
        return result;
    }
    public BaseEntity checkingSignature(File file) { return  checkingSignature(file, false);}
    public BaseEntity checkingSignature(File file, boolean checkSignatureDateValid) {
        return checkingSignature(readBytesFromFile(file), checkSignatureDateValid);
    }

    /**********************************************************************/

    /**
     * @param bytes             pdf file bytes
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
            if (checkingSignature) {checkingSignature(doc, bytes, entity, true);}
        } catch (PDFAnalysisException e) {
            if (throwException) {throw e;}
            else {entity = new InvoiceInfosEntity(e.getMessage());}
        } catch (Exception e) {
            if (throwException) {throw new PDFAnalysisException(e.getMessage(), e);}
            else
            { entity = new InvoiceInfosEntity(e.getMessage());}
        } finally {
            if (doc != null) {try {
                doc.close();
            } catch (Exception e) {
            }}
        }
        return entity;
    }

    /**
     * generate all invoice data, including signature infos.
     * see {@link #generateAll(byte[], boolean, boolean)}
     *
     * @param bytes          doc bytes
     * @param checkSignature if need checking signature, if true, will checking signature
     * @return invoice infos
     */
    public InvoiceAllEntity generateAll(byte[] bytes, boolean checkSignature) {
        return generateAll(bytes, checkSignature, false);
    }

    /**
     * generate all invoice data, including signature infos.
     * see {@link #generateAll(byte[], boolean, boolean)}
     *
     * @param bytes                   doc bytes
     * @param checkSignature          if need checking signature, if true, will checking signature
     * @param checkSignatureDateValid if checking signature date is valid. only effect when 'checkSignature' is true
     * @return invoice infos
     */
    public InvoiceAllEntity generateAll(byte[] bytes, boolean checkSignature, boolean checkSignatureDateValid) {
        InvoiceAllEntity result = new InvoiceAllEntity();
        PDDocument doc = null;
        try {
            InvoiceInfosEntity entity = null;
            // generate date information
            try {
                doc = PDDocument.load(bytes);
                // 获取pdf发票信息
                entity = generateInvoiceInfosFromPdfBytes(doc);
            } catch (Exception e) {
                entity = new InvoiceInfosEntity(e.getMessage());
            }
            result.setErrorCode(entity.getErrorCode());
            result.setErrorMessage(entity.getErrorMessage());
            result.setText(entity.getText());
            result.setInvoiceCode(entity.getInvoiceCode());
            result.setInvoiceNumber(entity.getInvoiceNumber());
            result.setInvoiceDateStr(entity.getInvoiceDateStr());
            result.setCheckingCode(entity.getCheckingCode());
            result.setMachineCode(entity.getMachineCode());
            result.setAmount(entity.getAmount());
            result.setCompanyName(entity.getCompanyName());
            // checking signature
            try {
                if (checkSignature) {
                    result.setCheckSignature(true);
                    checkingSignature(doc, bytes, entity, checkSignatureDateValid);
                }
            } catch (Exception e) {
                result.setCheckSignatureCode(1);
                result.setCheckSignatureMessage(e.getMessage());
            }
        } finally {
            if (doc != null) {try {
                doc.close();
            } catch (Exception e) {
            }}
        }
        return result;
    }

    /**
     * 根据pdf文档获取到对应的相关发票信息
     * 不关闭pdf流
     *
     * @param doc pdf document
     * @return invioce infos
     */
    private InvoiceInfosEntity generateInvoiceInfosFromPdfBytes(PDDocument doc) throws IOException {
        PDFTextStripper stripper = new PDFTextStripper();
        stripper.setSortByPosition(true);
        String text = stripper.getText(doc);
        String[] itemArray = text.replaceAll("\r\n", "\n").split("\n");
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
        String error = null;
        if (entity == null) {error = "Failed to analysis PDF file";}
    }

    private void checkingSignature(PDDocument doc, byte[] bytes, InvoiceInfosEntity entity, boolean checkSignatureDateValid)
            throws IOException, PDFAnalysisException, CMSException, CertificateException, NoSuchAlgorithmException {
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
                    String certInfos = verifyPKCS7(buf, contents, sig, checkSignatureDateValid);
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
                    String certInfos = verifyPKCS7(hash, contents, sig, checkSignatureDateValid);
                    checkTrue = validatePdfInfos(certInfos, entity);
                    //TODO check certificate chain, revocation lists, timestamp...
                } else if (subFilter.equals("adbe.x509.rsa_sha1")) {
                    // example: PDFBOX-2693.pdf
                    COSBase certBase = sigDict.getDictionaryObject(COSName.getPDFName("Cert"));
                    if (certBase instanceof COSArray){
                        for (COSBase cosBase : ((COSArray) certBase)) {
                            COSString certString = (COSString)((COSObject)(cosBase)).getObject();
                            byte[] certData = certString.getBytes();
                            CertificateFactory factory = CertificateFactory.getInstance("X.509");
                            ByteArrayInputStream certStream = new ByteArrayInputStream(certData);
                            Collection<? extends Certificate> certs = factory.generateCertificates(certStream);
//                    System.out.println("certs=" + certs);
                            for (Certificate cert : certs) {
                                checkTrue = validatePdfInfos(cert.toString(), entity);
                                if (checkTrue) {break;}
                            }
                            if (checkTrue){break;}
                        }

                    }else{
                        COSString certString = (COSString)certBase;
                        byte[] certData = certString.getBytes();
                        CertificateFactory factory = CertificateFactory.getInstance("X.509");
                        ByteArrayInputStream certStream = new ByteArrayInputStream(certData);
                        Collection<? extends Certificate> certs = factory.generateCertificates(certStream);
//                    System.out.println("certs=" + certs);
                        for (Certificate cert : certs) {
                            checkTrue = validatePdfInfos(cert.toString(), entity);
                            if (checkTrue) {break;}
                        }
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
        if (!checkTrue){ throw new PDFAnalysisException("Signaure validation failed.");}
        // 当为合法文档时候
        // 3 当前文档不能被更改, 通过文档证书时间以及entity的发票时间校验，校验到天
        if (!isSameDay(entity.getInvoiceDateStr(), signDate))
        {throw new PDFAnalysisException("Invoice date not equal with signature.[" + entity.getInvoiceDateStr() + ", " + signDate + "]");}
    }

    private boolean isSameDay(String day1, Date day2) {
        Integer day1Int = Integer.valueOf(day1.trim());
        if (day1Int == null || day2 == null){ return false;}
        boolean isSame = true;
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(day2);
        isSame = day1Int % 100 == cal2.get(Calendar.DAY_OF_MONTH);
        day1Int /= 100;
        isSame = day1Int % 100 == (cal2.get(Calendar.MONTH) + 1);
        day1Int /= 100;
        isSame = day1Int == cal2.get(Calendar.YEAR);
        return isSame;
    }

    // 校验参数
    private static boolean validatePdfInfos(String certInfos, InvoiceInfosEntity entity) {
        if (certInfos == null || "".equals(certInfos)){ return false;}
        boolean checkCompanyName = false;
        if (entity.getCompanyName() != null && !"".equals(entity.getCompanyName())) {
            checkCompanyName = certInfos.contains(entity.getCompanyName());
        }
        boolean checkSignName = false;
        // 获取cn
        String[] arrays = certInfos.toLowerCase().split(",");
        for (String keyValue : arrays) {
            if (keyValue.trim().startsWith("o=")) {
                checkSignName = entity.getText().contains(keyValue.trim().substring(2));
            } else if (keyValue.trim().startsWith("ou=") || keyValue.trim().startsWith("cn="))
            {checkSignName = entity.getText().contains(keyValue.trim().substring(3));}
            if (checkSignName) {break;}
        }
        return checkCompanyName || checkSignName;
    }

    /**
     * Verify a PKCS7 signature.
     *
     * @param byteArray the byte sequence that has been signed
     * @param contents  the /Contents field as a COSString
     * @param sig       the PDF signature (the /V dictionary)
     * @throws CertificateException
     * @throws CMSException
     * @throws StoreException
     * @throws OperatorCreationException
     */
    private static String verifyPKCS7(byte[] byteArray, COSString contents, PDSignature sig, boolean checkSignatureDateValid) throws
            CMSException, CertificateException {
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
        if (checkSignatureDateValid) {certFromSignedData.checkValidity(sig.getSignDate().getTime());}

        // no need verify signature data, forbid SHA256 not exist error
        /*if (!signerInformation.verify(new JcaSimpleSignerInfoVerifierBuilder().build(certFromSignedData)))
        {
//            System.out.println("Signature verified");
            throw new Exception("Signature verified failed");
        }*/
        return ((X509CertImpl) certFromSignedData).getSubjectDN().getName();
    }
}
