package com.zebone.nhis.common.module.cn.ipdw;

import java.util.Date;

import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Field.FieldType;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;

/**
 * Table: CN_ORD_HERB
 * 
 * @since 2016-09-12 10:30:28
 */
@Table(value = "CN_ORD_HERB")
public class CnOrdHerb {

	@PK
	@Field(value = "PK_ORDHERB", id = KeyId.UUID)
	private String pkOrdherb;

	@Field(value = "PK_CNORD")
	private String pkCnord;

	@Field(value = "SORT_NO")
	private Integer sortNo;

	@Field(value = "PK_PD")
	private String pkPd;

	@Field(value = "PRICE")
	private Double price;

	@Field(value = "PK_UNIT")
	private String pkUnit;

	@Field(value = "QUAN")
	private Double quan;

	@Field(value = "NOTE_USE")
	private String noteUse;

	@Field(value = "EU_HERBTYPE")
	private String euHerbtype;

	@Field(userfield = "pkEmp", userfieldscop = FieldType.INSERT)
	private String creator;

	@Field(value = "CREATE_TIME", date = FieldType.INSERT)
	private Date createTime;

	@Field(userfield = "pkEmp", userfieldscop = FieldType.ALL)
	private String modifier;

	@Field(value = "MODITY_TIME")
	private Date modityTime;

	@Field(value = "DEL_FLAG")
	private String delFlag;

	@Field(date = FieldType.ALL)
	private Date ts;
	
	@Field(value = "DT_HERBUSAGE")
	private String dtHerbusage;

	@Field(value = "FLAG_FIT")
	private String flagFit;

	@Field(value = "DESC_FIT")
	private String descFit;

	// 门诊收费加字段（只用做数据传输）
	private String namePd;

	public String getPkOrdherb() {

		return this.pkOrdherb;
	}

	public void setPkOrdherb(String pkOrdherb) {

		this.pkOrdherb = pkOrdherb;
	}

	public String getPkCnord() {

		return this.pkCnord;
	}

	public void setPkCnord(String pkCnord) {

		this.pkCnord = pkCnord;
	}

	public Integer getSortNo() {

		return this.sortNo;
	}

	public void setSortNo(Integer sortNo) {

		this.sortNo = sortNo;
	}

	public String getPkPd() {

		return this.pkPd;
	}

	public void setPkPd(String pkPd) {

		this.pkPd = pkPd;
	}

	public Double getPrice() {

		return this.price;
	}

	public void setPrice(Double price) {

		this.price = price;
	}

	public String getPkUnit() {

		return this.pkUnit;
	}

	public void setPkUnit(String pkUnit) {

		this.pkUnit = pkUnit;
	}

	public Double getQuan() {

		return this.quan;
	}

	public void setQuan(Double quan) {

		this.quan = quan;
	}

	public String getNoteUse() {

		return this.noteUse;
	}

	public void setNoteUse(String noteUse) {

		this.noteUse = noteUse;
	}

	public String getEuHerbtype() {

		return this.euHerbtype;
	}

	public void setEuHerbtype(String euHerbtype) {

		this.euHerbtype = euHerbtype;
	}

	public String getCreator() {

		return this.creator;
	}

	public void setCreator(String creator) {

		this.creator = creator;
	}

	public Date getCreateTime() {

		return this.createTime;
	}

	public void setCreateTime(Date createTime) {

		this.createTime = createTime;
	}

	public String getModifier() {

		return this.modifier;
	}

	public void setModifier(String modifier) {

		this.modifier = modifier;
	}

	public Date getModityTime() {

		return this.modityTime;
	}

	public void setModityTime(Date modityTime) {

		this.modityTime = modityTime;
	}

	public String getDelFlag() {

		return this.delFlag;
	}

	public void setDelFlag(String delFlag) {

		this.delFlag = delFlag;
	}

	public Date getTs() {

		return this.ts;
	}

	public void setTs(Date ts) {

		this.ts = ts;
	}

	public String getNamePd() {

		return namePd;
	}

	public void setNamePd(String namePd) {

		this.namePd = namePd;
	}

	public String getDtHerbusage() {
		return dtHerbusage;
	}

	public void setDtHerbusage(String dtHerbusage) {
		this.dtHerbusage = dtHerbusage;
	}

	public String getFlagFit() {
		return flagFit;
	}

	public void setFlagFit(String flagFit) {
		this.flagFit = flagFit;
	}

	public String getDescFit() {
		return descFit;
	}

	public void setDescFit(String descFit) {
		this.descFit = descFit;
	}
}