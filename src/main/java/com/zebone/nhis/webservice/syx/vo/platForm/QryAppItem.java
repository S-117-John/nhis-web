package com.zebone.nhis.webservice.syx.vo.platForm;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name="item")
public class QryAppItem {
	@XmlElement(name="pk")
	private String pk;
	
	@XmlElement(name="code")
	private String code;
	
	@XmlElement(name="name")
	private String name;
	
	@XmlElement(name="pkFather")
	private String pkFather;
	
	@XmlElement(name="prov")
	private String prov;
	
	@XmlElement(name="city")
	private String city;
	
	@XmlElement(name="dist")
	private String dist;
	
	@XmlElement(name="codeDiv")
	private String codeDiv;
	
	@XmlElement(name="nameDiv")
	private String nameDiv;

	@XmlElement(name="codeArea")
	private String codeArea;

	@XmlElement(name="nameArea")
	private String nameArea;

	@XmlElement(name="lcdepCode")
	private String lcdepCode;

	public String getLcdepCode() {
		return lcdepCode;
	}

	public void setLcdepCode(String lcdepCode) {
		this.lcdepCode = lcdepCode;
	}

	public String getCodeArea() {
		return codeArea;
	}

	public void setCodeArea(String codeArea) {
		this.codeArea = codeArea;
	}

	public String getNameArea() {
		return nameArea;
	}

	public void setNameArea(String nameArea) {
		this.nameArea = nameArea;
	}

	public String getPk() {
		return pk;
	}

	public void setPk(String pk) {
		this.pk = pk;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPkFather() {
		return pkFather;
	}

	public void setPkFather(String pkFather) {
		this.pkFather = pkFather;
	}

	public String getProv() {
		return prov;
	}

	public void setProv(String prov) {
		this.prov = prov;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getDist() {
		return dist;
	}

	public void setDist(String dist) {
		this.dist = dist;
	}

	public String getCodeDiv() {
		return codeDiv;
	}

	public void setCodeDiv(String codeDiv) {
		this.codeDiv = codeDiv;
	}

	public String getNameDiv() {
		return nameDiv;
	}

	public void setNameDiv(String nameDiv) {
		this.nameDiv = nameDiv;
	}
}
