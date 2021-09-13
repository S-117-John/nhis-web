/**
  * Copyright 2019 bejson.com 
  */
package com.zebone.nhis.ma.pub.platform.syx.vo;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlType;

@XmlType(name = "PATIENT_LIST")
public class PatientList {
	@XmlElementWrapper(name = "PATIENT_LIST")  
	@XmlElement(name = "PATIENT")  
	private List<PatientEmpi> patients;
	
//	@XmlElement(name = "PATIENT")
//    private PatientEmpi patientEmpi;

	public List<PatientEmpi> getPatients() {
		return patients;
	}

	public void setPatients(List<PatientEmpi> patients) {
		this.patients = patients;
	}

//	public PatientEmpi getPatientEmpi() {
//		return patientEmpi;
//	}
//
//	public void setPatientEmpi(PatientEmpi patientEmpi) {
//		this.patientEmpi = patientEmpi;
//	}
	
     
}