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
 * Table:INS_GZGY_DIV_HVITEM
 * @author 
 *
 */
@Table(value="INS_GZGY_DIV_HVITEM")
public class InsGzgyDivHvitem {
	@PK
	@Field(value="PK_HVITEMDIV",id=KeyId.UUID)
	private String pkHvitemDiv;
	
	@Field(value="PRICE_MIN")
	private Double priceMin;
	
	@Field(value="PRICE_MAX")
	private Double priceMax;
	
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
	
	private Boolean IsAdd;	//标志是否是新增
	
	public Boolean getIsAdd() {
		return IsAdd;
	}

	public void setIsAdd(Boolean isAdd) {
		IsAdd = isAdd;
	}

	private List<InsGzgyHpDiv> hvItemHpDiv = new ArrayList<InsGzgyHpDiv>();
	
	public List<InsGzgyHpDiv> getHvItemHpDiv() {
		return hvItemHpDiv;
	}

	public void setHvItemHpDiv(List<InsGzgyHpDiv> hvItemHpDiv) {
		this.hvItemHpDiv = hvItemHpDiv;
	}

	public String getPkHvitemDiv() {
		return pkHvitemDiv;
	}

	public void setPkHvitemDiv(String pkHvitemDiv) {
		this.pkHvitemDiv = pkHvitemDiv;
	}

	public Double getPriceMin() {
		return priceMin;
	}

	public void setPriceMin(Double priceMin) {
		this.priceMin = priceMin;
	}

	public Double getPriceMax() {
		return priceMax;
	}

	public void setPriceMax(Double priceMax) {
		this.priceMax = priceMax;
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
