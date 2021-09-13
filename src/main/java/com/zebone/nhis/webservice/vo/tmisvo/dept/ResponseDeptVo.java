package com.zebone.nhis.webservice.vo.tmisvo.dept;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 输血返回构造xml
 * @author frank
 *科室列表
 */
@XmlRootElement(name = "DeptInfo")
@XmlAccessorType(XmlAccessType.FIELD)
public class ResponseDeptVo {
	
	@XmlElement(name = "HospHISCode")
	public String hospHISCode;
	
	@XmlElement(name = "HospName")
	public String hospName;
	
	@XmlElement(name = "Name")
	public String name;
	
	@XmlElement(name = "Code")
	public String code;
	
	@XmlElement(name = "IsOutPatient")
	public String isOutPatient;
	
	@XmlElement(name = "IsInPatient")
	public String isInPatient;
	
	public String getHospHISCode() {
		return hospHISCode;
	}

	public void setHospHISCode(String hospHISCode) {
		this.hospHISCode = hospHISCode;
	}

	public String getHospName() {
		return hospName;
	}

	public void setHospName(String hospName) {
		this.hospName = hospName;
	}
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	public String getIsOutPatient() {
		return isOutPatient;
	}

	public void setIsOutPatient(String isOutPatient) {
		this.isOutPatient = isOutPatient;
	}
	public String getIsInPatient() {
		return isInPatient;
	}

	public void setIsInPatient(String isInPatient) {
		this.isInPatient = isInPatient;
	}
	
}
