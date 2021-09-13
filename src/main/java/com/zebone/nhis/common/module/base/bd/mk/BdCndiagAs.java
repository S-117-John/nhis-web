package com.zebone.nhis.common.module.base.bd.mk;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: BD_CNDIAG_AS 
 *
 * @since 2018-12-25 10:10:42
 */
@Table(value="BD_CNDIAG_AS")
public class BdCndiagAs extends BaseModule  {

	@PK
	@Field(value="PK_CNDIAGAS",id=KeyId.UUID)
    private String pkCndiagas;

	@Field(value="PK_CNDIAG")
    private String pkCndiag;

	@Field(value="ALIAS")
    private String alias;

	@Field(value="SPCODE")
    private String spcode;

	@Field(value="D_CODE")
    private String dCode;

	@Field(value="MODIFIER")
    private String modifier;

	@Field(value="MODITY_TIME")
    private Date modityTime;


    public String getPkCndiagas(){
        return this.pkCndiagas;
    }
    public void setPkCndiagas(String pkCndiagas){
        this.pkCndiagas = pkCndiagas;
    }

    public String getPkCndiag(){
        return this.pkCndiag;
    }
    public void setPkCndiag(String pkCndiag){
        this.pkCndiag = pkCndiag;
    }

    public String getAlias(){
        return this.alias;
    }
    public void setAlias(String alias){
        this.alias = alias;
    }

    public String getSpcode(){
        return this.spcode;
    }
    public void setSpcode(String spcode){
        this.spcode = spcode;
    }

    public String getdCode(){
        return this.dCode;
    }
    public void setdCode(String dCode){
        this.dCode = dCode;
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