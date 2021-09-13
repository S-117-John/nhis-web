package com.zebone.nhis.webservice.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.zebone.platform.modules.dao.build.au.Field;

/**
 * 科室响应消息体
 */
@XmlRootElement(name = "items")
@XmlAccessorType(XmlAccessType.FIELD)
public class BdOuDeptVo {
	
	/**
	 * 部门编码
	 */
	@XmlElement(name = "deptCode")
	@Field(value="code_dept")
	private String codeDept;
	
	/**
	 * 部门名称
	 */
	@XmlElement(name = "deptName")
	@Field(value="name_dept")
	private String nameDept;
	
	/**
	 * 拼音码
	 */
	@XmlElement(name = "deptInitials ")
	@Field(value="py_code")
	private String pyCode;
	/**
	 * 部门简介
	 */
	@XmlElement(name = "deptDesc")
	@Field(value="dept_desc")
	private String deptDesc;

	/**
	 * 部门排序号
	 */
	@XmlElement(name = "sortno")
	@Field(value="sortno")
	private String sortno;
	
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
	public String getPyCode() {
		return pyCode;
	}
	public void setPyCode(String pyCode) {
		this.pyCode = pyCode;
	}
	public String getDeptDesc() {
		return deptDesc;
	}
	public void setDeptDesc(String deptDesc) {
		this.deptDesc = deptDesc;
	}

	public String getSortno() {
		return sortno;
	}

	public void setSortno(String sortno) {
		this.sortno = sortno;
	}
}
