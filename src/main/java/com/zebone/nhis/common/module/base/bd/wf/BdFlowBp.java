package com.zebone.nhis.common.module.base.bd.wf;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;

@Table(value="BD_FLOW_BP")
public class BdFlowBp extends BaseModule{

	@PK
	@Field(value="PK_FLOWBP",id=KeyId.UUID)
    private String pkFlowbp;
	
	@Field(value="PK_FLOW")
	private String pkFlow;
	
	@Field(value="PK_BPPK")
	private String pkBppk;
	
	@Field(value="CODE_FLOW")
	private String codeFlow;
	
	@Field(value="NAME_FLOW")
	private String nameFlow;
	
	@Field(value="DATE_SUBMIT")
	private Date dateSubmit;
	
	@Field(value="PK_DEPT")
	private String pkDept;
	
	@Field(value="PK_EMP")
	private String pkEmp;
	
	@Field(value="NAME_EMP")
	private String nameEmp;
		
	@Field(value="DATE_CHK")
	private Date dateChk;
	
	@Field(value="EU_RESULT")
	private String euResult;
	
	@Field(value="PK_FLOWSTEP_PRE")
	private String pkFlowstepPre;
	
	@Field(value="PK_FLOWSTEP")
	private String pkFlowstep;
	
	@Field(value="note")
	private String note;

	public String getPkFlowbp() {
		return pkFlowbp;
	}

	public void setPkFlowbp(String pkFlowbp) {
		this.pkFlowbp = pkFlowbp;
	}

	public String getPkFlow() {
		return pkFlow;
	}

	public void setPkFlow(String pkFlow) {
		this.pkFlow = pkFlow;
	}

	public String getPkBppk() {
		return pkBppk;
	}

	public void setPkBppk(String pkBppk) {
		this.pkBppk = pkBppk;
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

	public Date getDateSubmit() {
		return dateSubmit;
	}

	public void setDateSubmit(Date dateSubmit) {
		this.dateSubmit = dateSubmit;
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

	public Date getDateChk() {
		return dateChk;
	}

	public void setDateChk(Date dateChk) {
		this.dateChk = dateChk;
	}

	public String getEuResult() {
		return euResult;
	}

	public void setEuResult(String euResult) {
		this.euResult = euResult;
	}

	public String getPkFlowstepPre() {
		return pkFlowstepPre;
	}

	public void setPkFlowstepPre(String pkFlowstepPre) {
		this.pkFlowstepPre = pkFlowstepPre;
	}

	public String getPkFlowstep() {
		return pkFlowstep;
	}

	public void setPkFlowstep(String pkFlowstep) {
		this.pkFlowstep = pkFlowstep;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getNameEmp() {
		return nameEmp;
	}

	public void setNameEmp(String nameEmp) {
		this.nameEmp = nameEmp;
	}
}
