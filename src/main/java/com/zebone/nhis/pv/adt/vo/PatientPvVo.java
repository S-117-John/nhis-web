package com.zebone.nhis.pv.adt.vo;

import java.util.Date;

/**
 * 患者就诊信息
 * @author leiminjian
 * @date 2019-09-23
 *
 */
public class PatientPvVo {

	private String pkPv; //患者就诊主键
	private String codePv; //患者就诊编码
	private String euPvtype; //患者就诊类型
	private String nameDept; //患者就诊科室
	private Date dateBegin; //患者就诊时间
	private String codeOp ;
	// 姓名
	private String name ;
	// 性别
	private String aex ;
	// 年龄
	private String age ;
	// 医保
	private String medicalInsurance ;
	// 诊断
	private String diagnosis ;
	private String pkPi ;
	private String codePi ;

	public String getPkPi() {
		return pkPi;
	}

	public void setPkPi(String pkPi) {
		this.pkPi = pkPi;
	}

	public String getCodePi() {
		return codePi;
	}

	public void setCodePi(String codePi) {
		this.codePi = codePi;
	}

	public String getCodeOp() {
		return codeOp;
	}

	public void setCodeOp(String codeOp) {
		this.codeOp = codeOp;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAex() {
		return aex;
	}

	public void setAex(String aex) {
		this.aex = aex;
	}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public String getMedicalInsurance() {
		return medicalInsurance;
	}

	public void setMedicalInsurance(String medicalInsurance) {
		this.medicalInsurance = medicalInsurance;
	}

	public String getDiagnosis() {
		return diagnosis;
	}

	public void setDiagnosis(String diagnosis) {
		this.diagnosis = diagnosis;
	}

	public String getPkPv() {
		return pkPv;
	}
	public void setPkPv(String pkPv) {
		this.pkPv = pkPv;
	}
	public String getCodePv() {
		return codePv;
	}
	public void setCodePv(String codePv) {
		this.codePv = codePv;
	}
	public String getEuPvtype() {
		return euPvtype;
	}
	public void setEuPvtype(String euPvtype) {
		this.euPvtype = euPvtype;
	}
	public String getNameDept() {
		return nameDept;
	}
	public void setNameDept(String nameDept) {
		this.nameDept = nameDept;
	}
	public Date getDateBegin() {
		return dateBegin;
	}
	public void setDateBegin(Date dateBegin) {
		this.dateBegin = dateBegin;
	}
	
	
}
