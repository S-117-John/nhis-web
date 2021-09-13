package com.zebone.nhis.common.module.base.bd.srv;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: BD_QC_PLATFORM 
 *
 * @since 2019-03-18 04:53:50
 */
@Table(value="BD_QC_PLATFORM")
public class BdQcPlatform extends BaseModule  {

	@PK
	@Field(value="PK_QCPLATFORM",id=KeyId.UUID)
    private String pkQcplatform;

	@Field(value="CODE")
    private String code;

	@Field(value="NAME")
    private String name;

	@Field(value="SPCODE")
    private String spcode;

	@Field(value="SCREEN")
    private String screen;

	@Field(value="VOICE")
    private String voice;

	@Field(value="PK_QCRULE_DEF")
    private String pkQcruleDef;

	@Field(value="FLAG_AUTO")
    private String flagAuto;

	@Field(value="NOTE")
    private String note;

	@Field(value="FLAG_ACTIVE")
    private String flagActive;

	@Field(value="PK_ORGAREA")
    private String pkOrgarea;

	@Field(value="MODIFIER")
    private String modifier;

	@Field(value="MODITY_TIME")
    private Date modityTime;


    public String getPkQcplatform(){
        return this.pkQcplatform;
    }
    public void setPkQcplatform(String pkQcplatform){
        this.pkQcplatform = pkQcplatform;
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

    public String getScreen(){
        return this.screen;
    }
    public void setScreen(String screen){
        this.screen = screen;
    }

    public String getVoice(){
        return this.voice;
    }
    public void setVoice(String voice){
        this.voice = voice;
    }

    public String getPkQcruleDef(){
        return this.pkQcruleDef;
    }
    public void setPkQcruleDef(String pkQcruleDef){
        this.pkQcruleDef = pkQcruleDef;
    }

    public String getFlagAuto(){
        return this.flagAuto;
    }
    public void setFlagAuto(String flagAuto){
        this.flagAuto = flagAuto;
    }

    public String getNote(){
        return this.note;
    }
    public void setNote(String note){
        this.note = note;
    }

    public String getFlagActive(){
        return this.flagActive;
    }
    public void setFlagActive(String flagActive){
        this.flagActive = flagActive;
    }

    public String getPkOrgarea(){
        return this.pkOrgarea;
    }
    public void setPkOrgarea(String pkOrgarea){
        this.pkOrgarea = pkOrgarea;
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