package com.zebone.nhis.pro.zsba.adt.vo;

import java.util.Date;

/**
 * 中山博爱医院类似患者就诊记录
 * @author wj
 *
 */
public class ZsbaHisAdtPv {
	
	private String patientId;//patient_id 患者编码
	private String inpatientNo;//inpatient_no 住院号	
	private String socialNo;//social_no 身份证号
	private String name;//name 姓名
	private String sex;//sex 性别
	private Date birthDate;//birth_date 出生日期
	private String homeStreet;//home_street 家庭地址
	private String homeTel;//home_tel 家庭电话
	private String relationName;//relation_name联系人
	private String relationTel;//relation_tel联系人电话
	private int admissTimes;//admiss_times 入院次数
	private Date admissDate;//admiss_date上次入院日期
	private String deptName;//dept_name 上次就诊科室
	private String wardName;//ward_name 上次就诊病区
	private String admissDiagStr;//admiss_diag_str 上次就诊诊断
	private boolean flagTransHisAndBaGl;//合并his 与 病案系统
	private boolean flagTranNhisAndHis;//合并 Nhis 与 （his + 病案）
	private String pkPi;//住院患者主键
	
	public String getPatientId() {
		return patientId;
	}
	public void setPatientId(String patientId) {
		this.patientId = patientId;
	}
	public String getInpatientNo() {
		return inpatientNo;
	}
	public void setInpatientNo(String inpatientNo) {
		this.inpatientNo = inpatientNo;
	}
	public String getSocialNo() {
		return socialNo;
	}
	public void setSocialNo(String socialNo) {
		this.socialNo = socialNo;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public Date getBirthDate() {
		return birthDate;
	}
	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}
	public String getHomeStreet() {
		return homeStreet;
	}
	public void setHomeStreet(String homeStreet) {
		this.homeStreet = homeStreet;
	}
	public String getHomeTel() {
		return homeTel;
	}
	public void setHomeTel(String homeTel) {
		this.homeTel = homeTel;
	}
	public String getRelationName() {
		return relationName;
	}
	public void setRelationName(String relationName) {
		this.relationName = relationName;
	}
	public String getRelationTel() {
		return relationTel;
	}
	public void setRelationTel(String relationTel) {
		this.relationTel = relationTel;
	}
	public int getAdmissTimes() {
		return admissTimes;
	}
	public void setAdmissTimes(int admissTimes) {
		this.admissTimes = admissTimes;
	}
	public Date getAdmissDate() {
		return admissDate;
	}
	public void setAdmissDate(Date admissDate) {
		this.admissDate = admissDate;
	}
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	public String getWardName() {
		return wardName;
	}
	public void setWardName(String wardName) {
		this.wardName = wardName;
	}
	public String getAdmissDiagStr() {
		return admissDiagStr;
	}
	public void setAdmissDiagStr(String admissDiagStr) {
		this.admissDiagStr = admissDiagStr;
	}
	public boolean isFlagTransHisAndBaGl() {
		return flagTransHisAndBaGl;
	}
	public void setFlagTransHisAndBaGl(boolean flagTransHisAndBaGl) {
		this.flagTransHisAndBaGl = flagTransHisAndBaGl;
	}
	public boolean isFlagTranNhisAndHis() {
		return flagTranNhisAndHis;
	}
	public void setFlagTranNhisAndHis(boolean flagTranNhisAndHis) {
		this.flagTranNhisAndHis = flagTranNhisAndHis;
	}
	public String getPkPi() {
		return pkPi;
	}
	public void setPkPi(String pkPi) {
		this.pkPi = pkPi;
	}
}
