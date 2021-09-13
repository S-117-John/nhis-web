package com.zebone.nhis.common.arch.vo;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "request")
public class ArchCancelParam {

	
	private String visitcode;//--就诊编码
	private String iptimes;//就诊次数
	private String filename;//文件名称
	private String type;//解除类型
	private String paticode;//
	private String times;
	@XmlElement(name="visitcode")
	public String getVisitcode() {
		return visitcode;
	}
	public void setVisitcode(String visitcode) {
		this.visitcode = visitcode;
	}
	@XmlElement(name="iptimes")
	public String getIptimes() {
		return iptimes;
	}
	public void setIptimes(String iptimes) {
		this.iptimes = iptimes;
	}
	@XmlElement(name="filename")
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	@XmlElement(name="type")
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	@XmlElement(name="paticode")
	public String getPaticode() {
		return paticode;
	}
	public void setPaticode(String paticode) {
		this.paticode = paticode;
	}
	@XmlElement(name="times")
	public String getTimes() {
		return times;
	}
	public void setTimes(String times) {
		this.times = times;
	}
	
	
	
	
	

}
