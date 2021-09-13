package com.zebone.nhis.webservice.vo.employeevo;

import javax.xml.bind.annotation.XmlElement;

public class ResEmployeeVo {
	
    private String emplCode = "";
    
    private String emplName = "";
    
    private String sex = "";
    
    private String idno = "";
    
    private String deptName = "";
    
    private String pkDept = "";
    
    private String deptCode = "";
  
	private String emplType = "";
    
    private String docLine = "";
    
    private String flagActive = "";
    
    private String codeEmp = "";	
    
    private String nameEmp = "";
    private String pkOrg = "";
    @XmlElement(name = "codeEmp")
    public String getCodeEmp() {
		return codeEmp;
	}

	public void setCodeEmp(String codeEmp) {
		this.codeEmp = codeEmp;
	}
	@XmlElement(name = "nameEmp")
	public String getNameEmp() {
		return nameEmp;
	}

	public void setNameEmp(String nameEmp) {
		this.nameEmp = nameEmp;
	}
	@XmlElement(name = "pkOrg")
	public String getPkOrg() {
		return pkOrg;
	}

	public void setPkOrg(String pkOrg) {
		this.pkOrg = pkOrg;
	}

	@XmlElement(name = "flagActive")
    public String getFlagActive() {
		return flagActive;
	}

	public void setFlagActive(String flagActive) {
		this.flagActive = flagActive;
	}

	@XmlElement(name = "pkDept")
	public String getPkDept() {
		return pkDept;
	}

	public void setPkDept(String pkDept) {
		this.pkDept = pkDept;
	}

	@XmlElement(name = "deptCode")
    public String getDeptCode() {
  		return deptCode;
  	}

  	public void setDeptCode(String deptCode) {
  		this.deptCode = deptCode;
  	}


	@XmlElement(name = "emplCode")
	public String getEmplCode() {
		return emplCode;
	}

	public void setEmplCode(String emplCode) {
		this.emplCode = emplCode;
	}

	@XmlElement(name = "emplName")
	public String getEmplName() {
		return emplName;
	}

	public void setEmplName(String emplName) {
		this.emplName = emplName;
	}
	
	@XmlElement(name = "sex")
	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}
    
	@XmlElement(name = "idno")
	public String getIdno() {
		return idno;
	}

	public void setIdno(String idno) {
		this.idno = idno;
	}
    
	@XmlElement(name = "deptName")
	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
    
	@XmlElement(name = "emplType")
	public String getEmplType() {
		return emplType;
	}

	public void setEmplType(String emplType) {
		this.emplType = emplType;
	}
    
	@XmlElement(name = "docLine")
	public String getDocLine() {
		return docLine;
	}

	public void setDocLine(String docLine) {
		this.docLine = docLine;
	}
    
    
}
