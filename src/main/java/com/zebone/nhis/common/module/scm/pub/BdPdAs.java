package com.zebone.nhis.common.module.scm.pub;


import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: BD_PD_AS - bd_pd_as 
 *
 * @since 2016-10-29 09:38:11
 */
@Table(value="BD_PD_AS")
public class BdPdAs extends BaseModule  {

	@PK
	@Field(value="PK_PDAS",id=KeyId.UUID)
    private String pkPdas;

	@Field(value="PK_PD")
    private String pkPd;

	@Field(value="ALIAS")
    private String alias;

	@Field(value="SPCODE")
    private String spcode;

	@Field(value="D_CODE")
    private String dCode;


    public String getPkPdas(){
        return this.pkPdas;
    }
    public void setPkPdas(String pkPdas){
        this.pkPdas = pkPdas;
    }

    public String getPkPd(){
        return this.pkPd;
    }
    public void setPkPd(String pkPd){
        this.pkPd = pkPd;
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
}