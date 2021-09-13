package com.zebone.nhis.task.compay.vo;

import java.math.BigDecimal;
import java.util.Date;

import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Table;

/**
 * Table: VIEW_ITEM 2.2.4.2 诊疗项目及服务设施目录 市医保、花都公医
 * 
 * @since 2018-09-22 09:41:58
 */
@Table(value = "VIEW_ITEM")
public class HpViewItemVo {

	@Field(value = "SERIAL_ITEM")
	private BigDecimal serialItem;

	@Field(value = "STAT_TYPE")
	private String statType;

	@Field(value = "ITEM_CODE")
	private String itemCode;

	@Field(value = "ITEM_NAME")
	private String itemName;

	@Field(value = "UNIT")
	private String unit;

	@Field(value = "MT_FLAG")
	private String mtFlag;

	@Field(value = "STAPLE_FLAG")
	private String stapleFlag;

	@Field(value = "WL_FLAG")
	private String wlFlag;

	@Field(value = "BO_FLAG")
	private String boFlag;

	@Field(value = "VALID_FLAG")
	private String validFlag;

	@Field(value = "EFFECT_DATE")
	private Date effectDate;

	@Field(value = "EXPIRE_DATE")
	private Date expireDate;

	@Field(value = "SELF_SCALE")
	private BigDecimal selfScale;

	public BigDecimal getSerialItem() {
		return serialItem;
	}

	public void setSerialItem(BigDecimal serialItem) {
		this.serialItem = serialItem;
	}

	public String getStatType() {
		return statType;
	}

	public void setStatType(String statType) {
		this.statType = statType;
	}

	public String getItemCode() {
		return itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getMtFlag() {
		return mtFlag;
	}

	public void setMtFlag(String mtFlag) {
		this.mtFlag = mtFlag;
	}

	public String getStapleFlag() {
		return stapleFlag;
	}

	public void setStapleFlag(String stapleFlag) {
		this.stapleFlag = stapleFlag;
	}

	public String getWlFlag() {
		return wlFlag;
	}

	public void setWlFlag(String wlFlag) {
		this.wlFlag = wlFlag;
	}

	public String getBoFlag() {
		return boFlag;
	}

	public void setBoFlag(String boFlag) {
		this.boFlag = boFlag;
	}

	public String getValidFlag() {
		return validFlag;
	}

	public void setValidFlag(String validFlag) {
		this.validFlag = validFlag;
	}

	public Date getEffectDate() {
		return effectDate;
	}

	public void setEffectDate(Date effectDate) {
		this.effectDate = effectDate;
	}

	public Date getExpireDate() {
		return expireDate;
	}

	public void setExpireDate(Date expireDate) {
		this.expireDate = expireDate;
	}

	public BigDecimal getSelfScale() {
		return selfScale;
	}

	public void setSelfScale(BigDecimal selfScale) {
		this.selfScale = selfScale;
	}
	
	
}
