package com.zebone.nhis.common.module.cn.cp;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: CP_REC_ORD_LAB 
 *
 * @since 2016-12-12 02:43:26
 */
@Table(value="CP_REC_ORD_LAB")
public class CpRecOrdLab extends BaseModule  {

	@PK
	@Field(value="PK_CPORDLAB",id=KeyId.UUID)
    private String pkCpordlab;

	@Field(value="PK_RECDT")
    private String pkRecdt;

	@Field(value="PK_CNORD")
    private String pkCnord;

	@Field(value="PURPOSE")
    private String purpose;

	@Field(value="DT_SAMPTYPE")
    private String dtSamptype;

	@Field(value="DT_TUBETYPE")
    private String dtTubetype;

	@Field(value="DT_COLTYPE")
    private String dtColtype;

	@Field(value="NOTE")
    private String note;

	@Field(value="FLAG_PRT")
    private String flagPrt;

	@Field(value="MODIFIER")
    private String modifier;

	@Field(value="MODITY_TIME")
    private Date modityTime;


    public String getPkCpordlab(){
        return this.pkCpordlab;
    }
    public void setPkCpordlab(String pkCpordlab){
        this.pkCpordlab = pkCpordlab;
    }

    public String getPkRecdt(){
        return this.pkRecdt;
    }
    public void setPkRecdt(String pkRecdt){
        this.pkRecdt = pkRecdt;
    }

    public String getPkCnord(){
        return this.pkCnord;
    }
    public void setPkCnord(String pkCnord){
        this.pkCnord = pkCnord;
    }

    public String getPurpose(){
        return this.purpose;
    }
    public void setPurpose(String purpose){
        this.purpose = purpose;
    }

    public String getDtSamptype(){
        return this.dtSamptype;
    }
    public void setDtSamptype(String dtSamptype){
        this.dtSamptype = dtSamptype;
    }

    public String getDtTubetype(){
        return this.dtTubetype;
    }
    public void setDtTubetype(String dtTubetype){
        this.dtTubetype = dtTubetype;
    }

    public String getDtColtype(){
        return this.dtColtype;
    }
    public void setDtColtype(String dtColtype){
        this.dtColtype = dtColtype;
    }

    public String getNote(){
        return this.note;
    }
    public void setNote(String note){
        this.note = note;
    }

    public String getFlagPrt(){
        return this.flagPrt;
    }
    public void setFlagPrt(String flagPrt){
        this.flagPrt = flagPrt;
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
}