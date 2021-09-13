package com.zebone.nhis.ma.tpi.rhip.vo;

import com.zebone.nhis.common.module.cn.ipdw.CnOpApply;


public class RhipCnOpApply extends CnOpApply {
	//术前诊断
	private String codeDiag;
	private String nameDiag;
	private String codeOp;
	private String nameOp;
	private String codeEmpPhyOp;
	private String deptCodeExec;
	private String anaeName;
	private String deptExecName;
	private String empCodeAsis;
	private String empCodeAsis2;
	
	public String getCodeDiag() {
		return codeDiag;
	}
	public void setCodeDiag(String codeDiag) {
		this.codeDiag = codeDiag;
	}
	public String getCodeOp() {
		return codeOp;
	}
	public void setCodeOp(String codeOp) {
		this.codeOp = codeOp;
	}
	public String getNameOp() {
		return nameOp;
	}
	public void setNameOp(String nameOp) {
		this.nameOp = nameOp;
	}
	public String getCodeEmpPhyOp() {
		return codeEmpPhyOp;
	}
	public void setCodeEmpPhyOp(String codeEmpPhyOp) {
		this.codeEmpPhyOp = codeEmpPhyOp;
	}
	public String getNameDiag() {
		return nameDiag;
	}
	public void setNameDiag(String nameDiag) {
		this.nameDiag = nameDiag;
	}
	public String getDeptCodeExec() {
		return deptCodeExec;
	}
	public void setDeptCodeExec(String deptCodeExec) {
		this.deptCodeExec = deptCodeExec;
	}
	public String getAnaeName() {
		return anaeName;
	}
	public void setAnaeName(String anaeName) {
		this.anaeName = anaeName;
	}
	public String getDeptExecName() {
		return deptExecName;
	}
	public void setDeptExecName(String deptExecName) {
		this.deptExecName = deptExecName;
	}
	public String getEmpCodeAsis2() {
		return empCodeAsis2;
	}
	public void setEmpCodeAsis2(String empCodeAsis2) {
		this.empCodeAsis2 = empCodeAsis2;
	}
	public String getEmpCodeAsis() {
		return empCodeAsis;
	}
	public void setEmpCodeAsis(String empCodeAsis) {
		this.empCodeAsis = empCodeAsis;
	}

	
}
