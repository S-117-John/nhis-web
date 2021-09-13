package com.zebone.nhis.common.module.compay.ins.lb.szyb;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;

@Table(value="INS_SZYB_EMPLOYEE")
public class InsSzybEmployee extends BaseModule {

    @PK
    @Field(value="PK_ID",id= Field.KeyId.UUID)
    private String pkId;

    @Field(value="INSU_PRESCRIPTION")
    private String insuPrescription;

    @Field(value="EMP_LEVEL")
    private String empLevel;

    @Field(value="PK_EMP")
    private String pkEmp;

    @Field(value="EMP_JOB_NAME")
    private String empJobName;

    @Field(value="EX_POST")
    private String exPost;

    @Field(value="ACA_POST")
    private String acaPost;

    @Field(value="EMP_SCHOOL")
    private String empSchool;

    @Field(value="REMARK")
    private String remark;

    @Field(value="OPER")
    private String oper;

    @Field(value="EMP_CER_NO")
    private String empCerNo;

    @Field(value="PRA_AREA")
    private String praArea;

    @Field(value="CATEGORV")
    private String categorv;

    @Field(value="WORK_PHONE")
    private String workPhone;

    @Field(value="ID_NO")
    private String idNo;

    @Field(value="CODE_EMP")
    private String codeEmp;

    public String getCodeEmp() {
        return codeEmp;
    }

    public void setCodeEmp(String codeEmp) {
        this.codeEmp = codeEmp;
    }

    public String getPkId() {
        return pkId;
    }

    public void setPkId(String pkId) {
        this.pkId = pkId;
    }

    public String getInsuPrescription() {
        return insuPrescription;
    }

    public void setInsuPrescription(String insuPrescription) {
        this.insuPrescription = insuPrescription;
    }

    public String getEmpLevel() {
        return empLevel;
    }

    public void setEmpLevel(String empLevel) {
        this.empLevel = empLevel;
    }

    public String getPkEmp() {
        return pkEmp;
    }

    public void setPkEmp(String pkEmp) {
        this.pkEmp = pkEmp;
    }

    public String getEmpJobName() {
        return empJobName;
    }

    public void setEmpJobName(String empJobName) {
        this.empJobName = empJobName;
    }

    public String getExPost() {
        return exPost;
    }

    public void setExPost(String exPost) {
        this.exPost = exPost;
    }

    public String getAcaPost() {
        return acaPost;
    }

    public void setAcaPost(String acaPost) {
        this.acaPost = acaPost;
    }

    public String getEmpSchool() {
        return empSchool;
    }

    public void setEmpSchool(String empSchool) {
        this.empSchool = empSchool;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getOper() {
        return oper;
    }

    public void setOper(String oper) {
        this.oper = oper;
    }

    public String getEmpCerNo() {
        return empCerNo;
    }

    public void setEmpCerNo(String empCerNo) {
        this.empCerNo = empCerNo;
    }

    public String getPraArea() {
        return praArea;
    }

    public void setPraArea(String praArea) {
        this.praArea = praArea;
    }

    public String getCategorv() {
        return categorv;
    }

    public void setCategorv(String categorv) {
        this.categorv = categorv;
    }

    public String getWorkPhone() {
        return workPhone;
    }

    public void setWorkPhone(String workPhone) {
        this.workPhone = workPhone;
    }

    public String getIdNo() {
        return idNo;
    }

    public void setIdNo(String idNo) {
        this.idNo = idNo;
    }
}
