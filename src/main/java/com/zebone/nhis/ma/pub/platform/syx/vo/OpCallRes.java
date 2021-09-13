package com.zebone.nhis.ma.pub.platform.syx.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "response")
public class OpCallRes {

    @XmlElement(name = "id")
    private String id;

    @XmlElement(name = "creationTime")
    private String creationTime;

    @XmlElement(name = "actionId")
    private String actionId;

    @XmlElement(name = "actionName")
    private String actionName;

    @XmlElement(name = "result")
    private OpCallResResult result;

    @XmlElement(name = "subject")
    private OpCallResSubject subject;

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

	public OpCallResResult getResult() {
		return result;
	}

	public void setResult(OpCallResResult result) {
		this.result = result;
	}

	public OpCallResSubject getSubject() {
		return subject;
	}

	public void setSubject(OpCallResSubject subject) {
		this.subject = subject;
	}
    
    
}
