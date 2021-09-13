package com.zebone.nhis.webservice.syx.vo.platForm;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class OpdiagInfoVo {
	
	@XmlElement(name = "diagnosisId")
	private String diagnosisId;
	
	@XmlElement(name = "visitId")
	private String visitId;
	
	@XmlElement(name = "patientId")
	private String patientId;
	
	@XmlElement(name = "diagCode")
	private String diagCode;
	
	@XmlElement(name = "diagDesc")
	private String diagDesc;
	
	@XmlElement(name = "diagDate")
	private String diagDate;
	
	@XmlElement(name = "visitType")
	private String visitType;
	
	@XmlElement(name = "rstatus")
	private String  rstatus;
		
	@XmlElement(name = "dateSource") 
	private String dateSource;
	
	
	public String getDateSource() {
		return dateSource;
	}

	public void setDateSource(String dateSource) {
		this.dateSource = dateSource;
	}

	public String getDiagnosisId() {
		return diagnosisId;
	}

	public void setDiagnosisId(String diagnosisId) {
		this.diagnosisId = diagnosisId;
	}

	public String getVisitId() {
		return visitId;
	}

	public void setVisitId(String visitId) {
		this.visitId = visitId;
	}

	public String getPatientId() {
		return patientId;
	}

	public void setPatientId(String patientId) {
		this.patientId = patientId;
	}

	public String getDiagCode() {
		return diagCode;
	}

	public void setDiagCode(String diagCode) {
		this.diagCode = diagCode;
	}

	public String getDiagDesc() {
		return diagDesc;
	}

	public void setDiagDesc(String diagDesc) {
		this.diagDesc = diagDesc;
	}

	public String getDiagDate() {
		return diagDate;
	}

	public void setDiagDate(String diagDate) {
		this.diagDate = diagDate;
	}

	public String getVisitType() {
		return visitType;
	}

	public void setVisitType(String visitType) {
		this.visitType = visitType;
	}

	public String getRstatus() {
		return rstatus;
	}

	public void setRstatus(String rstatus) {
		this.rstatus = rstatus;
	}	

}
