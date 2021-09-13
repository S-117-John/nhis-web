package com.zebone.nhis.common.module.cn.ipdw;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;

import java.math.BigDecimal;
import java.util.Date;


/**
 * Table: CN_ORD_ANTI_APPLY_DT 
 *
 * @since 2021-02-01 10:47:52
 */
@Table(value="CN_ORD_ANTI_APPLY_DT")
public class CnOrdAntiApplyDt extends BaseModule  {

	@PK
	@Field(value="PK_ORDANTIDETAIL",id=KeyId.UUID)
    private String pkOrdantidetail;

	@Field(value="PK_ORDANTIAPPLY")
    private String pkOrdantiapply;

	@Field(value="PK_PD")
    private String pkPd;

	@Field(value="DOSAGE")
    private BigDecimal dosage;

	@Field(value="PK_UNIT_DOS")
    private String pkUnitDos;

	@Field(value="CODE_SUPPLY")
    private String codeSupply;

	@Field(value="FLAG_PASS")
    private String flagPass;

	@Field(value="FLAG_EXPRE")
    private String flagExpre;

	@Field(value="CODE_FREQ")
    private String codeFreq;

	@Field(value="DAYS")
    private BigDecimal days;

	@Field(value="NOTE")
    private String note;

	@Field(value="EU_TYPE")
    private String euType;

	@Field(value="MODITY_TIME")
    private Date modityTime;


    public String getPkOrdantidetail(){
        return this.pkOrdantidetail;
    }
    public void setPkOrdantidetail(String pkOrdantidetail){
        this.pkOrdantidetail = pkOrdantidetail;
    }

    public String getPkOrdantiapply(){
        return this.pkOrdantiapply;
    }
    public void setPkOrdantiapply(String pkOrdantiapply){
        this.pkOrdantiapply = pkOrdantiapply;
    }

    public String getPkPd(){
        return this.pkPd;
    }
    public void setPkPd(String pkPd){
        this.pkPd = pkPd;
    }

    public BigDecimal getDosage(){
        return this.dosage;
    }
    public void setDosage(BigDecimal dosage){
        this.dosage = dosage;
    }

    public String getPkUnitDos(){
        return this.pkUnitDos;
    }
    public void setPkUnitDos(String pkUnitDos){
        this.pkUnitDos = pkUnitDos;
    }

    public String getCodeSupply(){
        return this.codeSupply;
    }
    public void setCodeSupply(String codeSupply){
        this.codeSupply = codeSupply;
    }

    public String getFlagPass(){
        return this.flagPass;
    }
    public void setFlagPass(String flagPass){
        this.flagPass = flagPass;
    }

    public String getFlagExpre(){
        return this.flagExpre;
    }
    public void setFlagExpre(String flagExpre){
        this.flagExpre = flagExpre;
    }

    public String getCodeFreq(){
        return this.codeFreq;
    }
    public void setCodeFreq(String codeFreq){
        this.codeFreq = codeFreq;
    }

    public BigDecimal getDays(){
        return this.days;
    }
    public void setDays(BigDecimal days){
        this.days = days;
    }

    public String getNote(){
        return this.note;
    }
    public void setNote(String note){
        this.note = note;
    }

    public String getEuType(){
        return this.euType;
    }
    public void setEuType(String euType){
        this.euType = euType;
    }

    public Date getModityTime(){
        return this.modityTime;
    }
    public void setModityTime(Date modityTime){
        this.modityTime = modityTime;
    }
}