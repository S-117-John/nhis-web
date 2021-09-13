package com.zebone.nhis.webservice.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 灵璧-便民平台-科室类
 * @author Tongjiaqi
 *
 */
@XmlRootElement(name = "Department")
@XmlAccessorType(XmlAccessType.FIELD)
public class LbBmptDepartment {
	//科室编码
	@XmlElement(name="DepartmentCode")
	private String DepartmentCode;
	//科室名称
	@XmlElement(name="DepartmentName")
	private String DepartmentName;
	//科室描述
	@XmlElement(name="DepartmentName")
	private String DepartmentDesc;
	public String getDepartmentCode() {
		return DepartmentCode;
	}
	public void setDepartmentCode(String departmentCode) {
		DepartmentCode = departmentCode;
	}
	public String getDepartmentName() {
		return DepartmentName;
	}
	public void setDepartmentName(String departmentName) {
		DepartmentName = departmentName;
	}
	public String getDepartmentDesc() {
		return DepartmentDesc;
	}
	public void setDepartmentDesc(String departmentDesc) {
		DepartmentDesc = departmentDesc;
	}
	
}
