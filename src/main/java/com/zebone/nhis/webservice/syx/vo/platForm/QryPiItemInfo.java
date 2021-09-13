package com.zebone.nhis.webservice.syx.vo.platForm;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name="ItemInfo")
public class QryPiItemInfo {
	@XmlElement(name="itemNo")
	private String itemNo;
	
	@XmlElement(name="itemName")
	private String itemName;
	
	@XmlElement(name="spec")
	private String spec;
	
	@XmlElement(name="unit")
	private String unit;
	
	@XmlElement(name="quantity")
	private Double quantity;
	
	@XmlElement(name="payTime")
	private String payTime;
	
	@XmlElement(name="amount")
	private Double amount;
	
	private String feekindId;
	
	private String feekindName;
	

	public String getItemNo() {
		return itemNo;
	}

	public void setItemNo(String itemNo) {
		this.itemNo = itemNo;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getSpec() {
		return spec;
	}

	public void setSpec(String spec) {
		this.spec = spec;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public Double getQuantity() {
		return quantity;
	}

	public void setQuantity(Double quantity) {
		this.quantity = quantity;
	}

	public String getPayTime() {
		return payTime;
	}

	public void setPayTime(String payTime) {
		this.payTime = payTime;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public String getFeekindId() {
		return feekindId;
	}

	public void setFeekindId(String feekindId) {
		this.feekindId = feekindId;
	}

	public String getFeekindName() {
		return feekindName;
	}

	public void setFeekindName(String feekindName) {
		this.feekindName = feekindName;
	}
	
	
}
