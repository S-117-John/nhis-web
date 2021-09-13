package com.zebone.nhis.compay.ins.shenzhen.vo.szxnh;

import com.zebone.platform.modules.dao.build.au.Field;

public class DiagVo {
	@Field(value = "DescDiag")
    private String descDiag ;

	@Field(value = "CodeDiag")
    private String codeDiag ;

	public String getDescDiag() {
		return descDiag;
	}

	public void setDescDiag(String descDiag) {
		this.descDiag = descDiag;
	}

	public String getCodeDiag() {
		return codeDiag;
	}

	public void setCodeDiag(String codeDiag) {
		this.codeDiag = codeDiag;
	}	
	
}
