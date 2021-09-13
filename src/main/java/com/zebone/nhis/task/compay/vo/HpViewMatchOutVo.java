package com.zebone.nhis.task.compay.vo;

import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Table;

/**
 * VIEW_MATCH_OUT 2.2.4.3 目录对应表  省内异地
 * 
 * @author JAY
 * 
 */
@Table(value = "VIEW_MATCH_OUT")
public class HpViewMatchOutVo {

	@Field(value = "MATCH_TYPE")
	private String matchType;

	@Field(value = "ITEM_CODE")
	private String itemCode;

	@Field(value = "ITEM_NAME")
	private String itemName;

	@Field(value = "ITEM_CODE_OUT")
	private String itemCodeOut;

	@Field(value = "ITEM_NAME_OUT")
	private String itemNameOut;
	
	@Field(value = "MODEL")
	private String model;

	@Field(value = "HOSPITAL_ID")
	private String hospitalId;

	@Field(value = "HOSP_CODE")
	private String hospCode;

	@Field(value = "HOSP_NAME")
	private String hospName;

	@Field(value = "HOSP_MODEL")
	private String hospModel;

	@Field(value = "HOSP_STANDARD")
	private String hospStandard;

	@Field(value = "STAPLE_FLAG")
	private String stapleFlag;

	public String getMatchType() {
		return matchType;
	}

	public void setMatchType(String matchType) {
		this.matchType = matchType;
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

	public String getItemCodeOut() {
		return itemCodeOut;
	}

	public void setItemCodeOut(String itemCodeOut) {
		this.itemCodeOut = itemCodeOut;
	}

	public String getItemNameOut() {
		return itemNameOut;
	}

	public void setItemNameOut(String itemNameOut) {
		this.itemNameOut = itemNameOut;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getHospitalId() {
		return hospitalId;
	}

	public void setHospitalId(String hospitalId) {
		this.hospitalId = hospitalId;
	}

	public String getHospCode() {
		return hospCode;
	}

	public void setHospCode(String hospCode) {
		this.hospCode = hospCode;
	}

	public String getHospName() {
		return hospName;
	}

	public void setHospName(String hospName) {
		this.hospName = hospName;
	}

	public String getHospModel() {
		return hospModel;
	}

	public void setHospModel(String hospModel) {
		this.hospModel = hospModel;
	}

	public String getHospStandard() {
		return hospStandard;
	}

	public void setHospStandard(String hospStandard) {
		this.hospStandard = hospStandard;
	}

	public String getStapleFlag() {
		return stapleFlag;
	}

	public void setStapleFlag(String stapleFlag) {
		this.stapleFlag = stapleFlag;
	}

	
}
