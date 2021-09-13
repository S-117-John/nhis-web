package com.zebone.nhis.ex.pub.vo;

import com.zebone.nhis.common.module.base.bd.wf.BdWfOrdArguDept;
/**
 * 
 * @author yangxue
 *
 */
public class BdWfOrdArguDeptVo extends BdWfOrdArguDept{
	private String nameOrgExec;
	private String nameDept;
	private String codeOrgExec;
	private String codeDept;
	public String getNameOrgExec() {
		return nameOrgExec;
	}
	public void setNameOrgExec(String nameOrgExec) {
		this.nameOrgExec = nameOrgExec;
	}
	public String getNameDept() {
		return nameDept;
	}
	public void setNameDept(String nameDept) {
		this.nameDept = nameDept;
	}
	public String getCodeOrgExec() {
		return codeOrgExec;
	}
	public void setCodeOrgExec(String codeOrgExec) {
		this.codeOrgExec = codeOrgExec;
	}
	public String getCodeDept() {
		return codeDept;
	}
	public void setCodeDept(String codeDept) {
		this.codeDept = codeDept;
	}
	

}
