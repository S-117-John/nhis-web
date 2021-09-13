package com.zebone.nhis.webservice.syx.vo.platForm;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "request")
public class OpvisitinfoRequest {
	
	@XmlElement(name = "id")
	private String id;
	
	@XmlElement(name = "creationTime")
	private String creationTime;
	
	@XmlElement(name = "actionId")
	private String actionId;
	
	@XmlElement(name = "actionName")
	private String actionName;
	
	@XmlElement(name = "sender")
	private Sender sender;
	
	@XmlElement(name = "visitdatetime")
	private VisitDateTime visitDateTime;
	
	@XmlElement(name = "visittype")
	private String visitType;

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

	public VisitDateTime getVisitDateTime() {
		return visitDateTime;
	}

	public void setVisitDateTime(VisitDateTime visitDateTime) {
		this.visitDateTime = visitDateTime;
	}

	public String getVisitType() {
		return visitType;
	}

	public void setVisitType(String visitType) {
		this.visitType = visitType;
	}

	public Sender getSender() {
		return sender;
	}

	public void setSender(Sender sender) {
		this.sender = sender;
	}
	
	

}
