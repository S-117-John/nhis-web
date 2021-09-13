package com.zebone.nhis.webservice.syx.vo.scmhr;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 平台规范请求vo
 * @author jd
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)  
@XmlRootElement(name="request")
public class Request {
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
	@XmlElement(name="subject")
	private ReqSubject subject;
	@XmlElement(name="ROOT")
	private Root root;
	
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

	public Sender getSender() {
		return sender;
	}

	public void setSender(Sender sender) {
		this.sender = sender;
	}

	public String getiTSVersion() {
		return iTSVersion;
	}

	public void setiTSVersion(String iTSVersion) {
		this.iTSVersion = iTSVersion;
	}

	public ReqSubject getSubject() {
		return subject;
	}

	public void setSubject(ReqSubject subject) {
		this.subject = subject;
	}

	public Root getRoot() {
		return root;
	}

	public void setRoot(Root root) {
		this.root = root;
	}
	
}
