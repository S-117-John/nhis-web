package com.zebone.nhis.ex.nis.pi.vo;

import com.zebone.nhis.common.module.pv.PvClinicGroup;

public class PvClinicGroupVo extends PvClinicGroup {
	private String nameDept;//科室名称
	private String nameDeptNs;//病区名称
	private String nameModifier;//修改人
	private String nameWg;//医疗组名称
	
	public String getNameDept() {
		return nameDept;
	}
	public void setNameDept(String nameDept) {
		this.nameDept = nameDept;
	}
	public String getNameDeptNs() {
		return nameDeptNs;
	}
	public void setNameDeptNs(String nameDeptNs) {
		this.nameDeptNs = nameDeptNs;
	}
	public String getNameModifier() {
		return nameModifier;
	}
	public void setNameModifier(String nameModifier) {
		this.nameModifier = nameModifier;
	}
	public String getNameWg() {
		return nameWg;
	}
	public void setNameWg(String nameWg) {
		this.nameWg = nameWg;
	}
}
