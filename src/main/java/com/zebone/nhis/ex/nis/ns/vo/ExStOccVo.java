package com.zebone.nhis.ex.nis.ns.vo;

import java.util.Date;
import java.util.List;

import com.zebone.nhis.common.module.ex.nis.ns.ExStOcc;

public class ExStOccVo extends ExStOcc{

	private String pkExocc;
	private Date datePlan;
	private Double quanOcc;
	private Integer packSize;
	private String pkUnit;
	private Date dateEndSt;
	//执行时间是否取开始时间--深大项目
	private String isExDateGetStartDate;
	//关联医嘱主键集合
	private List<String> ordList;

	public Date getDateEndSt() {
		return dateEndSt;
	}

	public void setDateEndSt(Date dateEndSt) {
		this.dateEndSt = dateEndSt;
	}

	public List<String> getOrdList() {
		return ordList;
	}
	public void setOrdList(List<String> ordList) {
		this.ordList = ordList;
	}

	public String getPkUnit() {
		return pkUnit;
	}
	public void setPkUnit(String pkUnit) {
		this.pkUnit = pkUnit;
	}
	public Double getQuanOcc() {
		return quanOcc;
	}
	public void setQuanOcc(Double quanOcc) {
		this.quanOcc = quanOcc;
	}
	public String getPkExocc() {
		return pkExocc;
	}
	public void setPkExocc(String pkExocc) {
		this.pkExocc = pkExocc;
	}
	public Date getDatePlan() {
		return datePlan;
	}
	public void setDatePlan(Date datePlan) {
		this.datePlan = datePlan;
	}
	public Integer getPackSize() {
		return packSize;
	}
	public void setPackSize(Integer packSize) {
		this.packSize = packSize;
	}

	public String getIsExDateGetStartDate() {
		return isExDateGetStartDate;
	}

	public void setIsExDateGetStartDate(String isExDateGetStartDate) {
		this.isExDateGetStartDate = isExDateGetStartDate;
	}
	
}
