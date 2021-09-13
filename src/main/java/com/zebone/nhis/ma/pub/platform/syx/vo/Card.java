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
@XmlRootElement(name = "CARD")
@XmlAccessorType(XmlAccessType.FIELD)
public class Card {
	@XmlElement(name = "CODE_CARD_TYPE") 
    private String CODE_CARD_TYPE;
	
	@XmlElement(name = "NAME_CARD_TYPE") 
    private String NAME_CARD_TYPE;
	
	@XmlElement(name = "CARD_NO") 
    private String CARD_NO;
	
	@XmlElement(name = "FLAG_MAKE") 
    private String FLAG_MAKE;
	
	public String getCODE_CARD_TYPE() {
		return CODE_CARD_TYPE;
	}
	public void setCODE_CARD_TYPE(String cODE_CARD_TYPE) {
		CODE_CARD_TYPE = cODE_CARD_TYPE;
	}
	public String getNAME_CARD_TYPE() {
		return NAME_CARD_TYPE;
	}
	public void setNAME_CARD_TYPE(String nAME_CARD_TYPE) {
		NAME_CARD_TYPE = nAME_CARD_TYPE;
	}
	public String getCARD_NO() {
		return CARD_NO;
	}
	public void setCARD_NO(String cARD_NO) {
		CARD_NO = cARD_NO;
	}
	public String getFLAG_MAKE() {
		return FLAG_MAKE;
	}
	public void setFLAG_MAKE(String fLAG_MAKE) {
		FLAG_MAKE = fLAG_MAKE;
	}
}