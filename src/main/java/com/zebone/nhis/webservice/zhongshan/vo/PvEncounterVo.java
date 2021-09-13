package com.zebone.nhis.webservice.zhongshan.vo;

import com.zebone.nhis.common.module.pv.PvEncounter;

public class PvEncounterVo extends PvEncounter {
	private String codePi;//患者编码
	private String codeIp;//住院号
	
	public String getCodeIp() {
		return codeIp;
	}

	public void setCodeIp(String codeIp) {
		this.codeIp = codeIp;
	}

	public String getCodePi() {
		return codePi;
	}

	public void setCodePi(String codePi) {
		this.codePi = codePi;
	}
	
}
