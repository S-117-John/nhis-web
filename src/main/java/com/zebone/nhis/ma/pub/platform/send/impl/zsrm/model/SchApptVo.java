package com.zebone.nhis.ma.pub.platform.send.impl.zsrm.model;

import com.zebone.nhis.common.module.sch.appt.SchAppt;

import java.util.Date;

public class SchApptVo extends SchAppt {
    /** 午别名称*/
    private String nameDateslot;
    /** 午别编码*/
    private String codeDateslot;
    /** 开始时间*/
    private String timeBegin;
    /** 结束时间*/
    private String timeEnd;
    /** 科室编码*/
    private String codeDept;
    /** 科室名称*/
    private String nameDept;
    /**科室位置信息**/
    private String namePlace;
    /** 医生编码*/
    private String codeEmp;
    /** 医生名称*/
    private String nameEmp;
    /** 预约渠道编码*/
    private String dtApptype;
    /** 预约渠道名称*/
    private String nameApptype;
    private String euSrvtype;

    private String codePi;
    private String idNo;
    private String namePi;
    private String mobile;
    private Date birthDate;
    private String cardNo;
    private String codeOp;
    private String nameOrg;
    private String resCode;
    private String resName;
    private String dtSex;
    private String nameDeptArea;
    private String resouName;
    private String codeDeptArea;
    private String resouCode;


    private String namePlaceArea;

    public String getResouCode() {
        return resouCode;
    }

    public void setResouCode(String resouCode) {
        this.resouCode = resouCode;
    }

    public String getCodeDeptArea() {
        return codeDeptArea;
    }

    public void setCodeDeptArea(String codeDeptArea) {
        this.codeDeptArea = codeDeptArea;
    }

    public String getResouName() {
        return resouName;
    }

    public void setResouName(String resouName) {
        this.resouName = resouName;
    }

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

    public String getDtSex() {
        return dtSex;
    }

    public void setDtSex(String dtSex) {
        this.dtSex = dtSex;
    }

    public String getResCode() {
        return resCode;
    }

    public void setResCode(String resCode) {
        this.resCode = resCode;
    }

    public String getResName() {
        return resName;
    }

    public void setResName(String resName) {
        this.resName = resName;
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

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
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

    @Override
    public String getDtApptype() {
        return dtApptype;
    }

    @Override
    public void setDtApptype(String dtApptype) {
        this.dtApptype = dtApptype;
    }

    public String getNameApptype() {
        return nameApptype;
    }

    public void setNameApptype(String nameApptype) {
        this.nameApptype = nameApptype;
    }

    public String getEuSrvtype() {
        return euSrvtype;
    }

    public void setEuSrvtype(String euSrvtype) {
        this.euSrvtype = euSrvtype;
    }

    public String getCodePi() {
        return codePi;
    }

    public void setCodePi(String codePi) {
        this.codePi = codePi;
    }

    public String getIdNo() {
        return idNo;
    }

    public void setIdNo(String idNo) {
        this.idNo = idNo;
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

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public String getNamePlace() {
        return namePlace;
    }

    public void setNamePlace(String namePlace) {
        this.namePlace = namePlace;
    }
}
