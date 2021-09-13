package com.zebone.nhis.webservice.syx.vo;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "return")
@XmlAccessorType(XmlAccessType.FIELD)
public class ExtReturn {
	@XmlAttribute(name = "ITSVersion")
	private String iTSVersion;
	
	@XmlElement(name = "id")
	private String id;
	
	@XmlElement(name = "createTime")
	private String createTime;
	@XmlElement(name = "actionId")
	private String actionId;
	@XmlElement(name = "actionName")
	private String actionName;
	@XmlElement(name = "result")
	private ExtResult result;
	
	@XmlElementWrapper(name = "subject")  
	@XmlElement(name = "VIEW_NIS_ENCOUNTER")  
	private List<ViewNisEncounter> viewNisEncounters;

	@XmlElementWrapper(name = "subject")  
	@XmlElement(name = "VIEW_NIS_ENCOUNTER_EVENT")  
	private List<ViewNisEncountersEvent> viewNisEncountersEvent;
	
	
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
	public ExtResult getResult() {
		if(result==null)result= new ExtResult();
		return result;
	}
	public void setResult(ExtResult result) {
		this.result = result;
	}
	public String getiTSVersion() {
		return iTSVersion;
	}
	public void setiTSVersion(String iTSVersion) {
		this.iTSVersion = iTSVersion;
	}
	public List<ViewNisEncounter> getViewNisEncounters() {
		return viewNisEncounters;
	}
	public void setViewNisEncounters(List<ViewNisEncounter> viewNisEncounters) {
		this.viewNisEncounters = viewNisEncounters;
	}
	public List<ViewNisEncountersEvent> getViewNisEncountersEvent() {
		return viewNisEncountersEvent;
	}
	public void setViewNisEncountersEvent(List<ViewNisEncountersEvent> viewNisEncountersEvent) {
		this.viewNisEncountersEvent = viewNisEncountersEvent;
	}
	
	
	
	
}
