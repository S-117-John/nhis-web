package com.zebone.nhis.cn.ipdw.vo;

import java.util.Date;


public class BdFlowSpVo{

	//是否适用审批流
	private String pkFlow;
	private String codeFlow;
	private String nameFlow;
	private String euType;
	private String pkFlowstep;
	private Integer sortno;
	private String nameStep;
	private String euDepttype;
	private String pkDept;
	private String pkEmp;
	private String nameEmp;
	
	//审批信息
	private String nameDept;
	private String euResult;
	private String note;
	private Date dateChk;
	
	
	public String getPkFlow() {
		return pkFlow;
	}
	public void setPkFlow(String pkFlow) {
		this.pkFlow = pkFlow;
	}
	public String getCodeFlow() {
		return codeFlow;
	}
	public void setCodeFlow(String codeFlow) {
		this.codeFlow = codeFlow;
	}
	public String getNameFlow() {
		return nameFlow;
	}
	public void setNameFlow(String nameFlow) {
		this.nameFlow = nameFlow;
	}
	public String getEuType() {
		return euType;
	}
	public void setEuType(String euType) {
		this.euType = euType;
	}
	public String getPkFlowstep() {
		return pkFlowstep;
	}
	public void setPkFlowstep(String pkFlowstep) {
		this.pkFlowstep = pkFlowstep;
	}
	public Integer getSortno() {
		return sortno;
	}
	public void setSortno(Integer sortno) {
		this.sortno = sortno;
	}
	public String getNameStep() {
		return nameStep;
	}
	public void setNameStep(String nameStep) {
		this.nameStep = nameStep;
	}
	public String getEuDepttype() {
		return euDepttype;
	}
	public void setEuDepttype(String euDepttype) {
		this.euDepttype = euDepttype;
	}
	public String getPkDept() {
		return pkDept;
	}
	public void setPkDept(String pkDept) {
		this.pkDept = pkDept;
	}
	public String getPkEmp() {
		return pkEmp;
	}
	public void setPkEmp(String pkEmp) {
		this.pkEmp = pkEmp;
	}
	public String getNameEmp() {
		return nameEmp;
	}
	public void setNameEmp(String nameEmp) {
		this.nameEmp = nameEmp;
	}
	public String getNameDept() {
		return nameDept;
	}
	public void setNameDept(String nameDept) {
		this.nameDept = nameDept;
	}
	public String getEuResult() {
		return euResult;
	}
	public void setEuResult(String euResult) {
		this.euResult = euResult;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public Date getDateChk() {
		return dateChk;
	}
	public void setDateChk(Date dateChk) {
		this.dateChk = dateChk;
	}

	
}
