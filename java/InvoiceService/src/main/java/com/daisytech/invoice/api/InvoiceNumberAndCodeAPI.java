package com.daisytech.invoice.api;

import com.daisytech.invoice.Application;
import com.daisytech.invoice.api.vo.InvoiceAllResponseVO;
import com.daisytech.invoice.api.vo.InvoiceCodeAndNumberResponseVO;
import com.daisytech.invoice.generator.InvoiceGenerator;
import com.daisytech.invoice.service.IInvoiceAnalysisService;
import com.daisytech.invoice.service.bean.InvoiceInformationEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;

@RestController
public class InvoiceNumberAndCodeAPI {
    private static final int CODE_SUCCESS = 0;
    private static final int CODE_PARAMETER_ERROR = 1;
    private static final int CODE_SYSTEM_ERROR = 2;

    @Autowired
    private IInvoiceAnalysisService service ;

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

    @RequestMapping(value = "/api/invoice/pdf/analysis", method = RequestMethod.POST, consumes = "multipart/form-data")
     public InvoiceCodeAndNumberResponseVO queryInvoiceByPdf(@RequestParam("pdfFile") MultipartFile file,
                                                             @RequestParam("checkSign") Boolean checkSign){
        InvoiceCodeAndNumberResponseVO res = new InvoiceCodeAndNumberResponseVO();
        try {
            checkSign = (checkSign==null?false:checkSign);
            InvoiceInformationEntity entity = service.generate(file.getBytes(), checkSign);
            if (entity!=null&&entity.getInvoiceNumber()!=null) res.setInvoiceNumber(entity.getInvoiceNumber());
            if (entity!=null&&entity.getInvoiceCode()!=null) res.setInvoiceCode(entity.getInvoiceCode());
            if (entity!=null) res.setCheckingCode(entity.getCheckingCode());
            if (entity!=null) res.setInvoiceDate(entity.getInvoiceDate());
        }catch (Exception e){
            res.setResultCode(CODE_SYSTEM_ERROR);
            res.setErrorMessage("Error message: "+e.getMessage());
        }
        return res;
    }

    @RequestMapping(value = "/api/invoice/pdf/analysis")
    public InvoiceCodeAndNumberResponseVO queryInvoiceByPath(String file, Boolean checkSign){
        InvoiceCodeAndNumberResponseVO res = new InvoiceCodeAndNumberResponseVO();
        try {
            checkSign = (checkSign==null?false:checkSign);
            InvoiceInformationEntity entity = service.generate(new File(file), checkSign);
            if (entity!=null&&entity.getInvoiceNumber()!=null) res.setInvoiceNumber(entity.getInvoiceNumber());
            if (entity!=null&&entity.getInvoiceCode()!=null) res.setInvoiceCode(entity.getInvoiceCode());
            if (entity!=null) res.setCheckingCode(entity.getCheckingCode());
            if (entity!=null) res.setInvoiceDate(entity.getInvoiceDate());
        }catch (Exception e){
            res.setResultCode(CODE_SYSTEM_ERROR);
            res.setErrorMessage("Error message: "+e.getMessage());
        }
        return res;
    }

    @RequestMapping(value = "/api/invoice/pdf/analysis/all", method = RequestMethod.POST, consumes = "multipart/form-data")
    public InvoiceAllResponseVO queryInvoiceAllByPdf(@RequestParam("pdfFile") MultipartFile file,
                                                     @RequestParam("checkSign") Boolean checkSign){
        InvoiceAllResponseVO res = new InvoiceAllResponseVO();
        try {
            checkSign = (checkSign==null?false:checkSign);
            res = service.generateAll(file.getBytes(), checkSign);
        }catch (Exception e){
            res.setResultCode(CODE_SYSTEM_ERROR);
            res.setErrorMessage("Error message: "+e.getMessage());
        }
        return res;
    }
    @RequestMapping(value = "/api/invoice/pdf/analysis/all")
    public InvoiceAllResponseVO queryInvoiceAllByPath(String file, Boolean checkSign){
        InvoiceAllResponseVO res = new InvoiceAllResponseVO();
        try {
            checkSign = (checkSign==null?false:checkSign);
            res = service.generateAll(Files.readAllBytes(new File(file).toPath()), checkSign);
        }catch (Exception e){
            res.setResultCode(CODE_SYSTEM_ERROR);
            res.setErrorMessage("Error message: "+e.getMessage());
        }
        return res;
    }
}
