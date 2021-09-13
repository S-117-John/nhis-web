package com.zebone.nhis.ma.pub.platform.send.impl.zsrm.model;

import java.sql.Blob;

public class SchDoctorVo {
    private String pkSch;

    /** 科室编码*/
    private String codeDept;

    /** 科室名称*/
    private String nameDept;

    /** 医生主键*/
    private String pkEmp;
    /** 医生编码*/
    private String codeEmp;

    /** 医生名称*/
    private String nameEmp;
    /** 医生简介*/
    private String spec;
    /** 医疗项目权限--对应职称*/
    private String dtEmpsrvtype;
    private String empsrvText;
    /**可预约数--实时统计查询*/
    private Integer cntAppt;

    /**可预约数--预约日期*/
    private String dateWork;

    private String nameDateslot;

    private String pkSchres;

    private String sortno;

    private String nameDeptArea;

    private String namePlaceArea;

    private String codeDeptArea;

    private String introduction;

    private String price;//加收

    private String pkSchsrv;

    private String priceSchsrv;//诊金

    private Blob photo;

    private String docterSortNo;

    public String getDocterSortNo() {
        return docterSortNo;
    }

    public void setDocterSortNo(String docterSortNo) {
        this.docterSortNo = docterSortNo;
    }

    public Blob getPhoto() {
        return photo;
    }

    public void setPhoto(Blob photo) {
        this.photo = photo;
    }

    public String getPriceSchsrv() {
        return priceSchsrv;
    }

    public void setPriceSchsrv(String priceSchsrv) {
        this.priceSchsrv = priceSchsrv;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPkSchsrv() {
        return pkSchsrv;
    }

    public void setPkSchsrv(String pkSchsrv) {
        this.pkSchsrv = pkSchsrv;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
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

    public String getCodeDeptArea() {
        return codeDeptArea;
    }

    public void setCodeDeptArea(String codeDeptArea) {
        this.codeDeptArea = codeDeptArea;
    }

    public String getSortno() {
        return sortno;
    }

    public void setSortno(String sortno) {
        this.sortno = sortno;
    }

    public String getPkSchres() {
        return pkSchres;
    }

    public void setPkSchres(String pkSchres) {
        this.pkSchres = pkSchres;
    }

    public String getNameDateslot() {
        return nameDateslot;
    }

    public void setNameDateslot(String nameDateslot) {
        this.nameDateslot = nameDateslot;
    }

    public String getDateWork() {
        return dateWork;
    }

    public void setDateWork(String dateWork) {
        this.dateWork = dateWork;
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

    public String getPkEmp() {
        return pkEmp;
    }

    public void setPkEmp(String pkEmp) {
        this.pkEmp = pkEmp;
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

    public String getSpec() {
        return spec;
    }

    public void setSpec(String spec) {
        this.spec = spec;
    }

    public String getDtEmpsrvtype() {
        return dtEmpsrvtype;
    }

    public void setDtEmpsrvtype(String dtEmpsrvtype) {
        this.dtEmpsrvtype = dtEmpsrvtype;
    }

    public String getEmpsrvText() {
        return empsrvText;
    }

    public void setEmpsrvText(String empsrvText) {
        this.empsrvText = empsrvText;
    }

    public Integer getCntAppt() {
        return cntAppt;
    }

    public void setCntAppt(Integer cntAppt) {
        this.cntAppt = cntAppt;
    }
}
