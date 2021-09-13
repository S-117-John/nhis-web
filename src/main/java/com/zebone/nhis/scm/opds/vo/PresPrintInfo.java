package com.zebone.nhis.scm.opds.vo;

import java.util.Date;
import java.util.List;

public class PresPrintInfo {
	
	/***表头信息**/
	private String nameOrg;
	private String namePi;
	private String nameSex;
	private String namePresType;
	private Date datePres;
	private String nameDept;
	private String age;
	private String nameDiag;
	private String presNo;
	private String codePv;
	
	
	/**表体信息*/
	private List<PresDtPrintInfo> presDt;

	public List<PresDtPrintInfo> getPresDt() {
		return presDt;
	}

	public void setPresDt(List<PresDtPrintInfo> presDt) {
		this.presDt = presDt;
	}

	public String getNameOrg() {
		return nameOrg;
	}

	public void setNameOrg(String nameOrg) {
		this.nameOrg = nameOrg;
	}

	public String getNamePi() {
		return namePi;
	}

	public void setNamePi(String namePi) {
		this.namePi = namePi;
	}

	public String getNameSex() {
		return nameSex;
	}

	public void setNameSex(String nameSex) {
		this.nameSex = nameSex;
	}

	public String getNamePresType() {
		return namePresType;
	}

	public void setNamePresType(String namePresType) {
		this.namePresType = namePresType;
	}

	

	public Date getDatePres() {
		return datePres;
	}

	public void setDatePres(Date datePres) {
		this.datePres = datePres;
	}

	public String getNameDept() {
		return nameDept;
	}

	public void setNameDept(String nameDept) {
		this.nameDept = nameDept;
	}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public String getNameDiag() {
		return nameDiag;
	}

	public void setNameDiag(String nameDiag) {
		this.nameDiag = nameDiag;
	}

	public String getPresNo() {
		return presNo;
	}

	public void setPresNo(String presNo) {
		this.presNo = presNo;
	}

	public String getCodePv() {
		return codePv;
	}

	public void setCodePv(String codePv) {
		this.codePv = codePv;
	}

	
	
}
