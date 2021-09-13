package com.zebone.nhis.webservice.sd.vo;

import com.zebone.nhis.common.module.bl.opcg.BlOpDt;

public class BlOpDtVo extends BlOpDt {

	public String itemCode;
	public String codeDisc;
	public String codeUnitPd;
	public String codeUnit;

	public String getItemCode() {
		return itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	public String getCodeUnitPd() {
		return codeUnitPd;
	}

	public void setCodeUnitPd(String codeUnitPd) {
		this.codeUnitPd = codeUnitPd;
	}

	public String getCodeUnit() {
		return codeUnit;
	}

	public void setCodeUnit(String codeUnit) {
		this.codeUnit = codeUnit;
	}

	public String getCodeDisc() {
		return codeDisc;
	}

	public void setCodeDisc(String codeDisc) {
		this.codeDisc = codeDisc;
	}

}
