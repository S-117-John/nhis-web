package com.zebone.nhis.common.module.cn.opdw;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;

/**
 * Table: BD_PVDOC_TEMP_ORG  - BD_PVDOC_TEMP_ORG 
 *
 * @since 2017-03-06 12:08:06
 */
@Table(value="BD_PVDOC_TEMP_ORG")
public class BdPvdocTempOrg extends BaseModule  {

	@PK
	@Field(value="PK_TEMPORG",id=KeyId.UUID)
    private String pkTemporg;

	@Field(value="PK_PVDOCTEMP")
    private String pkPvdoctemp;

	@Field(value="PK_ORG_USE")
    private String pkOrgUse;

	@Field(value="MODIFIER")
    private String modifier;

	@Field(value="MODITY_TIME")
    private Date modityTime;

    @Field(value="FLAG_DEFAULT")
    private String flagDefault;

    public String getPkTemporg(){
        return this.pkTemporg;
    }
    public void setPkTemporg(String pkTemporg){
        this.pkTemporg = pkTemporg;
    }

    public String getPkPvdoctemp(){
        return this.pkPvdoctemp;
    }
    public void setPkPvdoctemp(String pkPvdoctemp){
        this.pkPvdoctemp = pkPvdoctemp;
    }

    public String getPkOrgUse(){
        return this.pkOrgUse;
    }
    public void setPkOrgUse(String pkOrgUse){
        this.pkOrgUse = pkOrgUse;
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

    public String getFlagDefault() {
        return flagDefault;
    }

    public void setFlagDefault(String flagDefault) {
        this.flagDefault = flagDefault;
    }
}
