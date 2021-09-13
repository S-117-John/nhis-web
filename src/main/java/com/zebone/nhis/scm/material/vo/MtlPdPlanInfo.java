package com.zebone.nhis.scm.material.vo;

import com.zebone.nhis.common.module.scm.purchase.PdPlan;

@SuppressWarnings("serial")
public class MtlPdPlanInfo extends PdPlan {
	private String nameDept;
	private String nameStore;
	private String namePlatype;
	public String getNameDept() {
		return nameDept;
	}
	public void setNameDept(String nameDept) {
		this.nameDept = nameDept;
	}
	public String getNameStore() {
		return nameStore;
	}
	public void setNameStore(String nameStore) {
		this.nameStore = nameStore;
	}
	public String getNamePlatype() {
		return namePlatype;
	}
	public void setNamePlatype(String namePlatype) {
		this.namePlatype = namePlatype;
	}
}
