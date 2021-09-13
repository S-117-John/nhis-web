package com.zebone.nhis.webservice.syx.vo.emr;

import java.util.Date;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.codehaus.jackson.annotate.JsonProperty;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 房颤接口-请求
 * @author chengjia
 *
 */
public class EmrAfMessage {
	@JsonProperty("MethodName")
	private String methodName;
	@JsonProperty("AccessKey")
	private String accessKey;
	@JsonProperty("request")
	private EmrAfRequest emrAfRequest;
	public String getMethodName() {
		return methodName;
	}
	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}
	public String getAccessKey() {
		return accessKey;
	}
	public void setAccessKey(String accessKey) {
		this.accessKey = accessKey;
	}
	public EmrAfRequest getEmrAfRequest() {
		return emrAfRequest;
	}
	public void setEmrAfRequest(EmrAfRequest emrAfRequest) {
		this.emrAfRequest = emrAfRequest;
	}

}
