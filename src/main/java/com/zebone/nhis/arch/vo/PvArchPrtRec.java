package com.zebone.nhis.arch.vo;

import java.util.Date;
import java.util.List;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;

@Table(value="PV_ARCH_PRT_REC")
public class PvArchPrtRec extends BaseModule {

	@PK
    @Field(value="PK_PRT_REC")
    private String pkPrtRec;
	
	@Field(value="pk_pv")
    private String pkPv;
	
	@Field(value="date_prt")
    private Date datePrt;
	
	@Field(value="eu_type")
    private String euType;
	
	@Field(value="pk_emp_prt")
    private String pkEmpPrt;
	
	@Field(value="name_emp_prt")
    private String nameEmpPrt;

	private List<PvArchPrt> pvArchPrtList;
	

	public List<PvArchPrt> getPvArchPrtList() {
		return pvArchPrtList;
	}

	public void setPvArchPrtList(List<PvArchPrt> pvArchPrtList) {
		this.pvArchPrtList = pvArchPrtList;
	}

	public String getPkPrtRec() {
		return pkPrtRec;
	}

	public void setPkPrtRec(String pkPrtRec) {
		this.pkPrtRec = pkPrtRec;
	}

	public String getPkPv() {
		return pkPv;
	}

	public void setPkPv(String pkPv) {
		this.pkPv = pkPv;
	}

	public Date getDatePrt() {
		return datePrt;
	}

	public void setDatePrt(Date datePrt) {
		this.datePrt = datePrt;
	}

	public String getEuType() {
		return euType;
	}

	public void setEuType(String euType) {
		this.euType = euType;
	}

	public String getPkEmpPrt() {
		return pkEmpPrt;
	}

	public void setPkEmpPrt(String pkEmpPrt) {
		this.pkEmpPrt = pkEmpPrt;
	}

	public String getNameEmpPrt() {
		return nameEmpPrt;
	}

	public void setNameEmpPrt(String nameEmpPrt) {
		this.nameEmpPrt = nameEmpPrt;
	}
	
	
}
