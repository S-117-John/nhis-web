package com.zebone.nhis.task.compay.vo;

import java.math.BigDecimal;
import java.util.Date;

import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Table;

/**
 * Table: VIEW_ITEM_OUT 2.2.4.2 诊疗项目及服务设施目录 省内异地
 * 
 * @since 2018-09-22 09:41:58
 */
@Table(value = "VIEW_ITEM_OUT")
public class HpViewItemOutVo {

	@Field(value = "SERIAL_ITEM_OUTS")
	private BigDecimal serialItemOut;

	@Field(value = "STAT_TYPE")
	private String statType;

	@Field(value = "ITEM_CODE")
	private String itemCode;

	@Field(value = "ITEM_NAME")
	private String itemName;

	@Field(value = "ITEM_CODE_OUTS")
	private String itemCodeOuts;

	@Field(value = "ITEM_NAME_OUTS")
	private String itemNameOuts;
	
	@Field(value = "UNIT")
	private String unit;

	@Field(value = "MT_FLAG")
	private String mtFlag;

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

	public BigDecimal getSerialItemOut() {
		return serialItemOut;
	}

	public void setSerialItemOut(BigDecimal serialItemOut) {
		this.serialItemOut = serialItemOut;
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
	
	public String getItemCodeOuts() {
		return itemCodeOuts;
	}

	public void setItemCodeOuts(String itemCodeOuts) {
		this.itemCodeOuts = itemCodeOuts;
	}

	public String getItemNameOuts() {
		return itemNameOuts;
	}

	public void setItemNameOuts(String itemNameOuts) {
		this.itemNameOuts = itemNameOuts;
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
