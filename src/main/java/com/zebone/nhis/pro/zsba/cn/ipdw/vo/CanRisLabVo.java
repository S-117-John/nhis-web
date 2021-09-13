package com.zebone.nhis.pro.zsba.cn.ipdw.vo;

public class CanRisLabVo {
    /**医嘱主键**/
    private String pkCnord;
    /**医嘱名称*/
    private String nameOrd;
    /**医嘱状态*/
    private String euStatusOrd;
    /**申请单状态*/
    private String appStatus;
    /**执行单状态*/
    private String occStatus;

    /**执行科室*/
    private String nameDept;

    public String getPkCnord() {
        return pkCnord;
    }

    public void setPkCnord(String pkCnord) {
        this.pkCnord = pkCnord;
    }

    public String getNameOrd() {
        return nameOrd;
    }

    public void setNameOrd(String nameOrd) {
        this.nameOrd = nameOrd;
    }

    public String getEuStatusOrd() {
        return euStatusOrd;
    }

    public void setEuStatusOrd(String euStatusOrd) {
        this.euStatusOrd = euStatusOrd;
    }

    public String getAppStatus() {
        return appStatus;
    }

    public void setAppStatus(String appStatus) {
        this.appStatus = appStatus;
    }

    public String getOccStatus() {
        return occStatus;
    }

    public void setOccStatus(String occStatus) {
        this.occStatus = occStatus;
    }

    public String getNameDept() {
        return nameDept;
    }

    public void setNameDept(String nameDept) {
        this.nameDept = nameDept;
    }
}
