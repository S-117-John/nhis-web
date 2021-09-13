package com.zebone.nhis.compay.pub.syx.vo;

import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Table;

/**
 * VIEW_MATCH 2.2.4.3 目录对应表 市医保、花都公医
 * 
 * @author JAY
 * 
 */
@Table(value = "VIEW_MATCH")
public class ViewMatch {

	@Field(value = "MATCH_TYPE")
	private String matchType;

	@Field(value = "ITEM_CODE")
	private String itemCode;

	@Field(value = "ITEM_NAME")
	private String itemName;

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

	@Field(value = "AUDIT_FLAG")
	private String auditFlag;

	public String getMatchType() {
		return this.matchType;
	}

	public void setMatchType(String matchType) {
		this.matchType = matchType;
	}

	public String getItemCode() {
		return this.itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	public String getItemName() {
		return this.itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getModel() {
		return this.model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getHospitalId() {
		return this.hospitalId;
	}

	public void setHospitalId(String hospitalId) {
		this.hospitalId = hospitalId;
	}

	public String getHospCode() {
		return this.hospCode;
	}

	public void setHospCode(String hospCode) {
		this.hospCode = hospCode;
	}

	public String getHospName() {
		return this.hospName;
	}

	public void setHospName(String hospName) {
		this.hospName = hospName;
	}

	public String getHospModel() {
		return this.hospModel;
	}

	public void setHospModel(String hospModel) {
		this.hospModel = hospModel;
	}

	public String getHospStandard() {
		return this.hospStandard;
	}

	public void setHospStandard(String hospStandard) {
		this.hospStandard = hospStandard;
	}

	public String getStapleFlag() {
		return this.stapleFlag;
	}

	public void setStapleFlag(String stapleFlag) {
		this.stapleFlag = stapleFlag;
	}

	public String getAuditFlag() {
		return this.auditFlag;
	}

	public void setAuditFlag(String auditFlag) {
		this.auditFlag = auditFlag;
	}
}
