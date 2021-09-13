package com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.sch;

import java.util.Date;

public class ShortMsgSchApptInfoVo {

    private String namePi;

    private String mobile;

    private String telNo;

    private String nameDept;

    private String nameEmp;

    private String dateWork;

    private String flagStop;

    private String codePi;

    private Date beginTime;

    private String codeOp;

    private String nameOrg;

    private Date endTime;

    private String resouName;

    public String getResouName() {
        return resouName;
    }

    public void setResouName(String resouName) {
        this.resouName = resouName;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getNameOrg() {
        return nameOrg;
    }

    public void setNameOrg(String nameOrg) {
        this.nameOrg = nameOrg;
    }

    public String getCodeOp() {
        return codeOp;
    }

    public void setCodeOp(String codeOp) {
        this.codeOp = codeOp;
    }

    public String getCodePi() {
        return codePi;
    }

    public void setCodePi(String codePi) {
        this.codePi = codePi;
    }

    public Date getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Date beginTime) {
        this.beginTime = beginTime;
    }

    public String getNamePi() {
        return namePi;
    }

    public void setNamePi(String namePi) {
        this.namePi = namePi;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getTelNo() {
        return telNo;
    }

    public void setTelNo(String telNo) {
        this.telNo = telNo;
    }

    public String getNameDept() {
        return nameDept;
    }

    public void setNameDept(String nameDept) {
        this.nameDept = nameDept;
    }

    public String getNameEmp() {
        return nameEmp;
    }

    public void setNameEmp(String nameEmp) {
        this.nameEmp = nameEmp;
    }

    public String getDateWork() {
        return dateWork;
    }

    public void setDateWork(String dateWork) {
        this.dateWork = dateWork;
    }

    public String getFlagStop() {
        return flagStop;
    }

    public void setFlagStop(String flagStop) {
        this.flagStop = flagStop;
    }
}
