package com.zebone.nhis.common.module.ma.dp;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: DP_INV_RESULT - DP_INV_RESULT 
 *
 * @since 2016-11-09 01:37:11
 */
@Table(value="DP_INV_RESULT")
public class DpInvResult extends BaseModule  {

	@Field(value="PK_INVRESULT",id=KeyId.UUID)
    private String pkInvresult;

	@Field(value="PK_DPINV")
    private String pkDpinv;

    /** PK_TARGET - 患者主键pk_pi或员工主键pk_emp */
	@Field(value="PK_TARGET")
    private String pkTarget;

	@Field(value="FLAG_FIN")
    private String flagFin;

	@Field(value="DATE_FIN")
    private String dateFin;

	@Field(value="NOTE")
    private String note;

	@Field(value="MODITY_TIME")
    private Date modityTime;


    public String getPkInvresult(){
        return this.pkInvresult;
    }
    public void setPkInvresult(String pkInvresult){
        this.pkInvresult = pkInvresult;
    }

    public String getPkDpinv(){
        return this.pkDpinv;
    }
    public void setPkDpinv(String pkDpinv){
        this.pkDpinv = pkDpinv;
    }

    public String getPkTarget(){
        return this.pkTarget;
    }
    public void setPkTarget(String pkTarget){
        this.pkTarget = pkTarget;
    }

    public String getFlagFin(){
        return this.flagFin;
    }
    public void setFlagFin(String flagFin){
        this.flagFin = flagFin;
    }

    public String getDateFin(){
        return this.dateFin;
    }
    public void setDateFin(String dateFin){
        this.dateFin = dateFin;
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