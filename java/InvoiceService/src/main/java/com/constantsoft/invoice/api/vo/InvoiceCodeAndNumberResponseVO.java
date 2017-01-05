package com.constantsoft.invoice.api.vo;

/**
 * Created by walter.xu on 2016/12/30.
 */
public class InvoiceCodeAndNumberResponseVO extends ResponseVO{
    private String invoiceCode = "-";
    private String invoiceNumber = "-";

    public InvoiceCodeAndNumberResponseVO(){}
    public InvoiceCodeAndNumberResponseVO(int resultCode, String message) {
        super(resultCode, message);
    }

    public String getInvoiceCode() {
        return invoiceCode;
    }

    public void setInvoiceCode(String invoiceCode) {
        this.invoiceCode = invoiceCode;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }
}
