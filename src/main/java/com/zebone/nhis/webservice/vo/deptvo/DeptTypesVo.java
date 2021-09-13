package com.zebone.nhis.webservice.vo.deptvo;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;

public class DeptTypesVo {
	
	private String codeType;
	
	private String nameType;
	
	private String pkDept;

	@XmlElement(name = "codeType")
	public String getCodeType() {
		return codeType;
	}

	public void setCodeType(String codeType) {
		this.codeType = codeType;
	}
	
	@XmlElement(name = "nameType")
	public String getNameType() {
		return nameType;
	}

	public void setNameType(String nameType) {
		this.nameType = nameType;
	}
	
	@XmlTransient
	public String getPkDept() {
		return pkDept;
	}

	public void setPkDept(String pkDept) {
		this.pkDept = pkDept;
	}

	
	
	
}
