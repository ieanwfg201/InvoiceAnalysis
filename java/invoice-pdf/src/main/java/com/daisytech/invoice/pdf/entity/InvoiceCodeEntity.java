package com.daisytech.invoice.pdf.entity;

public class InvoiceCodeEntity extends BaseEntity {
    private String invoiceCode;

    public InvoiceCodeEntity(int errorCode, String errorMessage, String invoiceCode){
        setErrorCode(errorCode);
        setErrorMessage(errorMessage);
        setInvoiceCode(invoiceCode);
    }
    public String getInvoiceCode() {
        return invoiceCode;
    }

    public void setInvoiceCode(String invoiceCode) {
        this.invoiceCode = invoiceCode;
    }
}
