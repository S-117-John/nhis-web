package com.zebone.nhis.scm.material.vo;

import java.util.Date;
import java.util.List;

import com.zebone.nhis.common.module.scm.purchase.PdPlan;

public class MtlPdPlanVo extends PdPlan{
	private String plantypeName;
	private String orgAccName;
	private String deptAccName;
	private String storeAccName;
	private String planrecurName;
	private Date dateNeed;
	
	private List<MtlPdPlanDtVo> dtList;
	private List<Object[]> delDtList;
	
	public String getPlantypeName() {
		return plantypeName;
	}
	public void setPlantypeName(String plantypeName) {
		this.plantypeName = plantypeName;
	}
	public String getOrgAccName() {
		return orgAccName;
	}
	public void setOrgAccName(String orgAccName) {
		this.orgAccName = orgAccName;
	}
	public String getDeptAccName() {
		return deptAccName;
	}
	public void setDeptAccName(String deptAccName) {
		this.deptAccName = deptAccName;
	}
	public String getStoreAccName() {
		return storeAccName;
	}
	public void setStoreAccName(String storeAccName) {
		this.storeAccName = storeAccName;
	}
	public List<MtlPdPlanDtVo> getDtList() {
		return dtList;
	}
	public void setDtList(List<MtlPdPlanDtVo> dtList) {
		this.dtList = dtList;
	}
	public List<Object[]> getDelDtList() {
		return delDtList;
	}
	public void setDelDtList(List<Object[]> delDtList) {
		this.delDtList = delDtList;
	}
	public String getPlanrecurName() {
		return planrecurName;
	}
	public void setPlanrecurName(String planrecurName) {
		this.planrecurName = planrecurName;
	}
	public Date getDateNeed() {
		return dateNeed;
	}
	public void setDateNeed(Date dateNeed) {
		this.dateNeed = dateNeed;
	}


}
