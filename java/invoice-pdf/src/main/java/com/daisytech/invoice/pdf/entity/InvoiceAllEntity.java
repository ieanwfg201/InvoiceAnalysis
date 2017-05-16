package com.daisytech.invoice.pdf.entity;

public class InvoiceAllEntity extends BaseEntity{
    private String text;
    private String invoiceCode = "";   // 长度为8位的数字，前缀可能为：发票号码
    private String invoiceNumber ="";  // 长为10/12位的数字，前缀可能为：发票代码
    private String invoiceDateStr;      // 格式为: YYYY年MM月dd日，前缀可能为：开票日期
    private String checkingCode;        // 校验码：每5位数字为一组，共计4组20个数字，各组以" "分开，前缀可能为：校验码
    private String machineCode;         // 机器代码：12位数字，前缀可能为：机器代码
    private double amount;              // 金额，格式为：￥10.00(计最大的值)
    private String companyName;         // 纳税企业名称

    private boolean checkSignature;
    private int checkSignatureCode = 0;
    private String checkSignatureMessage = "";

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
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

    public String getInvoiceDateStr() {
        return invoiceDateStr;
    }

    public void setInvoiceDateStr(String invoiceDateStr) {
        this.invoiceDateStr = invoiceDateStr;
    }

    public String getCheckingCode() {
        return checkingCode;
    }

    public void setCheckingCode(String checkingCode) {
        this.checkingCode = checkingCode;
    }

    public String getMachineCode() {
        return machineCode;
    }

    public void setMachineCode(String machineCode) {
        this.machineCode = machineCode;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public boolean isCheckSignature() {
        return checkSignature;
    }

    public void setCheckSignature(boolean checkSignature) {
        this.checkSignature = checkSignature;
    }

    public int getCheckSignatureCode() {
        return checkSignatureCode;
    }

    public void setCheckSignatureCode(int checkSignatureCode) {
        this.checkSignatureCode = checkSignatureCode;
    }

    public String getCheckSignatureMessage() {
        return checkSignatureMessage;
    }

    public void setCheckSignatureMessage(String checkSignatureMessage) {
        this.checkSignatureMessage = checkSignatureMessage;
    }
}
