package com.zebone.nhis.ex.pub.vo;

import com.zebone.nhis.common.module.pv.PvEncounter;

public class PvEncounterVo extends PvEncounter {
	private String dtCountry;
	private String dtNation;
	private String mobile;
	/**
	 * 证据类型
	 */
	private String dtIdtype;

	public String getDtCountry() {
		return dtCountry;
	}

	public void setDtCountry(String dtCountry) {
		this.dtCountry = dtCountry;
	}

	public String getDtNation() {
		return dtNation;
	}

	public void setDtNation(String dtNation) {
		this.dtNation = dtNation;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getDtIdtype() {
		return dtIdtype;
	}

	public void setDtIdtype(String dtIdtype) {
		this.dtIdtype = dtIdtype;
	}

}
