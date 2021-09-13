package com.zebone.nhis.pv.adt.vo;

import java.util.Date;

/**
 * 中山博爱医院类似患者就诊记录
 * @author wj
 *
 */
public class ZsHisAdtPv {
	
	private String patientId;//patient_id 患者编码
	private String inpatientNo;//inpatient_no 住院号
	private String socialNo;//social_no 身份证号
	private String name;//name 姓名
	private String homeStreet;//home_street 家庭地址
	private String employerName;//employer_name 工作单位
	private String employerStreet;//employer_street 单位地址
	private String relationName;//relation_name联系人
	private Date birthDate;//birth_date 出生日期
	private int lastadmissTimes;//lastadmiss_times 最后入院次数
	
	public String getInpatientNo() {
		return inpatientNo;
	}
	
	public void setInpatientNo(String inpatientNo) {
		this.inpatientNo = inpatientNo;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getSocialNo() {
		return socialNo;
	}

	public void setSocialNo(String socialNo) {
		this.socialNo = socialNo;
	}

	public String getHomeStreet() {
		return homeStreet;
	}
	
	public void setHomeStreet(String homeStreet) {
		this.homeStreet = homeStreet;
	}
	
	public String getEmployerName() {
		return employerName;
	}
	
	public void setEmployerName(String employerName) {
		this.employerName = employerName;
	}
	
	public String getEmployerStreet() {
		return employerStreet;
	}
	
	public void setEmployerStreet(String employerStreet) {
		this.employerStreet = employerStreet;
	}
	
	public String getRelationName() {
		return relationName;
	}
	
	public void setRelationName(String relationName) {
		this.relationName = relationName;
	}
	
	public Date getBirthDate() {
		return birthDate;
	}
	
	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}
	
	public String getPatientId() {
		return patientId;
	}
	
	public void setPatientId(String patientId) {
		this.patientId = patientId;
	}
	
	public int getLastadmissTimes() {
		return lastadmissTimes;
	}
	
	public void setLastadmissTimes(int lastadmissTimes) {
		this.lastadmissTimes = lastadmissTimes;
	}
	
}
