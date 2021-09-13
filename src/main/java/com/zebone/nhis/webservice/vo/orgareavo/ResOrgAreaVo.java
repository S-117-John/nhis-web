package com.zebone.nhis.webservice.vo.orgareavo;

import java.util.Date;

import javax.xml.bind.annotation.XmlElement;

public class ResOrgAreaVo {
	private String pkOrgArea;
    private String pkOrg;
    private String codeArea;
    private String nameArea;
    private String note;
    private String creator;
    private Date createTime;
    private String modifier;
    private Date modityTime;
    private String delFlag;
    private Date ts;
    
	@XmlElement(name = "pkOrgArea")
	public String getPkOrgArea() {
		return pkOrgArea;
	}
	public void setPkOrgArea(String pkOrgArea) {
		this.pkOrgArea = pkOrgArea;
	}
	
	@XmlElement(name = "pkOrg")
	public String getPkOrg() {
		return pkOrg;
	}
	public void setPkOrg(String pkOrg) {
		this.pkOrg = pkOrg;
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
	@XmlElement(name = "note")
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	@XmlElement(name = "creator")
	public String getCreator() {
		return creator;
	}
	public void setCreator(String creator) {
		this.creator = creator;
	}
	@XmlElement(name = "createTime")
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	@XmlElement(name = "modifier")
	public String getModifier() {
		return modifier;
	}
	public void setModifier(String modifier) {
		this.modifier = modifier;
	}
	@XmlElement(name = "modityTime")
	public Date getModityTime() {
		return modityTime;
	}
	public void setModityTime(Date modityTime) {
		this.modityTime = modityTime;
	}
	@XmlElement(name = "delFlag")
	public String getDelFlag() {
		return delFlag;
	}
	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}
	@XmlElement(name = "ts")
	public Date getTs() {
		return ts;
	}
	public void setTs(Date ts) {
		this.ts = ts;
	}
}
