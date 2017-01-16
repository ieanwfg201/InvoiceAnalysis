package com.constantsoft.invoice.api;

import com.constantsoft.invoice.Application;
import com.constantsoft.invoice.api.vo.InvoiceCodeAndNumberResponseVO;
import com.constantsoft.invoice.generator.InvoiceGenerator;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by walter.xu on 2016/12/30.
 */
@RestController
public class InvoiceNumberAndCodeAPI {
    private static final int CODE_SUCCESS = 0;
    private static final int CODE_PARAMETER_ERROR = 1;
    private static final int CODE_SYSTEM_ERROR = 2;

    @RequestMapping("/invoice/code/number")
    public InvoiceCodeAndNumberResponseVO queryInvoice(String filePath, Boolean pdf, Boolean qrcode, Boolean ocr,
                                                       Double xStart, Double yStart, Double xEnd, Double yEnd, Boolean isRatate){
        if (filePath==null||filePath.trim().equals("")) return new InvoiceCodeAndNumberResponseVO(CODE_PARAMETER_ERROR, "file path is null");
        if (pdf==null) pdf =false;
        if (qrcode==null) qrcode =false;
        if (ocr == null) ocr = false;
        if (xStart == null) xStart = 0.0;
        if (yStart == null) yStart = 0.0;
        if (xEnd == null) xEnd = 0.0;
        if (yEnd == null) yEnd = 0.0;
        if (isRatate == null) isRatate = false;
        InvoiceCodeAndNumberResponseVO res = new InvoiceCodeAndNumberResponseVO();
        try {
            String[] arrays = InvoiceGenerator.instance(Application.getDataPath()).generateInvoiceCodeAndNumber(filePath,pdf,qrcode,ocr,xStart,yStart,xEnd,yEnd,isRatate);
            if (arrays!=null&&arrays.length==2){
                res.setInvoiceCode(arrays[0]);
                res.setInvoiceNumber(arrays[1]);
            }
        }catch (Exception e){
            res.setResultCode(CODE_SYSTEM_ERROR);
            res.setErrorMessage("Error message: "+e.getMessage());
        }
        return res;
    }
}
