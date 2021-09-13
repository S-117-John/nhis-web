package com.zebone.nhis.common.module.scm.pub;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: BD_PD_INDPD 
 *
 * @since 2018-12-15 03:03:56
 */
@Table(value="BD_PD_INDPD")
public class BdPdIndpd extends BaseModule  {

	@PK
	@Field(value="PK_PDINDPD",id=KeyId.UUID)
    private String pkPdindpd;

	@Field(value="PK_PDIND")
    private String pkPdind;

	@Field(value="PK_PD")
    private String pkPd;

	@Field(value="MODIFIER")
    private String modifier;

	@Field(value="MODITY_TIME")
    private Date modityTime;

    @Field(value="FLAG_PD")
    private String flagPd;

    public String getFlagPd() {
        return flagPd;
    }

    public void setFlagPd(String flagPd) {
        this.flagPd = flagPd;
    }

    public String getPkPdindpd(){
        return this.pkPdindpd;
    }
    public void setPkPdindpd(String pkPdindpd){
        this.pkPdindpd = pkPdindpd;
    }

    public String getPkPdind(){
        return this.pkPdind;
    }
    public void setPkPdind(String pkPdind){
        this.pkPdind = pkPdind;
    }

    public String getPkPd(){
        return this.pkPd;
    }
    public void setPkPd(String pkPd){
        this.pkPd = pkPd;
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