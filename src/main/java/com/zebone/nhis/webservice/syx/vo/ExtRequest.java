package com.zebone.nhis.webservice.syx.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "request")
@XmlAccessorType(XmlAccessType.FIELD)
public class ExtRequest {
	@XmlAttribute(name = "ITSVersion")
	private String iTSVersion;
	
	@XmlElement
	private String id;
	@XmlElement
	private String createTime;
	@XmlElement
	private String actionId;
	@XmlElement
	private String actionName;
	@XmlElement(name = "sender")
	private ExtSender sender;
	@XmlElement(name = "subject")
	private ExtSubject subject;


	
	public String getiTSVersion() {
		return iTSVersion;
	}
	public void setiTSVersion(String iTSVersion) {
		this.iTSVersion = iTSVersion;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
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
	public ExtSender getSender() {
		return sender;
	}
	public void setSender(ExtSender sender) {
		this.sender = sender;
	}
	public ExtSubject getSubject() {
		return subject;
	}
	public void setSubject(ExtSubject subject) {
		this.subject = subject;
	}

	
}
