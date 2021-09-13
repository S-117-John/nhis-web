package com.zebone.nhis.common.module.cn.cp;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: CP_REC_REASON 
 *
 * @since 2019-05-30 11:49:54
 */
@Table(value="CP_REC_REASON")
public class CpRecReason extends BaseModule  {

	@PK
	@Field(value="PK_RECREASON",id=KeyId.UUID)
    private String pkRecreason;

	@Field(value="PK_CPREC")
    private String pkCprec;

	@Field(value="PK_CPREASON")
    private String pkCpreason;

	@Field(value="NAME_REASON")
    private String nameReason;

	@Field(value="DATE_REC")
    private Date dateRec;

	@Field(value="NOTE")
    private String note;

	@Field(value="MODITY_TIME")
    private Date modityTime;


    public String getPkRecreason(){
        return this.pkRecreason;
    }
    public void setPkRecreason(String pkRecreason){
        this.pkRecreason = pkRecreason;
    }

    public String getPkCprec(){
        return this.pkCprec;
    }
    public void setPkCprec(String pkCprec){
        this.pkCprec = pkCprec;
    }

    public String getPkCpreason(){
        return this.pkCpreason;
    }
    public void setPkCpreason(String pkCpreason){
        this.pkCpreason = pkCpreason;
    }

    public String getNameReason(){
        return this.nameReason;
    }
    public void setNameReason(String nameReason){
        this.nameReason = nameReason;
    }

    public Date getDateRec(){
        return this.dateRec;
    }
    public void setDateRec(Date dateRec){
        this.dateRec = dateRec;
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