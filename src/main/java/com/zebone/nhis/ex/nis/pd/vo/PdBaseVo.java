package com.zebone.nhis.ex.nis.pd.vo;

import com.zebone.nhis.common.module.ex.nis.pd.BdPdBase;

public class PdBaseVo extends BdPdBase{

	private String spec;
	private String unitname;
	private String pdname;
	private String pdCode;
	private String spcode;
	
	public String getSpcode() {
		return spcode;
	}
	public void setSpcode(String spcode) {
		this.spcode = spcode;
	}
	public String getSpec() {
		return spec;
	}
	public void setSpec(String spec) {
		this.spec = spec;
	}
	public String getUnitname() {
		return unitname;
	}
	public void setUnitname(String unitname) {
		this.unitname = unitname;
	}
	public String getPdname() {
		return pdname;
	}
	public void setPdname(String pdname) {
		this.pdname = pdname;
	}
	public String getPdCode() {
		return pdCode;
	}
	public void setPdCode(String pdCode) {
		this.pdCode = pdCode;
	}
	
	
}
