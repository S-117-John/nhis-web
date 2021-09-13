package com.zebone.nhis.webservice.syx.vo.emr;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * 血透接口-请求
 * @author chengjia
 *
 */
@XmlRootElement(name = "Request")
public class EmrHmReq {
	@XmlElement(name="Mrn")
	private String mrn;
	@XmlElement(name="ReordTimeStart")
	private String reordTimeStart;
	@XmlElement(name="ReordTimeEnd")
	private String reordTimeEnd;
	
	@XmlTransient
	public String getMrn() {
		return mrn;
	}
	public void setMrn(String mrn) {
		this.mrn = mrn;
	}

	@XmlTransient
	public String getReordTimeStart() {
		return reordTimeStart;
	}
	public void setReordTimeStart(String reordTimeStart) {
		this.reordTimeStart = reordTimeStart;
	}
	@XmlTransient
	public String getReordTimeEnd() {
		return reordTimeEnd;
	}
	public void setReordTimeEnd(String reordTimeEnd) {
		this.reordTimeEnd = reordTimeEnd;
	}
	
	
}
