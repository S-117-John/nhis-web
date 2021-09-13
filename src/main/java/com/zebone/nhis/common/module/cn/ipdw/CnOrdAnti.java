package com.zebone.nhis.common.module.cn.ipdw;

import java.util.Date;

import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.FieldType;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;

@Table(value="CN_ORD_ANTI")
public class CnOrdAnti {

	@PK
	@Field(value="PK_ORDANTI",id=KeyId.UUID)
    private String pkOrdanti;

	@Field(value="PK_ORG")
    private String pkOrg;
	
	@Field(value="PK_CNORD")
    private String pkCnord;
	
	@Field(value="EU_TYPE")
	private String euType;
	
	@Field(value="EU_PREVTYPE")
	private String euPrevtype;
	
	@Field(value="EU_THERATYPE")
	private String euTheratype;
	
	@Field(value="DT_OPPREVTYPE")
	private String dtOpprevtype;
	
	@Field(value="DATE_ENTRY")
	private Date dateEntry;
	
	@Field(value="PK_EMP_ENTRY")
	private String pkEmpEntry;
	
	@Field(value="NAME_EMP_ENTRY")
	private String nameEmpEntry;
	
	@Field(userfield="pkEmp",userfieldscop=FieldType.INSERT)
    private String creator;

	@Field(value="CREATE_TIME",date=FieldType.INSERT)
    private Date createTime;

	@Field(userfield="pkEmp",userfieldscop=FieldType.ALL)
    private String modifier;

	@Field(value="MODITY_TIME")
    private Date modityTime;

	@Field(value="DEL_FLAG")
    private String delFlag;

	@Field(date=FieldType.ALL)
    private Date ts;

	public String getPkOrdanti() {
		return pkOrdanti;
	}

	public void setPkOrdanti(String pkOrdanti) {
		this.pkOrdanti = pkOrdanti;
	}

	public String getPkOrg() {
		return pkOrg;
	}

	public void setPkOrg(String pkOrg) {
		this.pkOrg = pkOrg;
	}

	public String getPkCnord() {
		return pkCnord;
	}

	public void setPkCnord(String pkCnord) {
		this.pkCnord = pkCnord;
	}

	public String getEuType() {
		return euType;
	}

	public void setEuType(String euType) {
		this.euType = euType;
	}

	public String getEuPrevtype() {
		return euPrevtype;
	}

	public void setEuPrevtype(String euPrevtype) {
		this.euPrevtype = euPrevtype;
	}

	public String getEuTheratype() {
		return euTheratype;
	}

	public void setEuTheratype(String euTheratype) {
		this.euTheratype = euTheratype;
	}

	public String getDtOpprevtype() {
		return dtOpprevtype;
	}

	public void setDtOpprevtype(String dtOpprevtype) {
		this.dtOpprevtype = dtOpprevtype;
	}

	public Date getDateEntry() {
		return dateEntry;
	}

	public void setDateEntry(Date dateEntry) {
		this.dateEntry = dateEntry;
	}

	public String getPkEmpEntry() {
		return pkEmpEntry;
	}

	public void setPkEmpEntry(String pkEmpEntry) {
		this.pkEmpEntry = pkEmpEntry;
	}

	public String getNameEmpEntry() {
		return nameEmpEntry;
	}

	public void setNameEmpEntry(String nameEmpEntry) {
		this.nameEmpEntry = nameEmpEntry;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getModifier() {
		return modifier;
	}

	public void setModifier(String modifier) {
		this.modifier = modifier;
	}

	public Date getModityTime() {
		return modityTime;
	}

	public void setModityTime(Date modityTime) {
		this.modityTime = modityTime;
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
