package com.zebone.nhis.emr.rec.tmp.vo;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;

@Table(value="BD_PVDOC_TEMP_ORG")
public class BdPvdocTempOrgVo  extends BaseModule{

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

	@Field(value="PK_DEPT")
    private String pkDept;

	@Field(value="FLAG_DEFAULT")
	private String flagDefault;

	public String getDelFlag() {
		return delFlag;
	}
	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}
	@Field(value="DEL_FLAG")
	private String delFlag;
    
	public String getPkDept() {
		return pkDept;
	}
	public void setPkDept(String pkDept) {
		this.pkDept = pkDept;
	}
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
