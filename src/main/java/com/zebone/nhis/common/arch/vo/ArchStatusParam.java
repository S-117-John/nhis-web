package com.zebone.nhis.common.arch.vo;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "request")
public class ArchStatusParam {

	
	private String paticode;
	
	private String visitcode;
	
	private String filename;
	
	
	
	
     @XmlElement(name="paticode")
	 public String getPaticode() {
		return paticode;
	}



	 
	public void setPaticode(String paticode) {
		this.paticode = paticode;
	}


	@XmlElement(name="visitcode")
	public String getVisitcode() {
		return visitcode;
	}



	public void setVisitcode(String visitcode) {
		this.visitcode = visitcode;
	}


	@XmlElement(name="filename")
	public String getFilename() {
		return filename;
	}



	public void setFilename(String filename) {
		this.filename = filename;
	}


}
