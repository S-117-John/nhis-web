package com.zebone.nhis.webservice.vo.appschvo;

import javax.xml.bind.annotation.XmlElement;

public class ResAppSchVo {
	private String cntUnused;
	private String codeEmp;
	private String dateWork;
	private String nameDateslot;
	private String pkSch;
	private String price;
	private String schresname;
	private String schsrvname;
	private String pkDept;
	
	private String timeBegin;
	private String timeEnd;
	
	@XmlElement(name="cntUnused")
	public String getCntUnused() {
		return cntUnused;
	}
	public void setCntUnused(String cntUnused) {
		this.cntUnused = cntUnused;
	}
	
	@XmlElement(name="codeEmp")
	public String getCodeEmp() {
		return codeEmp;
	}
	
	public void setCodeEmp(String codeEmp) {
		this.codeEmp = codeEmp;
	}
	
	@XmlElement(name="dateWork")
	public String getDateWork() {
		return dateWork;
	}
	public void setDateWork(String dateWork) {
		this.dateWork = dateWork;
	}
	
	@XmlElement(name="nameDateslot")
	public String getNameDateslot() {
		return nameDateslot;
	}
	public void setNameDateslot(String nameDateslot) {
		this.nameDateslot = nameDateslot;
	}
	
	@XmlElement(name="pkSch")
	public String getPkSch() {
		return pkSch;
	}
	public void setPkSch(String pkSch) {
		this.pkSch = pkSch;
	}
	
	@XmlElement(name="price")
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	
	@XmlElement(name="schresName")
	public String getSchresname() {
		return schresname;
	}
	public void setSchresname(String schresname) {
		this.schresname = schresname;
	}
	
	@XmlElement(name="schsrvName")
	public String getSchsrvname() {
		return schsrvname;
	}
	public void setSchsrvName(String schsrvname) {
		this.schsrvname = schsrvname;
	}
	@XmlElement(name="pkDept")
	public String getPkDept() {
		return pkDept;
	}
	public void setPkDept(String pkDept) {
		this.pkDept = pkDept;
	}
	@XmlElement(name="timeBegin")
	public String getTimeBegin() {
		return timeBegin;
	}
	public void setTimeBegin(String timeBegin) {
		this.timeBegin = timeBegin;
	}
	@XmlElement(name="timeEnd")
	public String getTimeEnd() {
		return timeEnd;
	}
	public void setTimeEnd(String timeEnd) {
		this.timeEnd = timeEnd;
	}
    
	
}
