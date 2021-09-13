package com.zebone.nhis.webservice.syx.vo;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * 请求
 * @author chengjia
 *
 */
@XmlRootElement(name = "COMMONREQUEST")
public class EmrRequest {
	@XmlElement(name="REQUESTID")
	private String requestId;
	@XmlElement(name="REQUESTIP")
	private String requestIp;
	@XmlElement(name="ACCESSTOKEN")
	private String token;
	@XmlElement(name="SERVICEVERSION")
	private String version;
	@XmlElement(name="SERVICECODE")
	private String code;
	@XmlElement(name="REQUESTDATA")
	private EmrReqData reqData;
	
	@XmlTransient
	public String getRequestId() {
		return requestId;
	}
	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}
	
	@XmlTransient
	public String getRequestIp() {
		return requestIp;
	}
	public void setRequestIp(String requestIp) {
		this.requestIp = requestIp;
	}
	
	@XmlTransient
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	
	@XmlTransient
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	
	@XmlTransient
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	
	@XmlTransient
	public EmrReqData getReqData() {
		return reqData;
	}
	public void setReqData(EmrReqData reqData) {
		this.reqData = reqData;
	}

}
