/**
  * Copyright 2019 bejson.com 
  */
package com.zebone.nhis.ma.pub.platform.syx.vo;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * Auto-generated: 2019-04-22 15:49:53
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */

@XmlRootElement(name = "HIP_EMPI")
@XmlAccessorType(XmlAccessType.FIELD)
public class HipEmpi {
	@XmlElement(name = "RESULT")
    private Result result;
	
	@XmlElementWrapper(name = "PATIENT_LIST")  
	@XmlElement(name = "PATIENT")  
	private List<PatientEmpi> patients;
	
	public Result getResult() {
		return result;
	}
	public void setResult(Result result) {
		this.result = result;
	}
	public List<PatientEmpi> getPatients() {
		return patients;
	}
	public void setPatients(List<PatientEmpi> patients) {
		this.patients = patients;
	}
     
}