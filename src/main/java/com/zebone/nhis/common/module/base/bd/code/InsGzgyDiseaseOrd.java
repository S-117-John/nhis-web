package com.zebone.nhis.common.module.base.bd.code;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.FieldType;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;

/**
 * Table:INS_GZGY_DISEASE_ORD
 * @author c
 *
 */
@Table("INS_GZGY_DISEASE_ORD")
public class InsGzgyDiseaseOrd {
	@PK
	@Field(value="PK_DISEASEORD",id=KeyId.UUID)
	private String pkDiseaseord;
	
	@Field(value="PK_GZGYDISEASE")
	private String pkGzgydisease;
	
	@Field(value="EU_ORDTYPE")
	private String euOrdtype;
	
	@Field(value="PK_ORD")
	private String pkOrd;
	
	@Field(value="CODE_ORD")
	private String codeOrd;
	
	@Field(value="NAME_ORD")
	private String nameOrd;
	
	@Field(value="DAYS")
	private Long days;
	
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

	public String getPkDiseaseord() {
		return pkDiseaseord;
	}

	public void setPkDiseaseord(String pkDiseaseord) {
		this.pkDiseaseord = pkDiseaseord;
	}

	public String getPkGzgydisease() {
		return pkGzgydisease;
	}

	public void setPkGzgydisease(String pkGzgydisease) {
		this.pkGzgydisease = pkGzgydisease;
	}

	public String getEuOrdtype() {
		return euOrdtype;
	}

	public void setEuOrdtype(String euOrdtype) {
		this.euOrdtype = euOrdtype;
	}

	public String getPkOrd() {
		return pkOrd;
	}

	public void setPkOrd(String pkOrd) {
		this.pkOrd = pkOrd;
	}

	public String getCodeOrd() {
		return codeOrd;
	}

	public void setCodeOrd(String codeOrd) {
		this.codeOrd = codeOrd;
	}

	public String getNameOrd() {
		return nameOrd;
	}

	public void setNameOrd(String nameOrd) {
		this.nameOrd = nameOrd;
	}

	public Long getDays() {
		return days;
	}

	public void setDays(Long days) {
		this.days = days;
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
