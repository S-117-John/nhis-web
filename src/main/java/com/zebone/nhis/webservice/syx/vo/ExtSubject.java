package com.zebone.nhis.webservice.syx.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement(name = "subject")
@XmlAccessorType(XmlAccessType.FIELD)
public class ExtSubject {
	@XmlElement(name = "address")
	private ExtAddres address;
	
	//添加患者常住地址主键
	@XmlElement(name = "pkAddr")
	private String pkAddr;
		
	//添加患者就诊id
	@XmlElement(name = "encounter_id")
	private String encounterId;
	//添加患者主索引id
	@XmlElement(name = "patient_id")
	private String patientId;
	//入院时间
	@XmlElement(name = "admission_date")
	private String admissionDate;
	//住院号
	@XmlElement(name = "seq_no_text")
	private String seqNoText;
	//病人姓名
	@XmlElement(name = "display_name")
	private String displayName;
	
	
	//临床事件部分字段
	@XmlElement(name = "ENCOUNTER_ID")
	private String enCounterId;
	@XmlElement(name = "OWNING_ORG_ID")
	private String owningOrgId;
	@XmlElement(name = "EFFECTIVE_DATE")
	private EffectiveDate effectiveDate;
	
	
	
	public ExtAddres getAddress() {
		return address;
	}

	public void setAddress(ExtAddres address) {
		this.address = address;
	}

	public String getPkAddr() {
		return pkAddr;
	}

	public void setPkAddr(String pkAddr) {
		this.pkAddr = pkAddr;
	}

	public String getEncounterId() {
		return encounterId;
	}

	public void setEncounterId(String encounterId) {
		this.encounterId = encounterId;
	}

	public String getPatientId() {
		return patientId;
	}

	public void setPatientId(String patientId) {
		this.patientId = patientId;
	}

	public String getAdmissionDate() {
		return admissionDate;
	}

	public void setAdmissionDate(String admissionDate) {
		this.admissionDate = admissionDate;
	}

	public String getSeqNoText() {
		return seqNoText;
	}

	public void setSeqNoText(String seqNoText) {
		this.seqNoText = seqNoText;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getEnCounterId() {
		return enCounterId;
	}

	public void setEnCounterId(String enCounterId) {
		this.enCounterId = enCounterId;
	}

	public String getOwningOrgId() {
		return owningOrgId;
	}

	public void setOwningOrgId(String owningOrgId) {
		this.owningOrgId = owningOrgId;
	}

	public EffectiveDate getEffectiveDate() {
		return effectiveDate;
	}

	public void setEffectiveDate(EffectiveDate effectiveDate) {
		this.effectiveDate = effectiveDate;
	}
	
	
	
	
	
}
