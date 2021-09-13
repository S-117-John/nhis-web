package com.zebone.nhis.common.module.cn.cp;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: CP_REC_DT 
 *
 * @since 2019-05-30 07:13:43
 */
@Table(value="CP_REC_DT")
public class CpRecDt extends BaseModule  {

	@PK
	@Field(value="PK_RECDT",id=KeyId.UUID)
    private String pkRecdt;

	@Field(value="PK_RECPHASE")
    private String pkRecphase;

	@Field(value="EU_CPORDTYPE")
    private String euCpordtype;

	@Field(value="PK_CPORD")
    private String pkCpord;

	@Field(value="PK_CNORD")
    private String pkCnord;
	
	@Field(value="pk_pres")
    private String pkPres;

	@Field(value="EU_STATUS")
    private String euStatus;

	@Field(value="DATE_EX")
    private Date dateEx;

	@Field(value="PK_EMP_EX")
    private String pkEmpEx;

	@Field(value="NAME_EMP_EX")
    private String nameEmpEx;

	@Field(value="NOTE")
    private String note;

	@Field(value="MODITY_TIME")
    private Date modityTime;


	
    public String getPkPres() {
		return pkPres;
	}
	public void setPkPres(String pkPres) {
		this.pkPres = pkPres;
	}
	public String getPkRecdt(){
        return this.pkRecdt;
    }
    public void setPkRecdt(String pkRecdt){
        this.pkRecdt = pkRecdt;
    }

    public String getPkRecphase(){
        return this.pkRecphase;
    }
    public void setPkRecphase(String pkRecphase){
        this.pkRecphase = pkRecphase;
    }

    public String getEuCpordtype(){
        return this.euCpordtype;
    }
    public void setEuCpordtype(String euCpordtype){
        this.euCpordtype = euCpordtype;
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

    public String getEuStatus(){
        return this.euStatus;
    }
    public void setEuStatus(String euStatus){
        this.euStatus = euStatus;
    }

    public Date getDateEx(){
        return this.dateEx;
    }
    public void setDateEx(Date dateEx){
        this.dateEx = dateEx;
    }

    public String getPkEmpEx(){
        return this.pkEmpEx;
    }
    public void setPkEmpEx(String pkEmpEx){
        this.pkEmpEx = pkEmpEx;
    }

    public String getNameEmpEx(){
        return this.nameEmpEx;
    }
    public void setNameEmpEx(String nameEmpEx){
        this.nameEmpEx = nameEmpEx;
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