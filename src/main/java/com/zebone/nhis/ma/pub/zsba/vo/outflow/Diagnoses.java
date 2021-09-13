package com.zebone.nhis.ma.pub.zsba.vo.outflow;

/**
 * 诊断信息
 */
public class Diagnoses {

    private String masterFlag;
    private String icdType;
    private String icdCode;
    private String icdName;

    public void setMasterFlag(String masterFlag) {
        this.masterFlag = masterFlag;
    }

    public String getMasterFlag() {
        return masterFlag;
    }

    public void setIcdType(String icdType) {
        this.icdType = icdType;
    }

    public String getIcdType() {
        return icdType;
    }

    public void setIcdCode(String icdCode) {
        this.icdCode = icdCode;
    }

    public String getIcdCode() {
        return icdCode;
    }

    public void setIcdName(String icdName) {
        this.icdName = icdName;
    }

    public String getIcdName() {
        return icdName;
    }

}