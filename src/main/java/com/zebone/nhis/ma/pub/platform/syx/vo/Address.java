/**
  * Copyright 2019 bejson.com 
  */
package com.zebone.nhis.ma.pub.platform.syx.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * Auto-generated: 2019-04-22 15:49:53
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
@XmlRootElement(name = "ADDRESS")
@XmlAccessorType(XmlAccessType.FIELD)
public class Address {
	@XmlElement(name="CODE_ADDR_TYPE")
    private String CODE_ADDR_TYPE;
	
	@XmlElement(name="NAME_ADDR_TYPE")
    private String NAME_ADDR_TYPE;
	
	@XmlElement(name="ADDR_PROV")
    private String ADDR_PROV;
	
	@XmlElement(name="ADDR_CITY")
    private String ADDR_CITY;

	@XmlElement(name="ADDR_COUNTY")
    private String ADDR_COUNTY;
	
	@XmlElement(name="ADDR_COUNTRY")
    private String ADDR_COUNTRY;
	
	@XmlElement(name="ADDR_VILLAGE")
    private String ADDR_VILLAGE;
	
	@XmlElement(name="ADDR_HOUSE_NUM")
    private String ADDR_HOUSE_NUM;
	
	@XmlElement(name="NOSTRUCTURE_ADDR")
    private String NOSTRUCTURE_ADDR;
	
	@XmlElement(name="ADDR_ZIPCODE")
    private String ADDR_ZIPCODE;
	
	@XmlElement(name="FLAG_MAKE")
    private String FLAG_MAKE;

	public String getCODE_ADDR_TYPE() {
		return CODE_ADDR_TYPE;
	}

	public void setCODE_ADDR_TYPE(String cODE_ADDR_TYPE) {
		CODE_ADDR_TYPE = cODE_ADDR_TYPE;
	}

	public String getNAME_ADDR_TYPE() {
		return NAME_ADDR_TYPE;
	}

	public void setNAME_ADDR_TYPE(String nAME_ADDR_TYPE) {
		NAME_ADDR_TYPE = nAME_ADDR_TYPE;
	}

	public String getADDR_PROV() {
		return ADDR_PROV;
	}

	public void setADDR_PROV(String aDDR_PROV) {
		ADDR_PROV = aDDR_PROV;
	}

	public String getADDR_CITY() {
		return ADDR_CITY;
	}

	public void setADDR_CITY(String aDDR_CITY) {
		ADDR_CITY = aDDR_CITY;
	}

	public String getADDR_COUNTY() {
		return ADDR_COUNTY;
	}

	public void setADDR_COUNTY(String aDDR_COUNTY) {
		ADDR_COUNTY = aDDR_COUNTY;
	}

	public String getADDR_COUNTRY() {
		return ADDR_COUNTRY;
	}

	public void setADDR_COUNTRY(String aDDR_COUNTRY) {
		ADDR_COUNTRY = aDDR_COUNTRY;
	}

	public String getADDR_VILLAGE() {
		return ADDR_VILLAGE;
	}

	public void setADDR_VILLAGE(String aDDR_VILLAGE) {
		ADDR_VILLAGE = aDDR_VILLAGE;
	}

	public String getADDR_HOUSE_NUM() {
		return ADDR_HOUSE_NUM;
	}

	public void setADDR_HOUSE_NUM(String aDDR_HOUSE_NUM) {
		ADDR_HOUSE_NUM = aDDR_HOUSE_NUM;
	}

	public String getNOSTRUCTURE_ADDR() {
		return NOSTRUCTURE_ADDR;
	}

	public void setNOSTRUCTURE_ADDR(String nOSTRUCTURE_ADDR) {
		NOSTRUCTURE_ADDR = nOSTRUCTURE_ADDR;
	}

	public String getADDR_ZIPCODE() {
		return ADDR_ZIPCODE;
	}

	public void setADDR_ZIPCODE(String aDDR_ZIPCODE) {
		ADDR_ZIPCODE = aDDR_ZIPCODE;
	}

	public String getFLAG_MAKE() {
		return FLAG_MAKE;
	}

	public void setFLAG_MAKE(String fLAG_MAKE) {
		FLAG_MAKE = fLAG_MAKE;
	}
}