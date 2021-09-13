package com.zebone.nhis.common.module.scm.purchase;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: PD_PURCHASE - pd_purchase 
 *
 * @since 2016-10-31 11:25:15
 */
@Table(value="PD_PURCHASE")
public class PdPurchase extends BaseModule  {

	@PK
	@Field(value="PK_PDPU",id=KeyId.UUID)
    private String pkPdpu;

	@Field(value="DT_PUTYPE")
    private String dtPutype;

	@Field(value="PK_DEPT")
    private String pkDept;

	@Field(value="CODE_PU")
    private String codePu;

	@Field(value="NAME_PU")
    private String namePu;

	@Field(value="PK_SUPPLYER")
    private String pkSupplyer;

	@Field(value="DATE_PU")
    private Date datePu;

    /** EU_STATUS - 0 制单，1 审核，2 完成，9 取消 */
	@Field(value="EU_STATUS")
    private String euStatus;

	@Field(value="PK_EMP_MAK")
    private String pkEmpMak;

	@Field(value="NAME_EMP_MAK")
    private String nameEmpMak;

	@Field(value="FLAG_CANC")
    private String flagCanc;

	@Field(value="PK_EMP_CANC")
    private String pkEmpCanc;

	@Field(value="NAME_EMP_CANC")
    private String nameEmpCanc;

	@Field(value="DATE_CANC")
    private Date dateCanc;

	@Field(value="FLAG_CHK")
    private String flagChk;

	@Field(value="PK_EMP_CHK")
    private String pkEmpChk;

	@Field(value="NAME_EMP_CHK")
    private String nameEmpChk;

	@Field(value="DATE_CHK")
    private Date dateChk;

	@Field(value="NOTE")
    private String note;

	@Field(value="MODITY_TIME")
    private Date modityTime;


    public String getPkPdpu(){
        return this.pkPdpu;
    }
    public void setPkPdpu(String pkPdpu){
        this.pkPdpu = pkPdpu;
    }

    public String getDtPutype(){
        return this.dtPutype;
    }
    public void setDtPutype(String dtPutype){
        this.dtPutype = dtPutype;
    }

    public String getPkDept(){
        return this.pkDept;
    }
    public void setPkDept(String pkDept){
        this.pkDept = pkDept;
    }

    public String getCodePu(){
        return this.codePu;
    }
    public void setCodePu(String codePu){
        this.codePu = codePu;
    }

    public String getNamePu(){
        return this.namePu;
    }
    public void setNamePu(String namePu){
        this.namePu = namePu;
    }

    public String getPkSupplyer(){
        return this.pkSupplyer;
    }
    public void setPkSupplyer(String pkSupplyer){
        this.pkSupplyer = pkSupplyer;
    }

    public Date getDatePu(){
        return this.datePu;
    }
    public void setDatePu(Date datePu){
        this.datePu = datePu;
    }

    public String getEuStatus(){
        return this.euStatus;
    }
    public void setEuStatus(String euStatus){
        this.euStatus = euStatus;
    }

    public String getPkEmpMak(){
        return this.pkEmpMak;
    }
    public void setPkEmpMak(String pkEmpMak){
        this.pkEmpMak = pkEmpMak;
    }

    public String getNameEmpMak(){
        return this.nameEmpMak;
    }
    public void setNameEmpMak(String nameEmpMak){
        this.nameEmpMak = nameEmpMak;
    }

    public String getFlagCanc(){
        return this.flagCanc;
    }
    public void setFlagCanc(String flagCanc){
        this.flagCanc = flagCanc;
    }

    public String getPkEmpCanc(){
        return this.pkEmpCanc;
    }
    public void setPkEmpCanc(String pkEmpCanc){
        this.pkEmpCanc = pkEmpCanc;
    }

    public String getNameEmpCanc(){
        return this.nameEmpCanc;
    }
    public void setNameEmpCanc(String nameEmpCanc){
        this.nameEmpCanc = nameEmpCanc;
    }

    public Date getDateCanc(){
        return this.dateCanc;
    }
    public void setDateCanc(Date dateCanc){
        this.dateCanc = dateCanc;
    }

    public String getFlagChk(){
        return this.flagChk;
    }
    public void setFlagChk(String flagChk){
        this.flagChk = flagChk;
    }

    public String getPkEmpChk(){
        return this.pkEmpChk;
    }
    public void setPkEmpChk(String pkEmpChk){
        this.pkEmpChk = pkEmpChk;
    }

    public String getNameEmpChk(){
        return this.nameEmpChk;
    }
    public void setNameEmpChk(String nameEmpChk){
        this.nameEmpChk = nameEmpChk;
    }

    public Date getDateChk(){
        return this.dateChk;
    }
    public void setDateChk(Date dateChk){
        this.dateChk = dateChk;
    }

    public String getNote(){
        return this.note;
    }
    public void setNote(String note){
        this.note = note;
    }

    public Date getModityTime(){
        return this.modityTime;
    }
    public void setModityTime(Date modityTime){
        this.modityTime = modityTime;
    }
}