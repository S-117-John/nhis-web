package com.zebone.nhis.common.module.base.bd.price;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.zebone.nhis.common.module.base.bd.code.InsGzgyHpDiv;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.FieldType;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;

/**
 * Table:INS_GZGY_DIV_BED
 * @author 
 *
 */
@Table(value="INS_GZGY_DIV_BED")
public class InsGzgyDivBed {
	
	@PK
	@Field(value="PK_BEDITEMDIV",id=KeyId.UUID)
	private String pkBedItemDiv;
	
	@Field(value="DICT_PSNLEVEL")
	private String dictPsnlevel;
	
	@Field(value="NAME_LEVEL")
	private String nameLevel;
	
	@Field(value="PRICE_MAX")
	private Double priceMax;
	
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
	
	private List<InsGzgyHpDiv> bedHpDiv = new ArrayList<InsGzgyHpDiv>();

	public List<InsGzgyHpDiv> getBedHpDiv() {
		return bedHpDiv;
	}

	public void setBedHpDiv(List<InsGzgyHpDiv> bedHpDiv) {
		this.bedHpDiv = bedHpDiv;
	}

	public String getPkBedItemDiv() {
		return pkBedItemDiv;
	}

	public void setPkBedItemDiv(String pkBedItemDiv) {
		this.pkBedItemDiv = pkBedItemDiv;
	}

	public String getDictPsnlevel() {
		return dictPsnlevel;
	}

	public void setDictPsnlevel(String dictPsnlevel) {
		this.dictPsnlevel = dictPsnlevel;
	}

	public String getNameLevel() {
		return nameLevel;
	}

	public void setNameLevel(String nameLevel) {
		this.nameLevel = nameLevel;
	}

	public Double getPriceMax() {
		return priceMax;
	}

	public void setPriceMax(Double priceMax) {
		this.priceMax = priceMax;
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
