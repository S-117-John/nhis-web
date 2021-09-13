package com.zebone.nhis.webservice.sd.vo;

import com.zebone.nhis.common.module.cn.ipdw.CnOrder;

public class CnOrderVo extends CnOrder {

	public String rowStatus;
	public String codePv;
	public String codePi;

	public String codeOrgExec;
	public String codeDept;
	public String codeDeptExec;
	public String codeModifier;
	public String codeUnit;
	public String codeUnitDos;
	public String codeUnitPd;
	public String codeUnitCg;

	public String getRowStatus() {
		return rowStatus;
	}

	public void setRowStatus(String rowStatus) {
		this.rowStatus = rowStatus;
	}

	public String getCodePv() {
		return codePv;
	}

	public void setCodePv(String codePv) {
		this.codePv = codePv;
	}

	public String getCodePi() {
		return codePi;
	}

	public void setCodePi(String codePi) {
		this.codePi = codePi;
	}

	public String getCodeOrgExec() {
		return codeOrgExec;
	}

	public void setCodeOrgExec(String codeOrgExec) {
		this.codeOrgExec = codeOrgExec;
	}

	public String getCodeDept() {
		return codeDept;
	}

	public void setCodeDept(String codeDept) {
		this.codeDept = codeDept;
	}

	public String getCodeDeptExec() {
		return codeDeptExec;
	}

	public void setCodeDeptExec(String codeDeptExec) {
		this.codeDeptExec = codeDeptExec;
	}

	public String getCodeModifier() {
		return codeModifier;
	}

	public void setCodeModifier(String codeModifier) {
		this.codeModifier = codeModifier;
	}

	public String getCodeUnitDos() {
		return codeUnitDos;
	}

	public void setCodeUnitDos(String codeUnitDos) {
		this.codeUnitDos = codeUnitDos;
	}

	public String getCodeUnit() {
		return codeUnit;
	}

	public void setCodeUnit(String codeUnit) {
		this.codeUnit = codeUnit;
	}

	public String getCodeUnitPd() {
		return codeUnitPd;
	}

	public void setCodeUnitPd(String codeUnitPd) {
		this.codeUnitPd = codeUnitPd;
	}

	public String getCodeUnitCg() {
		return codeUnitCg;
	}

	public void setCodeUnitCg(String codeUnitCg) {
		this.codeUnitCg = codeUnitCg;
	}

}
