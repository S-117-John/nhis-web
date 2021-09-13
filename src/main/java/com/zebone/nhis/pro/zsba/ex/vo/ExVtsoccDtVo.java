package com.zebone.nhis.pro.zsba.ex.vo;

import java.math.BigDecimal;

import com.zebone.nhis.common.module.ex.nis.emr.ExVtsOccDt;

public class ExVtsoccDtVo extends ExVtsOccDt{
	
	private BigDecimal addValTemp;
	
	private String nameEmp;
	
    private String pkVtsoccdtDt;
	
	
	public BigDecimal getAddValTemp() {
		return addValTemp;
	}

	public void setAddValTemp(BigDecimal addValTemp) {
		this.addValTemp = addValTemp;
	}

	public String getNameEmp() {
		return nameEmp;
	}

	public void setNameEmp(String nameEmp) {
		this.nameEmp = nameEmp;
	}

	public String getPkVtsoccdtDt() {
		return pkVtsoccdtDt;
	}

	public void setPkVtsoccdtDt(String pkVtsoccdtDt) {
		this.pkVtsoccdtDt = pkVtsoccdtDt;
	}		

}
