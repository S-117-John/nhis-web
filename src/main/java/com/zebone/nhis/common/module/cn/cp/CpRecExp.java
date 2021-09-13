package com.zebone.nhis.common.module.cn.cp;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: CP_REC_EXP 
 *
 * @since 2019-06-03 08:42:51
 */
@Table(value="CP_REC_EXP")
public class CpRecExp extends BaseModule  {

	@PK
	@Field(value="PK_RECEXP",id=KeyId.UUID)
    private String pkRecexp;

	@Field(value="PK_CPREC")
    private String pkCprec;

	@Field(value="EU_TASKTYPE")
    private String euTasktype;

	@Field(value="DT_CPEXPTYPE")
    private String dtCpexptype;

	@Field(value="PK_CPEXP")
    private String pkCpexp;

	@Field(value="NAME_EXP")
    private String nameExp;

	@Field(value="PK_CPPHASE")
    private String pkCpphase;

	@Field(value="PK_CPORD")
    private String pkCpord;

	@Field(value="PK_CNORD")
    private String pkCnord;

	@Field(value="DATE_EXP")
    private Date dateExp;

	@Field(value="PK_EMP_EXP")
    private String pkEmpExp;

	@Field(value="NAME_EMP_EXP")
    private String nameEmpExp;

	@Field(value="NOTE")
    private String note;

	@Field(value="MODITY_TIME")
    private Date modityTime;


    public String getPkRecexp(){
        return this.pkRecexp;
    }
    public void setPkRecexp(String pkRecexp){
        this.pkRecexp = pkRecexp;
    }

    public String getPkCprec(){
        return this.pkCprec;
    }
    public void setPkCprec(String pkCprec){
        this.pkCprec = pkCprec;
    }

    public String getEuTasktype(){
        return this.euTasktype;
    }
    public void setEuTasktype(String euTasktype){
        this.euTasktype = euTasktype;
    }

    public String getDtCpexptype(){
        return this.dtCpexptype;
    }
    public void setDtCpexptype(String dtCpexptype){
        this.dtCpexptype = dtCpexptype;
    }

    public String getPkCpexp(){
        return this.pkCpexp;
    }
    public void setPkCpexp(String pkCpexp){
        this.pkCpexp = pkCpexp;
    }

    public String getNameExp(){
        return this.nameExp;
    }
    public void setNameExp(String nameExp){
        this.nameExp = nameExp;
    }

    public String getPkCpphase(){
        return this.pkCpphase;
    }
    public void setPkCpphase(String pkCpphase){
        this.pkCpphase = pkCpphase;
    }

    public String getPkCpord(){
        return this.pkCpord;
    }
    public void setPkCpord(String pkCpord){
        this.pkCpord = pkCpord;
    }

    public String getPkCnord(){
        return this.pkCnord;
    }
    public void setPkCnord(String pkCnord){
        this.pkCnord = pkCnord;
    }

    public Date getDateExp(){
        return this.dateExp;
    }
    public void setDateExp(Date dateExp){
        this.dateExp = dateExp;
    }

    public String getPkEmpExp(){
        return this.pkEmpExp;
    }
    public void setPkEmpExp(String pkEmpExp){
        this.pkEmpExp = pkEmpExp;
    }

    public String getNameEmpExp(){
        return this.nameEmpExp;
    }
    public void setNameEmpExp(String nameEmpExp){
        this.nameEmpExp = nameEmpExp;
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