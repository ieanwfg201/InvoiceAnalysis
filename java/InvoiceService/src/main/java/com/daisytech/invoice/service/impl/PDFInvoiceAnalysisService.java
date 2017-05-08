package com.daisytech.invoice.service.impl;

import com.daisytech.invoice.api.vo.InvoiceAllResponseVO;
import com.daisytech.invoice.pdf.PDFInvoiceGenerator;
import com.daisytech.invoice.pdf.entity.InvoiceAllEntity;
import com.daisytech.invoice.pdf.entity.InvoiceInfosEntity;
import com.daisytech.invoice.service.IInvoiceAnalysisService;
import com.daisytech.invoice.service.bean.InvoiceInformationEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.File;
import java.nio.file.Files;

/**
 * Created by Walter on 2017/2/18.
 */
@Component
public class PDFInvoiceAnalysisService implements IInvoiceAnalysisService {
    protected Logger logger = LoggerFactory.getLogger(this.getClass());
    private PDFInvoiceGenerator generator = new PDFInvoiceGenerator(true);
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
        InvoiceInfosEntity res = generator.generate(bytes, checkingSignature);
        return trans(res);
    }

    private InvoiceInformationEntity trans(InvoiceInfosEntity res){
        InvoiceInformationEntity entity = new InvoiceInformationEntity();
        if (res!=null){
            entity.setAmount(res.getAmount());
            entity.setCheckingCode(res.getCheckingCode());
            entity.setCompanyName(res.getCompanyName());
            entity.setInvoiceCode(res.getInvoiceCode());
            entity.setInvoiceNumber(res.getInvoiceNumber());
            entity.setMachineCode(res.getMachineCode());
            entity.setInvoiceDate(res.getInvoiceDateStr());
        }
        return entity;
    }

    @Override
    public InvoiceAllResponseVO generateAll(byte[] bytes, boolean checkingSignature) throws Exception{
        InvoiceAllResponseVO entity = new InvoiceAllResponseVO();
        InvoiceAllEntity res = generator.generateAll(bytes, checkingSignature);
        return trans(res);
    }
    private InvoiceAllResponseVO trans(InvoiceAllEntity res){
        InvoiceAllResponseVO entity = new InvoiceAllResponseVO();
        if (res!=null){
            entity.setResultCode(res.getErrorCode());
            entity.setErrorMessage(res.getErrorMessage());
            entity.setInvoiceCode(res.getInvoiceCode());
            entity.setInvoiceNumber(res.getInvoiceNumber());
            entity.setInvoiceDate(res.getInvoiceDateStr());
            entity.setCheckingCode(res.getCheckingCode());
            entity.setCheckSign(res.isCheckSignature());
            entity.setCheckSignErrorCode(res.getCheckSignatureCode());
            entity.setCheckSIgnErrorMessage(res.getCheckSignatureMessage());
        }
        return entity;
    }
}
