/**
  * Copyright 2019 bejson.com 
  */
package com.zebone.nhis.ma.pub.platform.syx.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Auto-generated: 2019-04-22 15:49:53
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
@XmlRootElement(name = "LINKMAN")
@XmlAccessorType(XmlAccessType.FIELD)
public class Linkman {
	
	@XmlElement(name="CODE_CONT_RELAT")
    private String CODE_CONT_RELAT;
	
	@XmlElement(name="NAME_CONT_RELAT")
    private String NAME_CONT_RELAT;
	
	@XmlElement(name="CONTACT_NAME")
    private String CONTACT_NAME;
	
	@XmlElement(name="CONTACT_TEL")
    private String CONTACT_TEL;
	
	@XmlElement(name="CONTACT_ADDR")
    private String CONTACT_ADDR;
	
	@XmlElement(name="FLAG_MAKE")
    private String FLAG_MAKE;
	
	public String getCODE_CONT_RELAT() {
		return CODE_CONT_RELAT;
	}
	public void setCODE_CONT_RELAT(String cODE_CONT_RELAT) {
		CODE_CONT_RELAT = cODE_CONT_RELAT;
	}
	public String getNAME_CONT_RELAT() {
		return NAME_CONT_RELAT;
	}
	public void setNAME_CONT_RELAT(String nAME_CONT_RELAT) {
		NAME_CONT_RELAT = nAME_CONT_RELAT;
	}
	public String getCONTACT_NAME() {
		return CONTACT_NAME;
	}
	public void setCONTACT_NAME(String cONTACT_NAME) {
		CONTACT_NAME = cONTACT_NAME;
	}
	public String getCONTACT_TEL() {
		return CONTACT_TEL;
	}
	public void setCONTACT_TEL(String cONTACT_TEL) {
		CONTACT_TEL = cONTACT_TEL;
	}
	public String getCONTACT_ADDR() {
		return CONTACT_ADDR;
	}
	public void setCONTACT_ADDR(String cONTACT_ADDR) {
		CONTACT_ADDR = cONTACT_ADDR;
	}
	public String getFLAG_MAKE() {
		return FLAG_MAKE;
	}
	public void setFLAG_MAKE(String fLAG_MAKE) {
		FLAG_MAKE = fLAG_MAKE;
	}
}