package com.zebone.nhis.webservice.syx.vo.piAddr;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlRootElement(name = "AddrReq")
public class AddrReq {

	private String username;
	
	private String treatCard;
	
	private String presNum;
	
	private String consignee;
	
	private String conTel;
	
	private String provinces;
	
	private String city;
	
	private String zone;
	
	private String addrDetail;
	
	private String fee;

	@XmlElement(name="username")
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@XmlElement(name="treat_card")
	public String getTreatCard() {
		return treatCard;
	}

	public void setTreatCard(String treatCard) {
		this.treatCard = treatCard;
	}

	@XmlElement(name="pres_num")
	public String getPresNum() {
		return presNum;
	}

	public void setPresNum(String presNum) {
		this.presNum = presNum;
	}

	@XmlElement(name="consignee")
	public String getConsignee() {
		return consignee;
	}

	public void setConsignee(String consignee) {
		this.consignee = consignee;
	}

	@XmlElement(name="con_tel")
	public String getConTel() {
		return conTel;
	}

	public void setConTel(String conTel) {
		this.conTel = conTel;
	}

	@XmlElement(name="provinces")
	public String getProvinces() {
		return provinces;
	}

	public void setProvinces(String provinces) {
		this.provinces = provinces;
	}

	@XmlElement(name="city")
	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	@XmlElement(name="zone")
	public String getZone() {
		return zone;
	}

	public void setZone(String zone) {
		this.zone = zone;
	}

	@XmlElement(name="addr_detail")
	public String getAddrDetail() {
		return addrDetail;
	}

	public void setAddrDetail(String addrDetail) {
		this.addrDetail = addrDetail;
	}

	@XmlElement(name="fee")
	public String getFee() {
		return fee;
	}

	public void setFee(String fee) {
		this.fee = fee;
	}
	
	
}
