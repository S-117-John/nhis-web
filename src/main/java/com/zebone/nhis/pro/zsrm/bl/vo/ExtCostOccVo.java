package com.zebone.nhis.pro.zsrm.bl.vo;

import com.zebone.nhis.common.module.bl.opcg.BlOpDt;

import java.util.Date;

public class ExtCostOccVo extends BlOpDt {
    private String pkEmpOcc;
    private String nameEmpOcc;
    private String nameDeptOcc;
    private Date dateOcc;
    private String euStatus;
    private String pkCgopocc;
    private String euType;

    public String getEuType() {
        return euType;
    }

    public void setEuType(String euType) {
        this.euType = euType;
    }

    public String getPkCgopocc() {
        return pkCgopocc;
    }

    public void setPkCgopocc(String pkCgopocc) {
        this.pkCgopocc = pkCgopocc;
    }

    public String getPkEmpOcc() {
        return pkEmpOcc;
    }

    public void setPkEmpOcc(String pkEmpOcc) {
        this.pkEmpOcc = pkEmpOcc;
    }

    public String getNameEmpOcc() {
        return nameEmpOcc;
    }

    public void setNameEmpOcc(String nameEmpOcc) {
        this.nameEmpOcc = nameEmpOcc;
    }

    public String getNameDeptOcc() {
        return nameDeptOcc;
    }

    public void setNameDeptOcc(String nameDeptOcc) {
        this.nameDeptOcc = nameDeptOcc;
    }

    public Date getDateOcc() {
        return dateOcc;
    }

    public void setDateOcc(Date dateOcc) {
        this.dateOcc = dateOcc;
    }

    public String getEuStatus() {
        return euStatus;
    }

    public void setEuStatus(String euStatus) {
        this.euStatus = euStatus;
    }
}
