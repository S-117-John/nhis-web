package com.zebone.nhis.base.bd.vo;

import com.zebone.nhis.common.module.base.bd.wf.BdFlowBp;

public class BdFlowBpExt extends BdFlowBp {
	private String nameStep;
	private String sortno;
	private String euType;
	private String euDepttype;

	public String getEuDepttype() {
		return euDepttype;
	}

	public String getEuType() {
		return euType;
	}

	public String getNameStep() {
		return nameStep;
	}

	public String getSortno() {
		return sortno;
	}

	public void setEuDepttype(String euDepttype) {
		this.euDepttype = euDepttype;
	}

	public void setEuType(String euType) {
		this.euType = euType;
	}

	public void setNameStep(String nameStep) {
		this.nameStep = nameStep;
	}

	public void setSortno(String sortno) {
		this.sortno = sortno;
	}

}
