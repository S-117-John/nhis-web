package com.zebone.nhis.webservice.vo.paidvo;

import javax.xml.bind.annotation.XmlElement;

public class ResPaidVo {
	private Double amount;
	private Double amountPi;
	private String nameCg;
	private Double price;

	private Integer quan;
	private String spec;
	private String unit;
	
	private String itemCate;
    
	@XmlElement(name = "itemCate")
	public String getItemCate() {
		return itemCate;
	}

	public void setItemCate(String itemCate) {
		this.itemCate = itemCate;
	}

	@XmlElement(name = "amount")
	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}
	
	@XmlElement(name = "amountPi")
	public Double getAmountPi() {
		return amountPi;
	}

	public void setAmountPi(Double amountPi) {
		this.amountPi = amountPi;
	}

	@XmlElement(name = "nameCg")
	public String getNameCg() {
		return nameCg;
	}

	public void setNameCg(String nameCg) {
		this.nameCg = nameCg;
	}
     
	@XmlElement(name = "price")
	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}
    
	@XmlElement(name = "quan")
	public Integer getQuan() {
		return quan;
	}

	public void setQuan(Integer quan) {
		this.quan = quan;
	}
	
	@XmlElement(name = "spec")
	public String getSpec() {
		return spec;
	}

	public void setSpec(String spec) {
		this.spec = spec;
	}

	@XmlElement(name = "unit")
	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}
}
