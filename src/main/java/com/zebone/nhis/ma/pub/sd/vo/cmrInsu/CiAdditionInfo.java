package com.zebone.nhis.ma.pub.sd.vo.cmrInsu;

public class CiAdditionInfo {

    /**
     * 错误代码
     */
    private String errorCode;

    /**
     * 错误提示
     */
    private String errorMsg;

    /**
     * 接收方交易流水号
     */
    private String receiverTradeNum;

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public String getReceiverTradeNum() {
        return receiverTradeNum;
    }

    public void setReceiverTradeNum(String receiverTradeNum) {
        this.receiverTradeNum = receiverTradeNum;
    }
}
