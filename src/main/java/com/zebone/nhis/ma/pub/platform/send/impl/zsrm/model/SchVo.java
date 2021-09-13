package com.zebone.nhis.ma.pub.platform.send.impl.zsrm.model;

public class SchVo {

    private String pkSch;

    /** 科室编码*/
    private String codeDept;

    /** 科室名称*/
    private String nameDept;

    /** 医生编码*/
    private String codeEmp;

    /** 医生名称*/
    private String nameEmp;

    /** 午别名称*/
    private String nameDateslot;

    /** 午别编码*/
    private String codeDateslot;

    /** 服务类型*/
    private String euSrvtype;

    /** 资源类型：0医生、1：人员*/
    private String euRestype;

    /** 资源名称*/
    private String nameRes;

    /** 排班日期yyyy-MM-dd*/
    private String dateWork;

    /** 午别开始时间*/
    private String timeBegin;

    /** 午别结束时间*/
    private String timeEnd;

    private String cntAppt;

    private String nameDeptArea;

    private String namePlaceArea;

    public String getNameDeptArea() {
        return nameDeptArea;
    }

    public void setNameDeptArea(String nameDeptArea) {
        this.nameDeptArea = nameDeptArea;
    }

    public String getNamePlaceArea() {
        return namePlaceArea;
    }

    public void setNamePlaceArea(String namePlaceArea) {
        this.namePlaceArea = namePlaceArea;
    }

    public String getCntAppt() {
        return cntAppt;
    }

    public void setCntAppt(String cntAppt) {
        this.cntAppt = cntAppt;
    }

    public String getPkSch() {
        return pkSch;
    }

    public void setPkSch(String pkSch) {
        this.pkSch = pkSch;
    }

    public String getCodeDept() {
        return codeDept;
    }

    public void setCodeDept(String codeDept) {
        this.codeDept = codeDept;
    }

    public String getNameDept() {
        return nameDept;
    }

    public void setNameDept(String nameDept) {
        this.nameDept = nameDept;
    }

    public String getNameDateslot() {
        return nameDateslot;
    }

    public void setNameDateslot(String nameDateslot) {
        this.nameDateslot = nameDateslot;
    }

    public String getCodeDateslot() {
        return codeDateslot;
    }

    public void setCodeDateslot(String codeDateslot) {
        this.codeDateslot = codeDateslot;
    }

    public String getEuSrvtype() {
        return euSrvtype;
    }

    public void setEuSrvtype(String euSrvtype) {
        this.euSrvtype = euSrvtype;
    }

    public String getEuRestype() {
        return euRestype;
    }

    public void setEuRestype(String euRestype) {
        this.euRestype = euRestype;
    }

    public String getNameRes() {
        return nameRes;
    }

    public void setNameRes(String nameRes) {
        this.nameRes = nameRes;
    }

    public String getTimeBegin() {
        return timeBegin;
    }

    public void setTimeBegin(String timeBegin) {
        this.timeBegin = timeBegin;
    }

    public String getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(String timeEnd) {
        this.timeEnd = timeEnd;
    }

    public String getDateWork() {
        return dateWork;
    }

    public void setDateWork(String dateWork) {
        this.dateWork = dateWork;
    }

    public String getCodeEmp() {
        return codeEmp;
    }

    public void setCodeEmp(String codeEmp) {
        this.codeEmp = codeEmp;
    }

    public String getNameEmp() {
        return nameEmp;
    }

    public void setNameEmp(String nameEmp) {
        this.nameEmp = nameEmp;
    }
}
