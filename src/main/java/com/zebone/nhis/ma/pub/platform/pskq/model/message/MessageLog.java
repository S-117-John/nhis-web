package com.zebone.nhis.ma.pub.platform.pskq.model.message;

public class MessageLog {

    private String msgId;

    private String transType;

    private String msgType;

    private String msgContent;

    private String sysCode;

    private String msgStatus;

    private String errTxt;

    public String getErrTxt() {
        return errTxt;
    }

    public void setErrTxt(String errTxt) {
        this.errTxt = errTxt;
    }

    public String getTransType() {
        return transType;
    }

    public void setTransType(String transType) {
        this.transType = transType;
    }

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public String getMsgContent() {
        return msgContent;
    }

    public void setMsgContent(String msgContent) {
        this.msgContent = msgContent;
    }

    public String getSysCode() {
        return sysCode;
    }

    public void setSysCode(String sysCode) {
        this.sysCode = sysCode;
    }

    public String getMsgStatus() {
        return msgStatus;
    }

    public void setMsgStatus(String msgStatus) {
        this.msgStatus = msgStatus;
    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }



}
