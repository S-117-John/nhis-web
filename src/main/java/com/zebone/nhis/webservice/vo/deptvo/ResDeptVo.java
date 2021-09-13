package com.zebone.nhis.webservice.vo.deptvo;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;

public class ResDeptVo {
	private String pkDept;
	private String pkOrg;
	private String nameDept;
	private String codeDept;
	private String pkFather;
	private String shortname;
	private String bednum;
	private String bednumOpen;
	private String namePlace;
	private String deptDesc;
	private String flagOp;
	private String flagEr;
	private String flagIp;
	private String flagPe;
	private String flagHm;
	private String codeArea;
	private String nameArea;
    private DeptTypesVos deptTypes;
    private String pkArea;
    
    @XmlElement(name = "pkArea")
	public String getPkArea() {
		return pkArea;
	}
	public void setPkArea(String pkArea) {
		this.pkArea = pkArea;
	}
	@XmlElement(name = "codeArea")
    public String getCodeArea() {
		return codeArea;
	}
	public void setCodeArea(String codeArea) {
		this.codeArea = codeArea;
	}
	@XmlElement(name = "nameArea")

	public String getNameArea() {
		return nameArea;
	}
	public void setNameArea(String nameArea) {
		this.nameArea = nameArea;
	}
    
	@XmlElement(name = "deptTypes")
	public DeptTypesVos getDeptTypes() {
		return deptTypes;
	}
	public void setDeptTypes(DeptTypesVos deptTypes) {
		this.deptTypes = deptTypes;
	}
	
	@XmlElement(name = "flagOp")
	public String getFlagOp() {
		return flagOp;
	}
	public void setFlagOp(String flagOp) {
		this.flagOp = flagOp;
	}
	
	@XmlElement(name = "flagEr")
	public String getFlagEr() {
		return flagEr;
	}
	public void setFlagEr(String flagEr) {
		this.flagEr = flagEr;
	}
	
	@XmlElement(name = "flagIp")
	public String getFlagIp() {
		return flagIp;
	}
	public void setFlagIp(String flagIp) {
		this.flagIp = flagIp;
	}
	
	@XmlElement(name = "flagPe")
	public String getFlagPe() {
		return flagPe;
	}
	public void setFlagPe(String flagPe) {
		this.flagPe = flagPe;
	}
	
	@XmlElement(name = "flagHm")
	public String getFlagHm() {
		return flagHm;
	}
	public void setFlagHm(String flagHm) {
		this.flagHm = flagHm;
	}
	
	@XmlElement(name = "pkDept")
	public String getPkDept() {
		return pkDept;
	}
	public void setPkDept(String pkDept) {
		this.pkDept = pkDept;
	}
	@XmlElement(name = "pkOrg")
	public String getPkOrg() {
		return pkOrg;
	}
	public void setPkOrg(String pkOrg) {
		this.pkOrg = pkOrg;
	}
	@XmlElement(name = "nameDept")
	public String getNameDept() {
		return nameDept;
	}
	public void setNameDept(String nameDept) {
		this.nameDept = nameDept;
	}
	@XmlElement(name = "codeDept")
	public String getCodeDept() {
		return codeDept;
	}
	public void setCodeDept(String codeDept) {
		this.codeDept = codeDept;
	}
	@XmlElement(name = "pkFather")
	public String getPkFather() {
		return pkFather;
	}
	public void setPkFather(String pkFather) {
		this.pkFather = pkFather;
	}
	@XmlElement(name = "shortname")
	public String getShortname() {
		return shortname;
	}
	public void setShortname(String shortname) {
		this.shortname = shortname;
	}
	@XmlElement(name = "bednum")
	public String getBednum() {
		return bednum;
	}
	public void setBednum(String bednum) {
		this.bednum = bednum;
	}
	@XmlElement(name = "bednumOpen")
	public String getBednumOpen() {
		return bednumOpen;
	}
	public void setBednumOpen(String bednumOpen) {
		this.bednumOpen = bednumOpen;
	}
	@XmlElement(name = "namePlace")
	public String getNamePlace() {
		return namePlace;
	}
	public void setNamePlace(String namePlace) {
		this.namePlace = namePlace;
	}
	@XmlElement(name = "deptDesc")
	public String getDeptDesc() {
		return deptDesc;
	}
	public void setDeptDesc(String deptDesc) {
		this.deptDesc = deptDesc;
	}
	
	

}
