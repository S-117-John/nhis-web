package com.zebone.nhis.sch.appt.vo;

import java.util.Date;

import com.zebone.nhis.common.module.sch.appt.SchApptApply;

public class SchApptApplyVo extends SchApptApply {
	
	private String codePi;
	
	private String namePi;
	
	private String mobile;
	
	private String dtSex;
	
	private String dtIdtype;
	
	private String idNo;
	
	private Date birthDate;
	
	private String schResName;
	
	private String schSrvName;

	public String getCodePi() {
		return codePi;
	}

	public void setCodePi(String codePi) {
		this.codePi = codePi;
	}

	public String getNamePi() {
		return namePi;
	}

	public void setNamePi(String namePi) {
		this.namePi = namePi;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getSchResName() {
		return schResName;
	}

	public void setSchResName(String schResName) {
		this.schResName = schResName;
	}

	public String getSchSrvName() {
		return schSrvName;
	}

	public void setSchSrvName(String schSrvName) {
		this.schSrvName = schSrvName;
	}

	public String getDtSex() {
		return dtSex;
	}

	public void setDtSex(String dtSex) {
		this.dtSex = dtSex;
	}

	public String getDtIdtype() {
		return dtIdtype;
	}

	public void setDtIdtype(String dtIdtype) {
		this.dtIdtype = dtIdtype;
	}

	public String getIdNo() {
		return idNo;
	}

	public void setIdNo(String idNo) {
		this.idNo = idNo;
	}

	public Date getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}
	

}
