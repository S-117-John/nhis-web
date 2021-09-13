package com.zebone.nhis.compay.pub.vo;

public class HisInsuResVo {

    /**
     * 交易状态码
     * 0    成功
     * -1   失败
     */
    private String infcode;

    /**
     * 错误信息
     */
    private String errMsg;

    /**
     * 医保入参json参数
     */
    private String reqJsonStr;

    /**
     * 医保返回json参数
     */
    private String resJsonStr;

    /**
     * 医保返回json参数转为实体类
     */
    private NationalResVo resVo;

    /**
     * 医保日志
     */
    private String insuLog;

    public String getReqJsonStr() {
        return reqJsonStr;
    }

    public void setReqJsonStr(String reqJsonStr) {
        this.reqJsonStr = reqJsonStr;
    }

    public String getInsuLog() {
        return insuLog;
    }

    public void setInsuLog(String insuLog) {
        this.insuLog = insuLog;
    }

    public NationalResVo getResVo() {
        return resVo;
    }

    public void setResVo(NationalResVo resVo) {
        this.resVo = resVo;
    }

    public String getInfcode() {
        return infcode;
    }

    public void setInfcode(String infcode) {
        this.infcode = infcode;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    public String getResJsonStr() {
        return resJsonStr;
    }

    public void setResJsonStr(String resJsonStr) {
        this.resJsonStr = resJsonStr;
    }
}
