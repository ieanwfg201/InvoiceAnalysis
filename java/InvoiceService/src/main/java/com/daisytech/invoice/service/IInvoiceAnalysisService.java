package com.daisytech.invoice.service;

import com.daisytech.invoice.api.vo.InvoiceAllResponseVO;
import com.daisytech.invoice.service.bean.InvoiceInformationEntity;

import java.io.File;

/**
 * Created by Walter on 2017/2/18.
 */
public interface IInvoiceAnalysisService {
    InvoiceInformationEntity generate(File file)throws Exception;
    InvoiceInformationEntity generate(File file, boolean checkingSignature)throws Exception;
    InvoiceInformationEntity generate(byte[] bytes)throws Exception;
    InvoiceInformationEntity generate(byte[] bytes, boolean checkingSignature)throws Exception;

    InvoiceAllResponseVO generateAll(byte[] bytes, boolean checkingSignature) throws Exception;
}
