package com.zebone.nhis.ex.oi.vo;

import java.util.Date;

public class OiPatientInfoVo {
	 private String pkPi; //   --患者主键
     private String pkPv; //   --门诊就诊主键
     private String codePi; // --患者编码
     private String namePi; // --姓名
     private String sex; //  --性别
     private String idtype; //--证件类型
     private String idNo; //      --证件号码
     private String mobile; //     --手机号码
     private String codePv; //    --门诊就诊号码
     private String nameDept; //--门诊就诊科室
     private String pkEmpPhy; //   --门诊医生
     private String nameEmpPhy; // --门诊医生姓名

     private String diagname; //--诊断名称 
     private String descDiag;//诊断描述
     private String pkHp ;
     private String pkDiag ;
     private String pkDept ;
     private String pkPres ;
     private String pkBed ;
     private Date birthDate;
     private Date  dateExec;
     private String agePv;
     private String birthDateStr;
     private String insurNo;
     private String unRegisterFlag;
     private String pkInfureg;


	public String getPkPres() {
		return pkPres;
	}
	public void setPkPres(String pkPres) {
		this.pkPres = pkPres;
	}
	public String getPkBed() {
		return pkBed;
	}
	public void setPkBed(String pkBed) {
		this.pkBed = pkBed;
	}
	public String getPkPi() {
		return pkPi;
	}
	public void setPkPi(String pkPi) {
		this.pkPi = pkPi;
	}
	public String getPkPv() {
		return pkPv;
	}
	public void setPkPv(String pkPv) {
		this.pkPv = pkPv;
	}
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
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getIdtype() {
		return idtype;
	}
	public void setIdtype(String idtype) {
		this.idtype = idtype;
	}
	public String getIdNo() {
		return idNo;
	}
	public void setIdNo(String idNo) {
		this.idNo = idNo;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getCodePv() {
		return codePv;
	}
	public void setCodePv(String codePv) {
		this.codePv = codePv;
	}
	public String getNameDept() {
		return nameDept;
	}
	public void setNameDept(String nameDept) {
		this.nameDept = nameDept;
	}
	public String getPkEmpPhy() {
		return pkEmpPhy;
	}
	public void setPkEmpPhy(String pkEmpPhy) {
		this.pkEmpPhy = pkEmpPhy;
	}
	public String getNameEmpPhy() {
		return nameEmpPhy;
	}
	public void setNameEmpPhy(String nameEmpPhy) {
		this.nameEmpPhy = nameEmpPhy;
	}
	public String getDiagname() {
		return diagname;
	}
	public void setDiagname(String diagname) {
		this.diagname = diagname;
	}
	public String getDescDiag() {
		return descDiag;
	}
	public void setDescDiag(String descDiag) {
		this.descDiag = descDiag;
	}
	public String getPkHp() {
		return pkHp;
	}
	public void setPkHp(String pkHp) {
		this.pkHp = pkHp;
	}
	public String getPkDiag() {
		return pkDiag;
	}
	public void setPkDiag(String pkDiag) {
		this.pkDiag = pkDiag;
	}
	public String getPkDept() {
		return pkDept;
	}
	public void setPkDept(String pkDept) {
		this.pkDept = pkDept;
	}
	public Date getBirthDate() {
		return birthDate;
	}
	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}
	public Date getDateExec() {
		return dateExec;
	}
	public void setDateExec(Date dateExec) {
		this.dateExec = dateExec;
	}
	public String getAgePv() {
		return agePv;
	}
	public void setAgePv(String agePv) {
		this.agePv = agePv;
	}
	public String getBirthDateStr() {
		return birthDateStr;
	}
	public void setBirthDateStr(String birthDateStr) {
		this.birthDateStr = birthDateStr;
	}

	public String getInsurNo() {
		return insurNo;
	}
	public void setInsurNo(String insurNo) {
		this.insurNo = insurNo;
	}

	public String getUnRegisterFlag() {
		return unRegisterFlag;
	}
	public void setUnRegisterFlag(String unRegisterFlag) {
		this.unRegisterFlag = unRegisterFlag;
	}
	public String getPkInfureg() {
		return pkInfureg;
	}
	public void setPkInfureg(String pkInfureg) {
		this.pkInfureg = pkInfureg;
	}

	
	
}
