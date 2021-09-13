package com.zebone.nhis.common.module.cn.cp;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: CP_REC_ORD_RIS 
 *
 * @since 2016-12-12 02:43:26
 */
@Table(value="CP_REC_ORD_RIS")
public class CpRecOrdRis extends BaseModule  {

	@PK
	@Field(value="PK_CPORDRIS",id=KeyId.UUID)
    private String pkCpordris;

	@Field(value="PK_RECDT")
    private String pkRecdt;

	@Field(value="PK_CNORD")
    private String pkCnord;

	@Field(value="NOTE_DISE")
    private byte[] noteDise;

	@Field(value="DT_RISTYPE")
    private String dtRistype;

	@Field(value="DESC_BODY")
    private String descBody;

	@Field(value="PURPOSE")
    private String purpose;

	@Field(value="RIS_NOTICE")
    private String risNotice;

	@Field(value="FLAG_BED")
    private String flagBed;

	@Field(value="NOTE")
    private String note;

	@Field(value="MODIFIER")
    private String modifier;

	@Field(value="MODITY_TIME")
    private Date modityTime;


    public String getPkCpordris(){
        return this.pkCpordris;
    }
    public void setPkCpordris(String pkCpordris){
        this.pkCpordris = pkCpordris;
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

    public byte[] getNoteDise(){
        return this.noteDise;
    }
    public void setNoteDise(byte[] noteDise){
        this.noteDise = noteDise;
    }

    public String getDtRistype(){
        return this.dtRistype;
    }
    public void setDtRistype(String dtRistype){
        this.dtRistype = dtRistype;
    }

    public String getDescBody(){
        return this.descBody;
    }
    public void setDescBody(String descBody){
        this.descBody = descBody;
    }

    public String getPurpose(){
        return this.purpose;
    }
    public void setPurpose(String purpose){
        this.purpose = purpose;
    }

    public String getRisNotice(){
        return this.risNotice;
    }
    public void setRisNotice(String risNotice){
        this.risNotice = risNotice;
    }

    public String getFlagBed(){
        return this.flagBed;
    }
    public void setFlagBed(String flagBed){
        this.flagBed = flagBed;
    }

    public String getNote(){
        return this.note;
    }
    public void setNote(String note){
        this.note = note;
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