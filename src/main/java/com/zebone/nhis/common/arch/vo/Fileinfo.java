package com.zebone.nhis.common.arch.vo;

import javax.xml.bind.annotation.XmlElement;


public class Fileinfo {

	private String filepath;
	
	private String filename;
	
	private String doctype;
	
	private String date;
	
	private String memo;
	
	private String dept;
	
	private String deptr;
	
	
	
	
	@XmlElement(name="deptr")
	public String getDeptr() {
		return deptr;
	}

	public void setDeptr(String deptr) {
		this.deptr = deptr;
	}

	@XmlElement(name="dept")
	public String getDept() {
		return dept;
	}

	public void setDept(String dept) {
		this.dept = dept;
	}

	@XmlElement(name="date")
	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}
	@XmlElement(name="memo")
	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	@XmlElement(name="filepath")
	public String getFilepath() {
		return filepath;
	}

	public void setFilepath(String filepath) {
		this.filepath = filepath;
	}
	@XmlElement(name="filename")
	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	@XmlElement(name="doctype")
	public String getDoctype() {
		return doctype;
	}

	public void setDoctype(String doctype) {
		this.doctype = doctype;
	}
	
	
	
}
