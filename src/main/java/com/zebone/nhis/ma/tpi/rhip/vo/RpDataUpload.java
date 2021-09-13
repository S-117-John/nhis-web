package com.zebone.nhis.ma.tpi.rhip.vo;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;

@Table(value="RP_DATA_UPLOAD")
public class RpDataUpload extends BaseModule{

	@PK
    @Field(value="PK_RPDATA")
	private String pkRpData;
	
	@Field(value="OBJNAME")
	private String objName;
	
	@Field(value="PK_PV")
	private String pkPv;
	
	@Field(value="FLAG_UPLOAD")
	private String flagUpload;
	
	@Field(value="REMARK")
	private String remark;

	

	public String getPkRpData() {
		return pkRpData;
	}

	public void setPkRpData(String pkRpData) {
		this.pkRpData = pkRpData;
	}

	public String getObjName() {
		return objName;
	}

	public void setObjName(String objName) {
		this.objName = objName;
	}

	public String getPkPv() {
		return pkPv;
	}

	public void setPkPv(String pkPv) {
		this.pkPv = pkPv;
	}

	public String getFlagUpload() {
		return flagUpload;
	}

	public void setFlagUpload(String flagUpload) {
		this.flagUpload = flagUpload;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	
}
