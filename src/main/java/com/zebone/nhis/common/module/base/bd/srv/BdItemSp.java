package com.zebone.nhis.common.module.base.bd.srv;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;


@Table(value="BD_ITEM_SP")
public class BdItemSp extends BaseModule{
	
	@PK
	@Field(value="PK_ITEMSP",id=KeyId.UUID)
	private String pkItemsp;
	
	@Field(value="PK_ITEM")
	private String pkItem;
	
	@Field(value="EU_PVTYPE")
	private String euPvtype;
	
	@Field(value="RATIO_CHILDREN")
	private Double ratioChildren;
	
	@Field(value="RATIO_SPEC")
	private Double ratioSpec;

	private String nameOrg;

	/**
	 * 儿童加收金额
	 */
	@Field(value="AMOUNT_CHILDREN")
	public double amountChildren;

	/**
	 * 特诊加收金额
	 */
	@Field(value="AMOUNT_SPEC")
	public double amountSpec;

	/**
	 * 儿童加收比例模式
	 * 0:比例 1：金额
	 */
	@Field(value="EU_CDMODE")
	public String euCdmode;

	/**
	 * 特诊加收比例模式
	 * 0:比例 1：金额
	 */
	@Field(value="EU_SPMODE")
	public String euSpmode;

	public double getAmountChildren() {
		return amountChildren;
	}

	public void setAmountChildren(double amountChildren) {
		this.amountChildren = amountChildren;
	}

	public double getAmountSpec() {
		return amountSpec;
	}

	public void setAmountSpec(double amountSpec) {
		this.amountSpec = amountSpec;
	}

	public String getEuCdmode() {
		return euCdmode;
	}

	public void setEuCdmode(String euCdmode) {
		this.euCdmode = euCdmode;
	}

	public String getEuSpmode() {
		return euSpmode;
	}

	public void setEuSpmode(String euSpmode) {
		this.euSpmode = euSpmode;
	}

	public String getEuPvtype() {
		return euPvtype;
	}

	public void setEuPvtype(String euPvtype) {
		this.euPvtype = euPvtype;
	}

	public String getNameOrg() {
		return nameOrg;
	}

	public void setNameOrg(String nameOrg) {
		this.nameOrg = nameOrg;
	}

	public String getPkItemsp() {
		return pkItemsp;
	}

	public void setPkItemsp(String pkItemsp) {
		this.pkItemsp = pkItemsp;
	}

	public String getPkItem() {
		return pkItem;
	}

	public void setPkItem(String pkItem) {
		this.pkItem = pkItem;
	}

	public Double getRatioChildren() {
		return ratioChildren;
	}

	public void setRatioChildren(Double ratioChildren) {
		this.ratioChildren = ratioChildren;
	}

	public Double getRatioSpec() {
		return ratioSpec;
	}

	public void setRatioSpec(Double ratioSpec) {
		this.ratioSpec = ratioSpec;
	}



	
}
