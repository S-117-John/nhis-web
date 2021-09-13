package com.zebone.nhis.compay.pub.vo;

import java.util.Map;

public class NationalResVo {

    /**
     *交易状态码
     */
    private String infcode;

    /**
     *接收方报文ID
     */
    private String infRefmsgid;

    /**
     *接收报文时间
     */
    private String refmsgTime;

    /**
     *响应报文时间
     */
    private String respondTime;

    /**
     *错误信息
     */
    private String errMsg;

    /**
     *交易输出
     */
    private Object output;

    public String getInfcode() {
        return infcode;
    }

    public void setInfcode(String infcode) {
        this.infcode = infcode;
    }

    public String getInfRefmsgid() {
        return infRefmsgid;
    }

    public void setInfRefmsgid(String infRefmsgid) {
        this.infRefmsgid = infRefmsgid;
    }

    public String getRefmsgTime() {
        return refmsgTime;
    }

    public void setRefmsgTime(String refmsgTime) {
        this.refmsgTime = refmsgTime;
    }

    public String getRespondTime() {
        return respondTime;
    }

    public void setRespondTime(String respondTime) {
        this.respondTime = respondTime;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    public Object getOutput() {
        return output;
    }

    public void setOutput(Object output) {
        this.output = output;
    }
}
