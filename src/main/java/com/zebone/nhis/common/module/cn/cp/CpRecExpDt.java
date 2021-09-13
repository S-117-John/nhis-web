package com.zebone.nhis.common.module.cn.cp;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: CP_REC_EXP_DT 
 *
 * @since 2016-12-12 02:43:26
 */
@Table(value="CP_REC_EXP_DT")
public class CpRecExpDt extends BaseModule  {

	@PK
	@Field(value="PK_RECEXPDT",id=KeyId.UUID)
    private String pkRecexpdt;

	@Field(value="EU_TYPE")
    private String euType;

	@Field(value="PK_RECEXP")
    private String pkRecexp;

	@Field(value="PK_CPPHASE")
    private String pkCpphase;

	@Field(value="PK_CPORD")
    private String pkCpord;
    
	@Field(value="PK_CNORD")
	private String pkCnord;
	
	@Field(value="PK_TEMPWORK")
    private String pkTempwork;

	@Field(value="MODIFIER")
    private String modifier;

	@Field(value="MODITY_TIME")
    private Date modityTime;


    public String getPkRecexpdt(){
        return this.pkRecexpdt;
    }
    public void setPkRecexpdt(String pkRecexpdt){
        this.pkRecexpdt = pkRecexpdt;
    }

    public String getEuType(){
        return this.euType;
    }
    public void setEuType(String euType){
        this.euType = euType;
    }

    public String getPkRecexp(){
        return this.pkRecexp;
    }
    public void setPkRecexp(String pkRecexp){
        this.pkRecexp = pkRecexp;
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

    public String getPkTempwork() {
		return pkTempwork;
	}
	public void setPkTempwork(String pkTempwork) {
		this.pkTempwork = pkTempwork;
	}
	public String getModifier(){
        return this.modifier;
    }
    public void setModifier(String modifier){
        this.modifier = modifier;
    }

    public Date getModityTime(){
        return this.modityTime;
    }
    public void setModityTime(Date modityTime){
        this.modityTime = modityTime;
    }
	public String getPkCnord() {
		return pkCnord;
	}
	public void setPkCnord(String pkCnord) {
		this.pkCnord = pkCnord;
	}
    
}