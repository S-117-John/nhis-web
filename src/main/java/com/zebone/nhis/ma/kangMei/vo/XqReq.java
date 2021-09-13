package com.zebone.nhis.ma.kangMei.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType(name = "", propOrder = {"medicines","dose","unit","herbUnitPrice","goodsNum","remark","mUsage"
})
@XmlRootElement(name = "XqReq")
public class XqReq {

	private String medicines;

	private String dose;

	private String unit;

	private String herbUnitPrice;

	private String goodsNum;

	private String remark;

	private String mUsage;

	@XmlElement(name = "medicines")
	public String getMedicines() {
		return medicines;
	}

	public void setMedicines(String medicines) {
		this.medicines = medicines;
	}

	@XmlElement(name = "dose")
	public String getDose() {
		return dose;
	}

	public void setDose(String dose) {
		this.dose = dose;
	}

	@XmlElement(name = "unit")
	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	@XmlElement(name = "herb_unit_price")
	public String getHerbUnitPrice() {
		return herbUnitPrice;
	}

	public void setHerbUnitPrice(String herbUnitPrice) {
		this.herbUnitPrice = herbUnitPrice;
	}

	@XmlElement(name = "goods_num")
	public String getGoodsNum() {
		return goodsNum;
	}

	public void setGoodsNum(String goodsNum) {
		this.goodsNum = goodsNum;
	}

	@XmlElement(name = "remark")
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@XmlElement(name = "m_usage")
	public String getmUsage() {
		return mUsage;
	}

	public void setmUsage(String mUsage) {
		this.mUsage = mUsage;
	}

}
