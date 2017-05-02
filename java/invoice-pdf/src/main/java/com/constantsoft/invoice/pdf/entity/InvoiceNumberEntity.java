package com.constantsoft.invoice.pdf.entity;

public class InvoiceNumberEntity extends BaseEntity{
    private String invoiceNumber;
    public InvoiceNumberEntity(int errorCode, String errorMessage, String invoiceNumber){
        setErrorCode(errorCode);
        setErrorMessage(errorMessage);
        setInvoiceNumber(invoiceNumber);
    }
    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }
}
