package com.zebone.nhis.common.module.compay.ins.syx.gzyb;

import java.util.Date;

import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.FieldType;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;

/**
 * Table: INS_GZGY_BL - ins_gzgy_bl 
 *
 * @since 2016-10-11 09:06:42
 */
@Table(value="INS_GZGY_BL")
public class InsGzgyBl {
	
	@PK
	@Field(value="PK_GZGYBL",id=KeyId.UUID)
	private String pkGzgybl;
	
	@Field(value="PK_CNORD")
	private String pkCnord;
	
	@Field(value="PK_CG")
	private String pkCg;
	
	@Field(value="PK_PV")
	private String pkPv;
	
	@Field(value="EU_ITEMTYPE")
	private String euItemtype;
	
	@Field(value="PK_ITEM")
	private String pkItem;
	
	@Field(value="PK_ITEMCATE")
	private String pkItemcate;
	
	@Field(value="NAME_CG")
	private String nameCg;
	
	@Field(value="SPEC")
	private String spec;
	
	@Field(value="PRICE")
	private Double price;
	
	@Field(value="QUAN")
	private Double quan;
	
	@Field(value="AMOUNT")
	private Double amount;
	
	@Field(value="RATIO")
	private Double ratio;
	
	@Field(value="AMOUNT_PI")
	private Double amountPi;
	
	@Field(value="AMOUNT_HP")
	private Double amountHp;
	
	@Field(value="AMOUNT_UNIT")
	private Double amountUnit;
	
	@Field(value="DATE_HAP")
	private Date dateHap;
	
	@Field(value="DATE_CG")
	private Date dateCg;
	
	@Field(value="CREATE_TIME",date=FieldType.INSERT)
	private Date createTime;
	
	@Field(value="CREATOR",userfieldscop=FieldType.INSERT)
	private String creator;
	
	@Field(value="DEL_FLAG")
	private String delFlag = "0";
	
	@Field(date=FieldType.ALL)
	private Date ts;

	public String getPkGzgybl() {
		return pkGzgybl;
	}

	public void setPkGzgybl(String pkGzgybl) {
		this.pkGzgybl = pkGzgybl;
	}

	public String getPkCnord() {
		return pkCnord;
	}

	public void setPkCnord(String pkCnord) {
		this.pkCnord = pkCnord;
	}

	public String getPkCg() {
		return pkCg;
	}

	public void setPkCg(String pkCg) {
		this.pkCg = pkCg;
	}

	public String getPkPv() {
		return pkPv;
	}

	public void setPkPv(String pkPv) {
		this.pkPv = pkPv;
	}

	public String getEuItemtype() {
		return euItemtype;
	}

	public void setEuItemtype(String euItemtype) {
		this.euItemtype = euItemtype;
	}

	public String getPkItem() {
		return pkItem;
	}

	public void setPkItem(String pkItem) {
		this.pkItem = pkItem;
	}

	public String getPkItemcate() {
		return pkItemcate;
	}

	public void setPkItemcate(String pkItemcate) {
		this.pkItemcate = pkItemcate;
	}

	public String getNameCg() {
		return nameCg;
	}

	public void setNameCg(String nameCg) {
		this.nameCg = nameCg;
	}

	public String getSpec() {
		return spec;
	}

	public void setSpec(String spec) {
		this.spec = spec;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Double getQuan() {
		return quan;
	}

	public void setQuan(Double quan) {
		this.quan = quan;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public Double getRatio() {
		return ratio;
	}

	public void setRatio(Double ratio) {
		this.ratio = ratio;
	}

	public Double getAmountPi() {
		return amountPi;
	}

	public void setAmountPi(Double amountPi) {
		this.amountPi = amountPi;
	}

	public Double getAmountHp() {
		return amountHp;
	}

	public void setAmountHp(Double amountHp) {
		this.amountHp = amountHp;
	}

	public Double getAmountUnit() {
		return amountUnit;
	}

	public void setAmountUnit(Double amountUnit) {
		this.amountUnit = amountUnit;
	}

	public Date getDateHap() {
		return dateHap;
	}

	public void setDateHap(Date dateHap) {
		this.dateHap = dateHap;
	}

	public Date getDateCg() {
		return dateCg;
	}

	public void setDateCg(Date dateCg) {
		this.dateCg = dateCg;
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
