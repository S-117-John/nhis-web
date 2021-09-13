package com.zebone.nhis.common.module.cn.opdw;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;

@Table(value="BD_PVDOC_TEMP")
public class BdPvDocTemp extends BaseModule{
	
	@PK
	@Field(value="PK_PVDOCTEMP",id=KeyId.UUID)
    private String pkPvdoctemp;

	@Field(value="CODE")
    private String code;

	@Field(value="NAME")
    private String name;

	@Field(value="SPCODE")
    private String spcode;

	@Field(value="D_CODE")
    private String dCode;

	@Field(value="DATA_DOC")
    private byte[] dataDoc;

	@Field(value="PK_FATHER")
    private String pkFather;

	@Field(value="FLAG_ACTIVE")
    private String flagActive;

	@Field(value="MODIFIER")
    private String modifier;

	@Field(value="MODITY_TIME")
    private Date modityTime;

    @Field(value="FLAG_DEF")
	private String flagDef;

    @Field(value="FLAG_EMR")
	private String flagEmr;
    
    private String tmpTitle;
    
    public String getPkPvdoctemp(){
        return this.pkPvdoctemp;
    }
    public void setPkPvdoctemp(String pkPvdoctemp){
        this.pkPvdoctemp = pkPvdoctemp;
    }

    public String getFlagEmr() {
        return flagEmr;
    }
    public void setFlagEmr(String flagEmr)
    {
      this.flagEmr=flagEmr;
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

    public String getdCode(){
        return this.dCode;
    }
    public void setdCode(String dCode){
        this.dCode = dCode;
    }

    public byte[] getDataDoc(){
        return this.dataDoc;
    }
    public void setDataDoc(byte[] dataDoc){
        this.dataDoc = dataDoc;
    }

    public String getPkFather(){
        return this.pkFather;
    }
    public void setPkFather(String pkFather){
        this.pkFather = pkFather;
    }

    public String getFlagActive(){
        return this.flagActive;
    }
    public void setFlagActive(String flagActive){
        this.flagActive = flagActive;
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
	public String getFlagDef() {
		return flagDef;
	}
	public void setFlagDef(String flagDef) {
		this.flagDef = flagDef;
	}
	public String getTmpTitle() {
		return tmpTitle;
	}
	public void setTmpTitle(String tmpTitle) {
		this.tmpTitle = tmpTitle;
	}

}
