package com.zebone.nhis.task.scm.entity;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: MID_DRUGSTORE_UPLOAD_HIS -  
 *
 * @since 2021-07-09 09:19:50
 */
@Table(value="MID_DRUGSTORE_UPLOAD_HIS")
public class MidDrugstoreUploadHis  {

	@PK
	@Field(value="PK_MID_DRUGSTORE_UPLOAD_HIS",id=KeyId.UUID)
    private String pkMidDrugstoreUploadHis;

	@Field(value="CODE")
    private String code;

	@Field(value="AMOUNT_COST")
    private Double amountCost;
	
	@Field(value="QUAN_MIN")
    private Double quanMin;	
	
	@Field(value="DATE_UPLOAD")
    private Date dateUpload;

	public String getPkMidDrugstoreUploadHis() {
		return pkMidDrugstoreUploadHis;
	}

	public void setPkMidDrugstoreUploadHis(String pkMidDrugstoreUploadHis) {
		this.pkMidDrugstoreUploadHis = pkMidDrugstoreUploadHis;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Double getAmountCost() {
		return amountCost;
	}

	public void setAmountCost(Double amountCost) {
		this.amountCost = amountCost;
	}

	public Double getQuanMin() {
		return quanMin;
	}

	public void setQuanMin(Double quanMin) {
		this.quanMin = quanMin;
	}

	public Date getDateUpload() {
		return dateUpload;
	}

	public void setDateUpload(Date dateUpload) {
		this.dateUpload = dateUpload;
	}

}