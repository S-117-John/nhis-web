/**
  * Copyright 2019 bejson.com 
  */
package com.zebone.nhis.ma.pub.platform.syx.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement(name = "RESULT")
@XmlAccessorType(XmlAccessType.FIELD)
public class Result {
	@XmlElement(name="STAT")
    private String STAT;
	
	@XmlElement(name="REMARKS")
    private String REMARKS;
	
	@XmlElement(name="CONTROL_ID")
    private String CONTROL_ID;
	
	@XmlElement(name="DATE")
    private String DATE;

	public String getSTAT() {
		return STAT;
	}

	public void setSTAT(String sTAT) {
		STAT = sTAT;
	}

	public String getREMARKS() {
		return REMARKS;
	}

	public void setREMARKS(String rEMARKS) {
		REMARKS = rEMARKS;
	}

	public String getCONTROL_ID() {
		return CONTROL_ID;
	}

	public void setCONTROL_ID(String cONTROL_ID) {
		CONTROL_ID = cONTROL_ID;
	}

	public String getDATE() {
		return DATE;
	}

	public void setDATE(String dATE) {
		DATE = dATE;
	}
}