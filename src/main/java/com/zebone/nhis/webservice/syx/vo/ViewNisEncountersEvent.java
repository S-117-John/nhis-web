package com.zebone.nhis.webservice.syx.vo;

import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "VIEW_NIS_ENCOUNTER_EVENT")
@XmlAccessorType(XmlAccessType.FIELD)
public class ViewNisEncountersEvent {

	
	@XmlElement(name = "ENCOUNTER_EVENT_ID")
	private String encounterEventId;
	@XmlElement(name = "ENCOUNTER_ID")
	private String encounterId;
	@XmlElement(name = "EVENT_TYPE_FLAG")
	private String eventTypeFlag;
	@XmlElement(name = "OWNING_ORG_ID")
	private String owningOrgId;
	@XmlElement(name = "EFFECTIVE_START_DATE")
	private Date effectiveStartDate;
	@XmlElement(name = "EFFECTIVE_END_DATE")
	private Date effectiveEndDate;
	@XmlElement(name = "EVENT_DATA")
	private Date eventData;
	@XmlElement(name = "IS_DELETED")
	private String isDeleted;
	@XmlElement(name = "SICKBED_ID")
	private String sickbedId;
	@XmlElement(name = "NURS_DEGREE")
	private String nursDegree;
	@XmlElement(name = "VERSION_DATE")
	private Date versionDate;
	
	
	public String getEncounterEventId() {
		return encounterEventId;
	}
	public void setEncounterEventId(String encounterEventId) {
		this.encounterEventId = encounterEventId;
	}
	public String getEncounterId() {
		return encounterId;
	}
	public void setEncounterId(String encounterId) {
		this.encounterId = encounterId;
	}
	public String getEventTypeFlag() {
		return eventTypeFlag;
	}
	public void setEventTypeFlag(String eventTypeFlag) {
		this.eventTypeFlag = eventTypeFlag;
	}
	public String getOwningOrgId() {
		return owningOrgId;
	}
	public void setOwningOrgId(String owningOrgId) {
		this.owningOrgId = owningOrgId;
	}
	public Date getEffectiveStartDate() {
		return effectiveStartDate;
	}
	public void setEffectiveStartDate(Date effectiveStartDate) {
		this.effectiveStartDate = effectiveStartDate;
	}
	public Date getEffectiveEndDate() {
		return effectiveEndDate;
	}
	public void setEffectiveEndDate(Date effectiveEndDate) {
		this.effectiveEndDate = effectiveEndDate;
	}
	public Date getEventData() {
		return eventData;
	}
	public void setEventData(Date eventData) {
		this.eventData = eventData;
	}
	public String getIsDeleted() {
		return isDeleted;
	}
	public void setIsDeleted(String isDeleted) {
		this.isDeleted = isDeleted;
	}
	public String getSickbedId() {
		return sickbedId;
	}
	public void setSickbedId(String sickbedId) {
		this.sickbedId = sickbedId;
	}
	public String getNursDegree() {
		return nursDegree;
	}
	public void setNursDegree(String nursDegree) {
		this.nursDegree = nursDegree;
	}
	public Date getVersionDate() {
		return versionDate;
	}
	public void setVersionDate(Date versionDate) {
		this.versionDate = versionDate;
	}

	
	
	
}
