package com.zebone.nhis.ma.pub.arch.vo;

public class ArchCancelParamVo {

	
	private String visitcode;//--就诊编码
	private String iptimes;//就诊次数
	private String filename;//文件名称
	private String type;//解除类型
	private String paticode;//
	private String pid;
	private String times;
	private String visittype;

	public String getVisitcode() {
		return visitcode;
	}
	public void setVisitcode(String visitcode) {
		this.visitcode = visitcode;
	}

	public String getIptimes() {
		return iptimes;
	}
	public void setIptimes(String iptimes) {
		this.iptimes = iptimes;
	}

	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}

	public String getPaticode() {
		return paticode;
	}
	public void setPaticode(String paticode) {
		this.paticode = paticode;
	}

	public String getTimes() {
		return times;
	}
	public void setTimes(String times) {
		this.times = times;
	}
	public String getPid() {
		return pid;
	}
	public void setPid(String pid) {
		this.pid = pid;
	}
	public String getVisittype() {
		return visittype;
	}
	public void setVisittype(String visittype) {
		this.visittype = visittype;
	}
	

}
