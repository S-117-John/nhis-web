package com.zebone.nhis.common.arch.vo;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
 
public class ArchResultBody {
	
	private List<Fileinfo> fileinfos;
	
	private String status;
	
	

	@XmlElement(name="status")
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@XmlElement(name="fileinfos")
	public List<Fileinfo> getFileinfos() {
		return fileinfos;
	}

	public void setFileinfos(List<Fileinfo> fileinfos) {
		this.fileinfos = fileinfos;
	}

	
	
	
	

}
