package com.daisytech.invoice.api.vo;

/**
 * Created by walter.xu on 2016/12/30.
 */
public class InvoiceAllResponseVO extends InvoiceCodeAndNumberResponseVO{
    private boolean checkSign;
    private int checkSignErrorCode;
    private String checkSIgnErrorMessage;

    public boolean isCheckSign() {
        return checkSign;
    }

    public void setCheckSign(boolean checkSign) {
        this.checkSign = checkSign;
    }

    public int getCheckSignErrorCode() {
        return checkSignErrorCode;
    }

    public void setCheckSignErrorCode(int checkSignErrorCode) {
        this.checkSignErrorCode = checkSignErrorCode;
    }

    public String getCheckSIgnErrorMessage() {
        return checkSIgnErrorMessage;
    }

    public void setCheckSIgnErrorMessage(String checkSIgnErrorMessage) {
        this.checkSIgnErrorMessage = checkSIgnErrorMessage;
    }
}
