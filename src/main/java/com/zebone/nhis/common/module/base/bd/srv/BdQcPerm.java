package com.zebone.nhis.common.module.base.bd.srv;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: BD_QC_PERM 
 *
 * @since 2019-03-19 06:15:10
 */
@Table(value="BD_QC_PERM")
public class BdQcPerm extends BaseModule  {

	@PK
	@Field(value="PK_QCPERM",id=KeyId.UUID)
    private String pkQcperm;

	@Field(value="PK_QCPLATFORM")
    private String pkQcplatform;

	@Field(value="PK_USER")
    private String pkUser;

	@Field(value="MODIFIER")
    private String modifier;

	@Field(value="MODITY_TIME")
    private Date modityTime;

	@Field(value="FLAG_DEF")
    private String flagDef;


    public String getPkQcperm(){
        return this.pkQcperm;
    }
    public void setPkQcperm(String pkQcperm){
        this.pkQcperm = pkQcperm;
    }

    public String getPkQcplatform(){
        return this.pkQcplatform;
    }
    public void setPkQcplatform(String pkQcplatform){
        this.pkQcplatform = pkQcplatform;
    }

    public String getPkUser(){
        return this.pkUser;
    }
    public void setPkUser(String pkUser){
        this.pkUser = pkUser;
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

    public String getFlagDef(){
        return this.flagDef;
    }
    public void setFlagDef(String flagDef){
        this.flagDef = flagDef;
    }
}