package com.zebone.nhis.webservice.vo.tmisvo.item;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 输血返回构造xml
 * @author frank
 *收费项目信息
 */
@XmlRootElement(name = "FeeItemInfo")
@XmlAccessorType(XmlAccessType.FIELD)
public class ResponseFeeItemInfoVo {
	@XmlElement(name = "HospHISCode")
	public String hospHISCode;
	
	@XmlElement(name = "HospName")
	public String hospName;
	
	@XmlElement(name = "Name")
	public String name;
	
	@XmlElement(name = "Code")
	public String code;
	
	@XmlElement(name = "FeeTypeID")
	public String feeTypeID;
	@XmlElement(name = "Unit")
	public String unit;
	@XmlElement(name = "Price")
	public String price;
	public String getHospHISCode() {
		return hospHISCode;
	}
	public void setHospHISCode(String hospHISCode) {
		this.hospHISCode = hospHISCode;
	}
	public String getHospName() {
		return hospName;
	}
	public void setHospName(String hospName) {
		this.hospName = hospName;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getFeeTypeID() {
		return feeTypeID;
	}
	public void setFeeTypeID(String feeTypeID) {
		this.feeTypeID = feeTypeID;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	
	
	
}
