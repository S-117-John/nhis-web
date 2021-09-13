package com.zebone.nhis.emr.rec.rec.vo;

import java.util.Date;

public class EmrHistory {

	public String name;//姓名
	
	public Date dateBegin;//入院日期
	
	public Date dateEnd;//出院日期
	
	public String patientNo;//住院号
	
	public String patientId;//患者ID
	
	public String times;//就诊次数
	
	public String codeDept;//科室编码
	
	public String nameDept;//科室名称
	
	public String codeWard;//病区编码
	
	public String nameWard;//病区名称

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public String getPatientNo() {
		return patientNo;
	}

	public void setPatientNo(String patientNo) {
		this.patientNo = patientNo;
	}

	public String getTimes() {
		return times;
	}

	public void setTimes(String times) {
		this.times = times;
	}

	public String getCodeDept() {
		return codeDept;
	}

	public void setCodeDept(String codeDept) {
		this.codeDept = codeDept;
	}

	public String getNameDept() {
		return nameDept;
	}

	public void setNameDept(String nameDept) {
		this.nameDept = nameDept;
	}

	public String getCodeWard() {
		return codeWard;
	}

	public void setCodeWard(String codeWard) {
		this.codeWard = codeWard;
	}

	public String getNameWard() {
		return nameWard;
	}

	public void setNameWard(String nameWard) {
		this.nameWard = nameWard;
	}

	public String getPatientId() {
		return patientId;
	}

	public void setPatientId(String patientId) {
		this.patientId = patientId;
	}
	
	
}
