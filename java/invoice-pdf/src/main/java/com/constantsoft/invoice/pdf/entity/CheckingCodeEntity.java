package com.constantsoft.invoice.pdf.entity;

public class CheckingCodeEntity extends BaseEntity{
    private String checkingCode;
    public CheckingCodeEntity(int errorCode, String errorMessage, String checkingCode){
        setErrorCode(errorCode);
        setErrorMessage(errorMessage);
        setCheckingCode(checkingCode);
    }
    public String getCheckingCode() {
        return checkingCode;
    }

    public void setCheckingCode(String checkingCode) {
        this.checkingCode = checkingCode;
    }
}
