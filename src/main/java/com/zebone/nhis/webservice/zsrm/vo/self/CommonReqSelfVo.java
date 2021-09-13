package com.zebone.nhis.webservice.zsrm.vo.self;

/**
 * 公共请求参数
 */
public class CommonReqSelfVo{

    /**
     * 请求ID
     */
    private String reqId;

    /**
     * 请求时间
     */
    private String reqDate;

    /**
     * 患者编码
     */
    private String codePi;

    /**
     * 患者就诊pv
     */
    private String codePv;

    /**
     * 门诊号
     */
    private String codeOp;

    public String getReqId() {
        return reqId;
    }

    public void setReqId(String reqId) {
        this.reqId = reqId;
    }

    public String getReqDate() {
        return reqDate;
    }

    public void setReqDate(String reqDate) {
        this.reqDate = reqDate;
    }

    public String getCodePi() {
        return codePi;
    }

    public void setCodePi(String codePi) {
        this.codePi = codePi;
    }

    public String getCodePv() {
        return codePv;
    }

    public void setCodePv(String codePv) {
        this.codePv = codePv;
    }

    public String getCodeOp() {
        return codeOp;
    }

    public void setCodeOp(String codeOp) {
        this.codeOp = codeOp;
    }
}
