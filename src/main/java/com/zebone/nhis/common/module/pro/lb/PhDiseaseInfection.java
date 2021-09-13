package com.zebone.nhis.common.module.pro.lb;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;

@Table(value="PH_DISEASE_INFECTION")
public class PhDiseaseInfection extends BaseModule {
    @PK
    @Field(value="PK_INFECTION")
    private String pkInfection;

    @Field(value="PK_PHDISE")
    private String pkPhdise;

    @Field(value="EU_TYPE")
    private String euType;

    @Field(value="EU_OCCU")
    private String euOccu;

    @Field(value="OTH_OCCU")
    private String othOccu;

    @Field(value="EU_CASE1")
    private String euCase1;

    @Field(value="EU_CASE2")
    private String euCase2;

    @Field(value="DT_CONTAGIONA")
    private String dtContagiona;

    @Field(value="DT_CONTAGIONB")
    private String dtContagionb;

    @Field(value="DT_CONTAGIONC")
    private String dtContagionc;

    @Field(value="DT_CONTAGION")
    private String dtContagion;

    @Field(value="DISENAME_REVISAL")
    private String disenameRevisal;

    @Field(value="REASON_CANCEL")
    private String reasonCancel;

    public String getPkInfection() {
        return pkInfection;
    }

    public void setPkInfection(String pkInfection) {
        this.pkInfection = pkInfection == null ? null : pkInfection.trim();
    }

    public String getPkPhdise() {
        return pkPhdise;
    }

    public void setPkPhdise(String pkPhdise) {
        this.pkPhdise = pkPhdise == null ? null : pkPhdise.trim();
    }

    public String getEuType() {
        return euType;
    }

    public void setEuType(String euType) {
        this.euType = euType == null ? null : euType.trim();
    }

    public String getEuOccu() {
        return euOccu;
    }

    public void setEuOccu(String euOccu) {
        this.euOccu = euOccu == null ? null : euOccu.trim();
    }

    public String getOthOccu() {
        return othOccu;
    }

    public void setOthOccu(String othOccu) {
        this.othOccu = othOccu == null ? null : othOccu.trim();
    }

    public String getEuCase1() {
        return euCase1;
    }

    public void setEuCase1(String euCase1) {
        this.euCase1 = euCase1 == null ? null : euCase1.trim();
    }

    public String getEuCase2() {
        return euCase2;
    }

    public void setEuCase2(String euCase2) {
        this.euCase2 = euCase2 == null ? null : euCase2.trim();
    }

    public String getDtContagiona() {
        return dtContagiona;
    }

    public void setDtContagiona(String dtContagiona) {
        this.dtContagiona = dtContagiona == null ? null : dtContagiona.trim();
    }

    public String getDtContagionb() {
        return dtContagionb;
    }

    public void setDtContagionb(String dtContagionb) {
        this.dtContagionb = dtContagionb == null ? null : dtContagionb.trim();
    }

    public String getDtContagionc() {
        return dtContagionc;
    }

    public void setDtContagionc(String dtContagionc) {
        this.dtContagionc = dtContagionc == null ? null : dtContagionc.trim();
    }

    public String getDtContagion() {
        return dtContagion;
    }

    public void setDtContagion(String dtContagion) {
        this.dtContagion = dtContagion == null ? null : dtContagion.trim();
    }

    public String getDisenameRevisal() {
        return disenameRevisal;
    }

    public void setDisenameRevisal(String disenameRevisal) {
        this.disenameRevisal = disenameRevisal == null ? null : disenameRevisal.trim();
    }

    public String getReasonCancel() {
        return reasonCancel;
    }

    public void setReasonCancel(String reasonCancel) {
        this.reasonCancel = reasonCancel == null ? null : reasonCancel.trim();
    }
}