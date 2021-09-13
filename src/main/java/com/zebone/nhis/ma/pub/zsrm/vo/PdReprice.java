package com.zebone.nhis.ma.pub.zsrm.vo;


import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;

/**
 * Table: PD_REPRICE - pd_reprice
 *
 * @since 2016-11-10 01:36:26
 */
@Table(value = "PD_REPRICE")
public class PdReprice extends BaseModule {

    @PK
    @Field(value = "PK_PDREP", id = KeyId.UUID)
    private String pkPdrep;

    /**
     * DT_REPTYPE - 0政策条件， 1企业调价，2其他调价
     */
    @Field(value = "DT_REPTYPE")
    private String dtReptype;

    @Field(value = "CODE_REP")
    private String codeRep;

    @Field(value = "APPROVAL")
    private String approval;

    /**
     * EU_REPMODE - 0 实时调价，1 定时调价
     */
    @Field(value = "EU_REPMODE")
    private String euRepmode;

    @Field(value = "DATE_EFFE")
    private Date dateEffe;

    @Field(value = "DATE_INPUT")
    private Date dateInput;

    @Field(value = "PK_EMP_INPUT")
    private String pkEmpInput;

    @Field(value = "NAME_EMP_INPUT")
    private String nameEmpInput;

    @Field(value = "FLAG_CHK")
    private String flagChk;

    @Field(value = "PK_EMP_CHK")
    private String pkEmpChk;

    @Field(value = "NAME_EMP_CHK")
    private String nameEmpChk;

    @Field(value = "DATE_CHK")
    private Date dateChk;

    /**
     * EU_STATUS - 0 制单，1审核，2完成，9取消
     */
    @Field(value = "EU_STATUS")
    private String euStatus;

    @Field(value = "FLAG_CANC")
    private String flagCanc;

    @Field(value = "PK_EMP_CANC")
    private String pkEmpCanc;

    @Field(value = "NAME_EMP_CANC")
    private String nameEmpCanc;

    @Field(value = "DATE_CANC")
    private Date dateCanc;

    @Field(value = "NOTE")
    private String note;

    @Field(value = "MODITY_TIME")
    private Date modityTime;

    @Field(value = "pk_store")
    private String pkStore;

    @Field(value = "pk_dept")
    private String pkDept;

    public String getPkDept() {
        return pkDept;
    }

    public void setPkDept(String pkDept) {
        this.pkDept = pkDept;
    }

    public String getPkStore() {
        return pkStore;
    }

    public void setPkStore(String pkStore) {
        this.pkStore = pkStore;
    }

    public String getPkPdrep() {
        return this.pkPdrep;
    }

    public void setPkPdrep(String pkPdrep) {
        this.pkPdrep = pkPdrep;
    }

    public String getDtReptype() {
        return this.dtReptype;
    }

    public void setDtReptype(String dtReptype) {
        this.dtReptype = dtReptype;
    }

    public String getCodeRep() {
        return this.codeRep;
    }

    public void setCodeRep(String codeRep) {
        this.codeRep = codeRep;
    }

    public String getApproval() {
        return this.approval;
    }

    public void setApproval(String approval) {
        this.approval = approval;
    }

    public String getEuRepmode() {
        return this.euRepmode;
    }

    public void setEuRepmode(String euRepmode) {
        this.euRepmode = euRepmode;
    }

    public Date getDateEffe() {
        return this.dateEffe;
    }

    public void setDateEffe(Date dateEffe) {
        this.dateEffe = dateEffe;
    }

    public Date getDateInput() {
        return this.dateInput;
    }

    public void setDateInput(Date dateInput) {
        this.dateInput = dateInput;
    }

    public String getPkEmpInput() {
        return this.pkEmpInput;
    }

    public void setPkEmpInput(String pkEmpInput) {
        this.pkEmpInput = pkEmpInput;
    }

    public String getNameEmpInput() {
        return this.nameEmpInput;
    }

    public void setNameEmpInput(String nameEmpInput) {
        this.nameEmpInput = nameEmpInput;
    }

    public String getFlagChk() {
        return this.flagChk;
    }

    public void setFlagChk(String flagChk) {
        this.flagChk = flagChk;
    }

    public String getPkEmpChk() {
        return this.pkEmpChk;
    }

    public void setPkEmpChk(String pkEmpChk) {
        this.pkEmpChk = pkEmpChk;
    }

    public String getNameEmpChk() {
        return this.nameEmpChk;
    }

    public void setNameEmpChk(String nameEmpChk) {
        this.nameEmpChk = nameEmpChk;
    }

    public Date getDateChk() {
        return this.dateChk;
    }

    public void setDateChk(Date dateChk) {
        this.dateChk = dateChk;
    }

    public String getEuStatus() {
        return this.euStatus;
    }

    public void setEuStatus(String euStatus) {
        this.euStatus = euStatus;
    }

    public String getFlagCanc() {
        return this.flagCanc;
    }

    public void setFlagCanc(String flagCanc) {
        this.flagCanc = flagCanc;
    }

    public String getPkEmpCanc() {
        return this.pkEmpCanc;
    }

    public void setPkEmpCanc(String pkEmpCanc) {
        this.pkEmpCanc = pkEmpCanc;
    }

    public String getNameEmpCanc() {
        return this.nameEmpCanc;
    }

    public void setNameEmpCanc(String nameEmpCanc) {
        this.nameEmpCanc = nameEmpCanc;
    }

    public Date getDateCanc() {
        return this.dateCanc;
    }

    public void setDateCanc(Date dateCanc) {
        this.dateCanc = dateCanc;
    }

    public String getNote() {
        return this.note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Date getModityTime() {
        return this.modityTime;
    }

    public void setModityTime(Date modityTime) {
        this.modityTime = modityTime;
    }
}