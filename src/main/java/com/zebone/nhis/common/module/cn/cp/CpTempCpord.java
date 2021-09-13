package com.zebone.nhis.common.module.cn.cp;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: CP_TEMP_CPORD 
 *
 * @since 2019-05-30 07:29:22
 */
@Table(value="CP_TEMP_CPORD")
public class CpTempCpord extends BaseModule  {

	@PK
	@Field(value="PK_CPORD",id=KeyId.UUID)
    private String pkCpord;

	@Field(value="PK_CPPHASE")
    private String pkCpphase;

	@Field(value="SORTNO")
    private Integer sortno;

	@Field(value="EU_TASKTYPE")
    private String euTasktype;

	@Field(value="EU_ALWAYS")
    private String euAlways;

	@Field(value="NAME_FORM")
    private String nameForm;

	@Field(value="EU_CPORDTYPE")
    private String euCpordtype;

	@Field(value="CODE_ORDTYPE")
    private String codeOrdtype;

	@Field(value="PK_ORD")
    private String pkOrd;

	@Field(value="CODE_ORD")
    private String codeOrd;

	@Field(value="NAME_ORD")
    private String nameOrd;

	@Field(value="DOSAGE")
    private Double dosage;

	@Field(value="PK_UNIT_DOSE")
    private String pkUnitDose;

	@Field(value="CODE_SUPPLY")
    private String codeSupply;

	@Field(value="CODE_FREQ")
    private String codeFreq;

	@Field(value="PK_PARENT")
    private String pkParent;

	@Field(value="DT_PHARM")
    private String dtPharm;

	@Field(value="FLAG_NEC")
    private String flagNec;

	@Field(value="MODITY_TIME")
    private Date modityTime;


    public String getPkCpord(){
        return this.pkCpord;
    }
    public void setPkCpord(String pkCpord){
        this.pkCpord = pkCpord;
    }

    public String getPkCpphase(){
        return this.pkCpphase;
    }
    public void setPkCpphase(String pkCpphase){
        this.pkCpphase = pkCpphase;
    }

    public Integer getSortno(){
        return this.sortno;
    }
    public void setSortno(Integer sortno){
        this.sortno = sortno;
    }

    public String getEuTasktype(){
        return this.euTasktype;
    }
    public void setEuTasktype(String euTasktype){
        this.euTasktype = euTasktype;
    }

    public String getEuAlways(){
        return this.euAlways;
    }
    public void setEuAlways(String euAlways){
        this.euAlways = euAlways;
    }

    public String getNameForm(){
        return this.nameForm;
    }
    public void setNameForm(String nameForm){
        this.nameForm = nameForm;
    }

    public String getEuCpordtype(){
        return this.euCpordtype;
    }
    public void setEuCpordtype(String euCpordtype){
        this.euCpordtype = euCpordtype;
    }

    public String getCodeOrdtype(){
        return this.codeOrdtype;
    }
    public void setCodeOrdtype(String codeOrdtype){
        this.codeOrdtype = codeOrdtype;
    }

    public String getPkOrd(){
        return this.pkOrd;
    }
    public void setPkOrd(String pkOrd){
        this.pkOrd = pkOrd;
    }

    public String getCodeOrd(){
        return this.codeOrd;
    }
    public void setCodeOrd(String codeOrd){
        this.codeOrd = codeOrd;
    }

    public String getNameOrd(){
        return this.nameOrd;
    }
    public void setNameOrd(String nameOrd){
        this.nameOrd = nameOrd;
    }

    public Double getDosage(){
        return this.dosage;
    }
    public void setDosage(Double dosage){
        this.dosage = dosage;
    }

    public String getPkUnitDose(){
        return this.pkUnitDose;
    }
    public void setPkUnitDose(String pkUnitDose){
        this.pkUnitDose = pkUnitDose;
    }

    public String getCodeSupply(){
        return this.codeSupply;
    }
    public void setCodeSupply(String codeSupply){
        this.codeSupply = codeSupply;
    }

    public String getCodeFreq(){
        return this.codeFreq;
    }
    public void setCodeFreq(String codeFreq){
        this.codeFreq = codeFreq;
    }

    public String getPkParent(){
        return this.pkParent;
    }
    public void setPkParent(String pkParent){
        this.pkParent = pkParent;
    }

    public String getDtPharm(){
        return this.dtPharm;
    }
    public void setDtPharm(String dtPharm){
        this.dtPharm = dtPharm;
    }

    public String getFlagNec(){
        return this.flagNec;
    }
    public void setFlagNec(String flagNec){
        this.flagNec = flagNec;
    }

    public Date getModityTime(){
        return this.modityTime;
    }
    public void setModityTime(Date modityTime){
        this.modityTime = modityTime;
    }
}