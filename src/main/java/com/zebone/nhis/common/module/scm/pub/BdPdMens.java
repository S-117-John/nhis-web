package com.zebone.nhis.common.module.scm.pub;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: BD_PD_MENS 
 *
 * @since 2018-07-23 06:40:34
 */
@Table(value="BD_PD_MENS")
public class BdPdMens extends BaseModule  {

	@PK
	@Field(value="PK_PDMENS",id=KeyId.UUID)
    private String pkPdmens;

	@Field(value="PK_PD")
    private String pkPd;

	@Field(value="PK_PD_MENS")
    private String pkPdMens;

	@Field(value="FLAG_DEF")
    private String flagDef;

	@Field(value="MODITY_TIME")
    private Date modityTime;


    public String getPkPdmens(){
        return this.pkPdmens;
    }
    public void setPkPdmens(String pkPdmens){
        this.pkPdmens = pkPdmens;
    }

    public String getPkPd(){
        return this.pkPd;
    }
    public void setPkPd(String pkPd){
        this.pkPd = pkPd;
    }

    public String getPkPdMens(){
        return this.pkPdMens;
    }
    public void setPkPdMens(String pkPdMens){
        this.pkPdMens = pkPdMens;
    }

    public String getFlagDef(){
        return this.flagDef;
    }
    public void setFlagDef(String flagDef){
        this.flagDef = flagDef;
    }

    public Date getModityTime(){
        return this.modityTime;
    }
    public void setModityTime(Date modityTime){
        this.modityTime = modityTime;
    }
}