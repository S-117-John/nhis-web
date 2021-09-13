package com.zebone.nhis.common.module.scm.pub;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;


@Table(value = "BD_PD_GN")
public class BdPdGn extends BaseModule{

	@PK
	@Field(value = "PK_PDGN", id = KeyId.UUID)
	private String pkPdgn;
	
	@Field(value="DT_PHARM")
	private String dtPharm;
	
	@Field(value="CODE")
	private String code;
	
	@Field(value="NAME")
	private String name;
	
	@Field(value="SPCODE")
	private String spcode;
	
	@Field(value="D_CODE")
	private String dcode;
	
	@Field(value="EU_DRUG_TYPE")
	private String euDrugType;
	
	@Field(value="MODITY_TIME")
	private Date modifyTime;

	private String state;
	
	public String getEuDrugType() {
		return euDrugType;
	}

	public void setEuDrugType(String euDrugType) {
		this.euDrugType = euDrugType;
	}

	public Date getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}

	
	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getPkPdgn() {
		return pkPdgn;
	}

	public void setPkPdgn(String pkPdgn) {
		this.pkPdgn = pkPdgn;
	}

	public String getDtPharm() {
		return dtPharm;
	}

	public void setDtPharm(String dtPharm) {
		this.dtPharm = dtPharm;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSpcode() {
		return spcode;
	}

	public void setSpcode(String spcode) {
		this.spcode = spcode;
	}

	public String getDcode() {
		return dcode;
	}

	public void setDcode(String dcode) {
		this.dcode = dcode;
	}

	

	

	
	
	
}
