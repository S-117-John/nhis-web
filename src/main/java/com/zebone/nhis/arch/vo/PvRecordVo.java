package com.zebone.nhis.arch.vo;

import com.zebone.nhis.common.module.pv.PvEncounter;

public class PvRecordVo extends PvEncounter{
	
	private String CodePi;
	
	private Integer ipTimes;
	
	private String NameDept;
	
	private String PkArchive;

	public String getCodePi() {
		return CodePi;
	}

	public void setCodePi(String codePi) {
		CodePi = codePi;
	}

	public Integer getIpTimes() {
		return ipTimes;
	}

	public void setIpTimes(Integer ipTimes) {
		this.ipTimes = ipTimes;
	}

	public String getNameDept() {
		return NameDept;
	}

	public void setNameDept(String nameDept) {
		NameDept = nameDept;
	}

	public String getPkArchive() {
		return PkArchive;
	}

	public void setPkArchive(String pkArchive) {
		PkArchive = pkArchive;
	}

}
