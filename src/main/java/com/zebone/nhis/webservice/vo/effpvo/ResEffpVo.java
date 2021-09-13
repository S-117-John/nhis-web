package com.zebone.nhis.webservice.vo.effpvo;

import java.util.Date;

import javax.xml.bind.annotation.XmlElement;

public class ResEffpVo {
	private String codePv;
	
	private Date dateClinic;
	
	private String nameDept;
	
	private String pkDept;
	
	private String pkPv;
	
	
	@XmlElement(name="codePv")
	public String getCodePv() {
		return codePv;
	}

	public void setCodePv(String codePv) {
		this.codePv = codePv;
	}
	
	@XmlElement(name="dateClinic")
	public Date getDateClinic() {
		return dateClinic;
	}

	public void setDateClinic(Date dateClinic) {
		this.dateClinic = dateClinic;
	}

	@XmlElement(name="nameDept")
	public String getNameDept() {
		return nameDept;
	}

	public void setNameDept(String nameDept) {
		this.nameDept = nameDept;
	}
    
	@XmlElement(name="pkDept")
	public String getPkDept() {
		return pkDept;
	}

	public void setPkDept(String pkDept) {
		this.pkDept = pkDept;
	}
	
	@XmlElement(name="pkPv")
	public String getPkPv() {
		return pkPv;
	}

	public void setPkPv(String pkPv) {
		this.pkPv = pkPv;
	}

}
