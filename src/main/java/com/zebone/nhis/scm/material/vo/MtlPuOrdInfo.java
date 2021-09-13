package com.zebone.nhis.scm.material.vo;

import java.util.Date;
import java.util.List;

import com.zebone.nhis.common.module.scm.purchase.PdPurchase;


@SuppressWarnings("serial")
public class MtlPuOrdInfo extends PdPurchase {
	
	private List<MtlPuOrdDtInfo> dt;
	//采购计划主键
	private String pkPdPlan;
	private String nameDept;
	private String nameSpr;
	private Date dateValidRun;
	private Date dateValidLicense;
	private String putype;
	
	private List<Object[]> delDtList;//删除的明细主键

	public List<MtlPuOrdDtInfo> getDt() {
		return dt;
	}

	public void setDt(List<MtlPuOrdDtInfo> dt) {
		this.dt = dt;
	}

	public String getPkPdPlan() {
		return pkPdPlan;
	}

	public void setPkPdPlan(String pkPdPlan) {
		this.pkPdPlan = pkPdPlan;
	}

	public String getNameDept() {
		return nameDept;
	}

	public void setNameDept(String nameDept) {
		this.nameDept = nameDept;
	}

	public String getNameSpr() {
		return nameSpr;
	}

	public void setNameSpr(String nameSpr) {
		this.nameSpr = nameSpr;
	}

	public Date getDateValidRun() {
		return dateValidRun;
	}

	public void setDateValidRun(Date dateValidRun) {
		this.dateValidRun = dateValidRun;
	}

	public Date getDateValidLicense() {
		return dateValidLicense;
	}

	public void setDateValidLicense(Date dateValidLicense) {
		this.dateValidLicense = dateValidLicense;
	}

	public String getPutype() {
		return putype;
	}

	public void setPutype(String putype) {
		this.putype = putype;
	}

	public List<Object[]> getDelDtList() {
		return delDtList;
	}

	public void setDelDtList(List<Object[]> delDtList) {
		this.delDtList = delDtList;
	}

}
