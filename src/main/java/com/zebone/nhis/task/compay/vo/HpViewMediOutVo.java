package com.zebone.nhis.task.compay.vo;

import java.math.BigDecimal;
import java.util.Date;

import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Table;

/**
 * Table: VIEW_MEDI_OUT 2.2.4.1 药品目录  省内异地
 * 
 * @since 2018-09-22 09:42:06
 */
@Table(value = "VIEW_MEDI_OUT")
public class HpViewMediOutVo {
	
	@Field(value = "SERIAL_MEDI_OUTS")
	private String serialMediOut;
	
	@Field(value = "MEDI_ITEM_TYPE")
	private String mediItemType;

	@Field(value = "CLASS_CODE")
	private String classCode;

	@Field(value = "STAT_TYPE")
	private String statType;

	@Field(value = "MEDI_CODE")
	private String mediCode;

	@Field(value = "MEDI_NAME")
	private String mediName;

	@Field(value = "MEDI_CODE_OUTS")
	private String mediCodeOuts;

	@Field(value = "MEDI_NAME_OUTS")
	private String mediNameOuts;
	
	@Field(value = "ENGLISH_NAME")
	private String englishName;

	@Field(value = "CODE_WB")
	private String codeWb;

	@Field(value = "CODE_PY")
	private String codePy;

	@Field(value = "MODEL")
	private String model;

	@Field(value = "STAPLE_FLAG")
	private String stapleFlag;

	@Field(value = "OTC")
	private String otc;

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

	@Field(value = "RANGE_FLAG")
	private String rangeFlag;

	@Field(value = "RANGE")
	private String range;

	public String getSerialMediOut() {
		return serialMediOut;
	}

	public void setSerialMediOut(String serialMediOut) {
		this.serialMediOut = serialMediOut;
	}

	public String getMediItemType() {
		return mediItemType;
	}

	public void setMediItemType(String mediItemType) {
		this.mediItemType = mediItemType;
	}

	public String getClassCode() {
		return classCode;
	}

	public void setClassCode(String classCode) {
		this.classCode = classCode;
	}

	public String getStatType() {
		return statType;
	}

	public void setStatType(String statType) {
		this.statType = statType;
	}

	public String getMediCode() {
		return mediCode;
	}

	public void setMediCode(String mediCode) {
		this.mediCode = mediCode;
	}

	public String getMediName() {
		return mediName;
	}

	public void setMediName(String mediName) {
		this.mediName = mediName;
	}

	public String getMediCodeOuts() {
		return mediCodeOuts;
	}

	public void setMediCodeOuts(String mediCodeOuts) {
		this.mediCodeOuts = mediCodeOuts;
	}

	public String getMediNameOuts() {
		return mediNameOuts;
	}

	public void setMediNameOuts(String mediNameOuts) {
		this.mediNameOuts = mediNameOuts;
	}

	public String getEnglishName() {
		return englishName;
	}

	public void setEnglishName(String englishName) {
		this.englishName = englishName;
	}

	public String getCodeWb() {
		return codeWb;
	}

	public void setCodeWb(String codeWb) {
		this.codeWb = codeWb;
	}

	public String getCodePy() {
		return codePy;
	}

	public void setCodePy(String codePy) {
		this.codePy = codePy;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getStapleFlag() {
		return stapleFlag;
	}

	public void setStapleFlag(String stapleFlag) {
		this.stapleFlag = stapleFlag;
	}

	public String getOtc() {
		return otc;
	}

	public void setOtc(String otc) {
		this.otc = otc;
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

	public String getRangeFlag() {
		return rangeFlag;
	}

	public void setRangeFlag(String rangeFlag) {
		this.rangeFlag = rangeFlag;
	}

	public String getRange() {
		return range;
	}

	public void setRange(String range) {
		this.range = range;
	}	
}
