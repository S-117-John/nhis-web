package com.zebone.nhis.common.module.base.bd.code;

import java.util.Date;

import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.FieldType;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;

/**
 * Table: INS_GZGY_DISEASE
 * @author c
 *
 */
@Table(value="INS_GZGY_DISEASE")
public class InsGzgyDisease {
	@PK
	@Field(value="PK_GZGYDISEASE",id=KeyId.UUID)
	private String pkGzgydisease;
	
	@Field(value="DT_DISEASETYPE")
	private String dtDiseasetype;
	
	@Field(value="PK_DIAG")
	private String pkDiag;
	
	@Field(value="CODE_DIAG")
	private String codeDiag;
	
	@Field(value="NAME_DiAG")
	private String nameDiag;
	
	@Field(value="AMOUNT")
	private Double amount;
	
	@Field(value="AMOUNT_YEAR")
	private Double amountYear;
	
	@Field(value="NOTE")
	private String note;
	
	@Field(value="CREATE_TIME",date=FieldType.INSERT)
	private Date createTime;
	
	@Field(value="CREATOR",userfieldscop=FieldType.INSERT)
	private String creator;
	
	@Field(value="DEL_FLAG")
	private String delFlag = "0";
	
	@Field(date=FieldType.ALL)
	private Date ts;

	public Double getAmountYear() {
		return amountYear;
	}

	public void setAmountYear(Double amountYear) {
		this.amountYear = amountYear;
	}

	public String getPkGzgydisease() {
		return pkGzgydisease;
	}

	public void setPkGzgydisease(String pkGzgydisease) {
		this.pkGzgydisease = pkGzgydisease;
	}

	public String getDtDiseasetype() {
		return dtDiseasetype;
	}

	public void setDtDiseasetype(String dtDiseasetype) {
		this.dtDiseasetype = dtDiseasetype;
	}

	public String getPkDiag() {
		return pkDiag;
	}

	public void setPkDiag(String pkDiag) {
		this.pkDiag = pkDiag;
	}

	public String getCodeDiag() {
		return codeDiag;
	}

	public void setCodeDiag(String codeDiag) {
		this.codeDiag = codeDiag;
	}

	public String getNameDiag() {
		return nameDiag;
	}

	public void setNameDiag(String nameDiag) {
		this.nameDiag = nameDiag;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}

	public Date getTs() {
		return ts;
	}

	public void setTs(Date ts) {
		this.ts = ts;
	}
	
	
	
}
