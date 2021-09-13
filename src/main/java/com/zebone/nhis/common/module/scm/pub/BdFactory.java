package com.zebone.nhis.common.module.scm.pub;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;

/**
 * Table: BD_FACTORY - bd_factory
 * 
 * @since 2016-10-22 09:12:16
 */
@Table(value = "BD_FACTORY")
public class BdFactory extends BaseModule {

	@PK
	@Field(value = "PK_FACTORY", id = KeyId.UUID)
	private String pkFactory;

	@Field(value = "CODE")
	private String code;

	@Field(value = "NAME")
	private String name;

	@Field(value = "SPCODE")
	private String spcode;

	@Field(value = "D_CODE")
	private String dCode;

	@Field(value = "SHORT_NAME")
	private String shortName;

	@Field(value = "NOTE")
	private String note;

	@Field(value = "FLAG_STOP")
	private String flagStop;

	@Field(value = "license_no")
	private String licenseNo;

	@Field(value = "date_valid")
	private Date dateValid;

	// 修改状态   修改为 1
	private String status;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getPkFactory() {
		return this.pkFactory;
	}

	public void setPkFactory(String pkFactory) {
		this.pkFactory = pkFactory;
	}

	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSpcode() {
		return this.spcode;
	}

	public void setSpcode(String spcode) {
		this.spcode = spcode;
	}

	public String getdCode() {
		return this.dCode;
	}

	public void setdCode(String dCode) {
		this.dCode = dCode;
	}

	public String getShortName() {
		return this.shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public String getNote() {
		return this.note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getFlagStop() {
		return this.flagStop;
	}

	public void setFlagStop(String flagStop) {
		this.flagStop = flagStop;
	}

	public String getLicenseNo() {
		return licenseNo;
	}

	public void setLicenseNo(String licenseNo) {
		this.licenseNo = licenseNo;
	}

	public Date getDateValid() {
		return dateValid;
	}

	public void setDateValid(Date dateValid) {
		this.dateValid = dateValid;
	}

}