package com.zebone.nhis.common.module.base.bd.price;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.zebone.nhis.common.module.base.bd.code.InsGzgyHpDiv;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Field.FieldType;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;

/**
 * Table:INS_GZGY_DIV_SPITEM
 * @author 
 *
 */
@Table(value="INS_GZGY_DIV_SPITEM")
public class InsGzgyDivSpitem {
	
	@PK
	@Field(value="PK_SPITEMDIV",id=KeyId.UUID)
	private String pkSpitemdiv;
	
	@Field(value="PK_ITEM")
	private String pkItem;
	
	@Field(value="AMOUNT_MAX")
	private Double amountMax;
	
	@Field(value="RATIO_INIT")
	private Double ratioInit;
	
	@Field(value="EU_CALCMODE")
	private String euCalcmode;
	
	@Field(value="RATIO")
	private Double ratio;
	
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
	
	@Field(value="RATIO_UNIT")
	private Double ratioUnit;
	
	private String nameItem;
	
	private List<InsGzgyHpDiv> spitemHpDiv = new ArrayList<InsGzgyHpDiv>();
	
	public Double getRatioUnit() {
		return ratioUnit;
	}

	public void setRatioUnit(Double ratioUnit) {
		this.ratioUnit = ratioUnit;
	}

	public String getNameItem() {
		return nameItem;
	}

	public void setNameItem(String nameItem) {
		this.nameItem = nameItem;
	}

	public List<InsGzgyHpDiv> getSpitemHpDiv() {
		return spitemHpDiv;
	}

	public void setSpitemHpDiv(List<InsGzgyHpDiv> spitemHpDiv) {
		this.spitemHpDiv = spitemHpDiv;
	}

	public String getPkSpitemdiv() {
		return pkSpitemdiv;
	}

	public void setPkSpitemdiv(String pkSpitemdiv) {
		this.pkSpitemdiv = pkSpitemdiv;
	}

	public String getPkItem() {
		return pkItem;
	}

	public void setPkItem(String pkItem) {
		this.pkItem = pkItem;
	}

	public Double getAmountMax() {
		return amountMax;
	}

	public void setAmountMax(Double amountMax) {
		this.amountMax = amountMax;
	}

	public Double getRatioInit() {
		return ratioInit;
	}

	public void setRatioInit(Double ratioInit) {
		this.ratioInit = ratioInit;
	}

	public String getEuCalcmode() {
		return euCalcmode;
	}

	public void setEuCalcmode(String euCalcmode) {
		this.euCalcmode = euCalcmode;
	}

	public Double getRatio() {
		return ratio;
	}

	public void setRatio(Double ratio) {
		this.ratio = ratio;
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
