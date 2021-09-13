package com.zebone.nhis.ma.pub.zsrm.vo;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(value = XmlAccessType.FIELD)
public class PiRegVo {
	//国籍
	@XmlElement(name = "Nationality")
	private String dtCountry;
	//证件类型
	@XmlElement(name = "DocuType")
	private String dtIdtype;
	//证件号码
	@XmlElement(name = "DocuId")
	private String idNo;
	//健康e卡号
	@XmlElement(name = "HealthCardId")
	private String healthCardId;

	//审核不通过原因
	@XmlElement(name = "Reason")
	private String reason;
    //实体卡卡号
	@XmlElement(name = "IcCardId")
	private String icCardId;
   //电子健康卡ID
   @XmlElement(name = "EleHealthCarId")
	private  String eleHealthCarId;

	@XmlElement(name = "Count")
	private  String count;

	//干部级别
	@XmlElement(name = "CadreLevel")
	private  String cadreLevel;
	
	
	public String getCadreLevel() {
		return cadreLevel;
	}

	public void setCadreLevel(String cadreLevel) {
		this.cadreLevel = cadreLevel;
	}

	public String getCount() {
		return count;
	}

	public void setCount(String count) {
		this.count = count;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getHealthCardId() {
		return healthCardId;
	}

	public void setHealthCardId(String healthCardId) {
		this.healthCardId = healthCardId;
	}

	public String getDtCountry() {
		return dtCountry;
	}

	public void setDtCountry(String dtCountry) {
		this.dtCountry = dtCountry;
	}

	public String getDtIdtype() {
		return dtIdtype;
	}

	public void setDtIdtype(String dtIdtype) {
		this.dtIdtype = dtIdtype;
	}

	public String getIdNo() {
		return idNo;
	}

	public void setIdNo(String idNo) {
		this.idNo = idNo;
	}

	public String getIcCardId() {
		return icCardId;
	}

	public void setIcCardId(String icCardId) {
		this.icCardId = icCardId;
	}

	public String getEleHealthCarId() {
		return eleHealthCarId;
	}

	public void setEleHealthCarId(String eleHealthCarId) {
		this.eleHealthCarId = eleHealthCarId;
	}
}
