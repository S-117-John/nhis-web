package com.zebone.nhis.common.module.base.bd.wf;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: BD_INTERFACE_EXT 
 *
 * @since 2018-12-17 06:04:30
 */
@Table(value="BD_INTERFACE_EXT")
public class BdInterfaceExt extends BaseModule  {

	@PK
	@Field(value="PK_INTERFACE",id=KeyId.UUID)
    private String pkInterface;

	@Field(value="CODE")
    private String code;

	@Field(value="NAME")
    private String name;

	@Field(value="SPCODE")
    private String spcode;

	@Field(value="PROVIDER")
    private String provider;

	@Field(value="CONFIG")
    private String config;

	@Field(value="FLAG_ACTIVE")
    private String flagActive;

	@Field(value="NOTE")
    private String note;

	@Field(value="MODIFIER")
    private String modifier;

	@Field(value="MODITY_TIME")
    private Date modityTime;


    public String getPkInterface(){
        return this.pkInterface;
    }
    public void setPkInterface(String pkInterface){
        this.pkInterface = pkInterface;
    }

    public String getCode(){
        return this.code;
    }
    public void setCode(String code){
        this.code = code;
    }

    public String getName(){
        return this.name;
    }
    public void setName(String name){
        this.name = name;
    }

    public String getSpcode(){
        return this.spcode;
    }
    public void setSpcode(String spcode){
        this.spcode = spcode;
    }

    public String getProvider(){
        return this.provider;
    }
    public void setProvider(String provider){
        this.provider = provider;
    }

    public String getConfig(){
        return this.config;
    }
    public void setConfig(String config){
        this.config = config;
    }

    public String getFlagActive(){
        return this.flagActive;
    }
    public void setFlagActive(String flagActive){
        this.flagActive = flagActive;
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