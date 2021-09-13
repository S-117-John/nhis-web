package com.zebone.nhis.emr.nis.vo;

import java.util.Date;

import com.zebone.nhis.common.module.pv.PvEncounter;

/**
 * 挂号信息
 * 
 * @author yuanxinan
 *
 */
public class RegisInfoParam extends PvEncounter {

	private String dtTglevel;

	private Date outEnd;

	private String codePi;

	public String getDtTglevel() {
		return dtTglevel;
	}

	public void setDtTglevel(String dtTglevel) {
		this.dtTglevel = dtTglevel;
	}

	public Date getOutEnd() {
		return outEnd;
	}

	public void setOutEnd(Date outEnd) {
		this.outEnd = outEnd;
	}

	public String getCodePi() {
		return codePi;
	}

	public void setCodePi(String codePi) {
		this.codePi = codePi;
	}

}
