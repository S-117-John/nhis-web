package com.zebone.nhis.webservice.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 灵璧自助住院费用清单响应消息体
 */
@XmlRootElement(name = "items")
@XmlAccessorType(XmlAccessType.FIELD)
public class LbBlCgIpVo {
	/*
	 * 操作时间-费用发生的时间
	 */
	@XmlElement(name="operateTime")
	private String operateTime;
	/*
	 * 科室名称
	 */
	@XmlElement(name="deptName")
	private String deptName;
	/*
	 * 项目编码
	 */
	@XmlElement(name="itemId")
	private String itemId;
	/*
	 * 项目类别
	 */
	@XmlElement(name="itemType")
	private String itemType;
	/*
	 * 项目名称
	 */
	@XmlElement(name="itemName")
	private String itemName;
	/*
	 * 规格
	 */
	@XmlElement(name="specs")
	private String specs;
	/*
	 * 单位
	 */
	@XmlElement(name="unit")
	private String unit;
	/*
	 * 项目数量
	 */
	@XmlElement(name="qty")
	private String qty;
	/*
	 * 项目单价
	 */
	@XmlElement(name="price")
	private String price;
	/*
	 * 项目总费用
	 */
	@XmlElement(name="itemFee")
	private String itemFee;
	/*
	 * 备注
	 */
	@XmlElement(name="reserved")
	private String reserved;
	public String getOperateTime() {
		return operateTime;
	}
	public void setOperateTime(String operateTime) {
		this.operateTime = operateTime;
	}
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	public String getItemId() {
		return itemId;
	}
	public void setItemId(String itemId) {
		this.itemId = itemId;
	}
	public String getItemType() {
		return itemType;
	}
	public void setItemType(String itemType) {
		this.itemType = itemType;
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public String getSpecs() {
		return specs;
	}
	public void setSpecs(String specs) {
		this.specs = specs;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public String getQty() {
		return qty;
	}
	public void setQty(String qty) {
		this.qty = qty;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getItemFee() {
		return itemFee;
	}
	public void setItemFee(String itemFee) {
		this.itemFee = itemFee;
	}
	public String getReserved() {
		return reserved;
	}
	public void setReserved(String reserved) {
		this.reserved = reserved;
	}
}
