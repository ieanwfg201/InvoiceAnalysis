package com.constantsoft.invoice.api.vo;

/**
 * Created by walter.xu on 2016/12/30.
 */
public class ResponseVO {
    private int resultCode = 0;
    private String errorMessage ;
    public ResponseVO(){}
    public ResponseVO(int resultCode, String message){
        this.resultCode = resultCode;
        this.errorMessage = message;
    }

    public int getResultCode() {
        return resultCode;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
