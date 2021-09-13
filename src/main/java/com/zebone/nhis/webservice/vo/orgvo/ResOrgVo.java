package com.zebone.nhis.webservice.vo.orgvo;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

public class ResOrgVo {
	private String pkOrg;
	private String nameOrg;
	private String codeOrg;
	private String shortname;
	private String pkFather;
	private String hosptype;
	private String grade;
	private String bednum;

	@XmlElement(name = "pkOrg")
	public String getPkOrg() {
		return pkOrg;
	}

	public void setPkOrg(String pkOrg) {
		this.pkOrg = pkOrg;
	}
	
	@XmlElement(name = "nameOrg")
	public String getNameOrg() {
		return nameOrg;
	}

	public void setNameOrg(String nameOrg) {
		this.nameOrg = nameOrg;
	}
	
	@XmlElement(name = "codeOrg")
	public String getCodeOrg() {
		return codeOrg;
	}

	public void setCodeOrg(String codeOrg) {
		this.codeOrg = codeOrg;
	}
	
	@XmlElement(name = "shortname")
	public String getShortname() {
		return shortname;
	}

	public void setShortname(String shortname) {
		this.shortname = shortname;
	}
    
	@XmlElement(name = "pkFather")
	public String getPkFather() {
		return pkFather;
	}

	public void setPkFather(String pkFather) {
		this.pkFather = pkFather;
	}
	
	@XmlElement(name = "hosptype")
	public String getHosptype() {
		return hosptype;
	}

	public void setHosptype(String hosptype) {
		this.hosptype = hosptype;
	}
    
	@XmlElement(name = "grade")
	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}
    
	@XmlElement(name = "bednum")
	public String getBednum() {
		return bednum;
	}

	public void setBednum(String bednum) {
		this.bednum = bednum;
	}

}
