package com.zebone.nhis.webservice.zhongshan.vo;

import java.util.Date;

import javax.xml.bind.annotation.XmlElement;

public class CriticalXmlHead {
	private String serviceId;
	private String orgCode;
	private String sysId;
	private String operator;
	private String sourceIp;
	private Date dateMsg;
	@XmlElement(name="serviceid")
	public String getServiceId() {
		return serviceId;
	}
	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}
	@XmlElement(name="orgcode")
	public String getOrgCode() {
		return orgCode;
	}
	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}
	@XmlElement(name="sysid")
	public String getSysId() {
		return sysId;
	}
	public void setSysId(String sysId) {
		this.sysId = sysId;
	}
	@XmlElement(name="operator")
	public String getOperator() {
		return operator;
	}
	public void setOperator(String operator) {
		this.operator = operator;
	}
	@XmlElement(name="sourceip")
	public String getSourceIp() {
		return sourceIp;
	}
	public void setSourceIp(String sourceIp) {
		this.sourceIp = sourceIp;
	}
	@XmlElement(name="date_msg")
	public Date getDateMsg() {
		return dateMsg;
	}
	public void setDateMsg(Date dateMsg) {
		this.dateMsg = dateMsg;
	}
}
