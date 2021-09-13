package com.zebone.nhis.pro.lb.arch.vo;

import java.util.Date;

public class PatientArchVo {

	private String pkPv;
	private String codeIp;
	private String namePi;
	private String dtSex;
	private Date birthDate;
	private String agePv;
	private String euStatus;
	private Date dateBegin;
	private Date dateEnd;
	private String pkDept;
	private String nameDept;
	private String pkDeptNs;
	private String nameDeptNs;
	private Integer cntArch;
	private String nameHp;
	
	public String getPkPv() {
		return pkPv;
	}

	public void setPkPv(String pkPv) {
		this.pkPv = pkPv;
	}

	public String getCodeIp() {
		return codeIp;
	}

	public void setCodeIp(String codeIp) {
		this.codeIp = codeIp;
	}

	public String getNamePi() {
		return namePi;
	}

	public void setNamePi(String namePi) {
		this.namePi = namePi;
	}

	public String getDtSex() {
		return dtSex;
	}

	public void setDtSex(String dtSex) {
		this.dtSex = dtSex;
	}

	public Date getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}

	public String getAgePv() {
		return agePv;
	}

	public void setAgePv(String agePv) {
		this.agePv = agePv;
	}

	public String getEuStatus() {
		return euStatus;
	}

	public void setEuStatus(String euStatus) {
		this.euStatus = euStatus;
	}

	public Date getDateBegin() {
		return dateBegin;
	}

	public void setDateBegin(Date dateBegin) {
		this.dateBegin = dateBegin;
	}

	public Date getDateEnd() {
		return dateEnd;
	}

	public void setDateEnd(Date dateEnd) {
		this.dateEnd = dateEnd;
	}

	public String getPkDept() {
		return pkDept;
	}

	public void setPkDept(String pkDept) {
		this.pkDept = pkDept;
	}

	public String getPkDeptNs() {
		return pkDeptNs;
	}

	public void setPkDeptNs(String pkDeptNs) {
		this.pkDeptNs = pkDeptNs;
	}

	public Integer getCntArch() {
		return cntArch;
	}

	public void setCntArch(Integer cntArch) {
		this.cntArch = cntArch;
	}

	public String getNameDept() {
		return nameDept;
	}

	public void setNameDept(String nameDept) {
		this.nameDept = nameDept;
	}

	public String getNameDeptNs() {
		return nameDeptNs;
	}

	public void setNameDeptNs(String nameDeptNs) {
		this.nameDeptNs = nameDeptNs;
	}

	public String getNameHp() {
		return nameHp;
	}

	public void setNameHp(String nameHp) {
		this.nameHp = nameHp;
	}

}
