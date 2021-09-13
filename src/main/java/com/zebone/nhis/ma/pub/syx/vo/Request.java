package com.zebone.nhis.ma.pub.syx.vo;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("request")
public class Request {
	@XStreamAsAttribute
	private String ITSVersion = "1.0";
	
	private String id;
	private String createTime;
	private String actionId;
	private String actionName;
	private Sender sender;
	private Subject subject;
	
	public String getITSVersion() {
		return ITSVersion;
	}
	public void setITSVersion(String iTSVersion) {
		ITSVersion = iTSVersion;
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
	public Sender getSender() {
		if(sender == null)sender = new Sender();
		return sender;
	}
	public void setSender(Sender sender) {
		this.sender = sender;
	}
	public Subject getSubject() {
		if(subject == null)subject = new Subject();
		return subject;
	}
	public void setSubject(Subject subject) {
		this.subject = subject;
	}
	
}
