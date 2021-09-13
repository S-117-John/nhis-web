package com.zebone.nhis.webservice.syx.vo.platForm;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "response")
public class OpdiagInfoResponse {
	
	@XmlElement(name = "id")
	private String id;
	
	@XmlElement(name = "creationTime")
	private String creationTime;
	
	@XmlElement(name = "actionId")
	private String actionId;
	
	@XmlElement(name = "actionName")
	private String actionName;
	
	@XmlElement(name = "subject")
	private List<OpdiagInfoVo> subject;
	
	@XmlElement(name = "result")
	private OpdiagInfoResult result;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(String creationTime) {
		this.creationTime = creationTime;
	}

	public String getActionId() {
		return actionId;
	}

	public void setActionId(String actionId) {
		this.actionId = actionId;
	}

	public String getActionName() {
		return actionName;
	}

	public void setActionName(String actionName) {
		this.actionName = actionName;
	}

	public List<OpdiagInfoVo> getSubject() {
		return subject;
	}

	public void setSubject(List<OpdiagInfoVo> subject) {
		this.subject = subject;
	}

	public OpdiagInfoResult getResult() {
		return result;
	}

	public void setResult(OpdiagInfoResult result) {
		this.result = result;
	}
	
}
