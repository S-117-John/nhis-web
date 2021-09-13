package com.zebone.nhis.common.module.base.bd.wf;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;

@Table(value="BD_FLOW_STEP")
public class BdFlowStep extends BaseModule{

	@PK
	@Field(value="PK_FLOWSTEP",id=KeyId.UUID)
    private String pkFlowstep;
	
	@Field(value="PK_FLOW")
	private String pkFlow;
	
	@Field(value="sortno")
	private Integer sortno;
	
	@Field(value="NAME_STEP")
	private String nameStep;
	
	@Field(value="FLAG_START")
	private String flagStart;
	
	@Field(value="FLAG_END")
	private String flagEnd;
	
	@Field(value="EU_DEPTTYPE")
	private String euDepttype;
	
	@Field(value="PK_DEPT")
	private String pkDept;
	
	@Field(value="PK_EMP")
	private String pkEmp;
	
	@Field(value="NAME_EMP")
	private String nameEmp;
	
	@Field(value="note")
	private String note;

	public String getPkFlowstep() {
		return pkFlowstep;
	}

	public void setPkFlowstep(String pkFlowstep) {
		this.pkFlowstep = pkFlowstep;
	}

	public String getPkFlow() {
		return pkFlow;
	}

	public void setPkFlow(String pkFlow) {
		this.pkFlow = pkFlow;
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

	public String getFlagStart() {
		return flagStart;
	}

	public void setFlagStart(String flagStart) {
		this.flagStart = flagStart;
	}

	public String getFlagEnd() {
		return flagEnd;
	}

	public void setFlagEnd(String flagEnd) {
		this.flagEnd = flagEnd;
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

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}
	
	
}
