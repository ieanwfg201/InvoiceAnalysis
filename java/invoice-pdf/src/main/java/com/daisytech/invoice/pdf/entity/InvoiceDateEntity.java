package com.daisytech.invoice.pdf.entity;

public class InvoiceDateEntity extends BaseEntity {
    private String invoiceDateStr;
    public InvoiceDateEntity(int errorCode, String errorMessage, String invoiceDateStr){
        setErrorCode(errorCode);
        setErrorMessage(errorMessage);
        setInvoiceDateStr(invoiceDateStr);
    }
    public String getInvoiceDateStr() {
        return invoiceDateStr;
    }

    public void setInvoiceDateStr(String invoiceDateStr) {
        this.invoiceDateStr = invoiceDateStr;
    }
}
