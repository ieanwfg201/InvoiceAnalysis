package com.constantsoft.invoice.pdf.exception;

/**
 * Created by walter.xu on 2017/2/28.
 */
public class PDFAnalysisException extends Exception {
    public PDFAnalysisException(String msg){
        super(msg);
    }
    public PDFAnalysisException(String msg, Exception e){
        super(msg, e);
    }
}
