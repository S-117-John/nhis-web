package com.zebone.nhis.common.module.base.bd.srv;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: BD_QC_QUE 
 *
 * @since 2019-03-18 05:49:27
 */
@Table(value="BD_QC_QUE")
public class BdQcQue extends BaseModule  {

	@PK
	@Field(value="PK_QCQUE",id=KeyId.UUID)
    private String pkQcque;

	@Field(value="PK_QCPLATFORM")
    private String pkQcplatform;

	@Field(value="SORTNO")
    private Integer sortno;

	@Field(value="PK_QCRULE")
    private String pkQcrule;

	@Field(value="DT_QCTYPE")
    private String dtQctype;

	@Field(value="PK_DEPT")
    private String pkDept;

	@Field(value="PK_SCHRES")
    private String pkSchres;

	@Field(value="NOTE")
    private String note;

	@Field(value="FLAG_STOP")
    private String flagStop;

	@Field(value="MODIFIER")
    private String modifier;

	@Field(value="MODITY_TIME")
    private Date modityTime;


    public String getPkQcque(){
        return this.pkQcque;
    }
    public void setPkQcque(String pkQcque){
        this.pkQcque = pkQcque;
    }

    public String getPkQcplatform(){
        return this.pkQcplatform;
    }
    public void setPkQcplatform(String pkQcplatform){
        this.pkQcplatform = pkQcplatform;
    }

    public Integer getSortno(){
        return this.sortno;
    }
    public void setSortno(Integer sortno){
        this.sortno = sortno;
    }

    public String getPkQcrule(){
        return this.pkQcrule;
    }
    public void setPkQcrule(String pkQcrule){
        this.pkQcrule = pkQcrule;
    }

    public String getDtQctype(){
        return this.dtQctype;
    }
    public void setDtQctype(String dtQctype){
        this.dtQctype = dtQctype;
    }

    public String getPkDept(){
        return this.pkDept;
    }
    public void setPkDept(String pkDept){
        this.pkDept = pkDept;
    }

    public String getPkSchres(){
        return this.pkSchres;
    }
    public void setPkSchres(String pkSchres){
        this.pkSchres = pkSchres;
    }

    public String getNote(){
        return this.note;
    }
    public void setNote(String note){
        this.note = note;
    }

    public String getFlagStop(){
        return this.flagStop;
    }
    public void setFlagStop(String flagStop){
        this.flagStop = flagStop;
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