package com.zebone.nhis.webservice.syx.vo.scmhr;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

public class RequestPubVo {
	@XmlElement(name="id")
	private String id;
	@XmlElement(name="createTime")
	private String createTime;
	@XmlElement(name="actionId")
	private String actionId;
	@XmlElement(name="actionName")
	private String actionName;
	@XmlElement(name="sender")
	private Sender sender;
	@XmlAttribute(name="ITSVersion")
	private String iTSVersion;

	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setActionId(String actionId) {
		this.actionId = actionId;
	}

	public String getActionId() {
		return actionId;
	}

	public void setActionName(String actionName) {
		this.actionName = actionName;
	}

	public String getActionName() {
		return actionName;
	}

	public void setSender(Sender sender) {
		this.sender = sender;
	}

	public Sender getSender() {
		return sender;
	}

	public void setITSVersion(String iTSVersion) {
		this.iTSVersion = iTSVersion;
	}

	public String getITSVersion() {
		return iTSVersion;
	}
}
