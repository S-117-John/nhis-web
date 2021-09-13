package com.zebone.nhis.compay.ins.syx.vo.gzyb;

import java.util.Date;

import com.zebone.platform.modules.dao.build.au.Field;

public class BlipdtReUploadVo {
	/** DOSAGE - 剂型名称 */
	@Field(value = "DTDOSAGE")
	private String dtdoSage;

	/** FLAG_FIT - */
	@Field(value = "FLAG_FIT")
	private String flagfit;

	/** FLAG_PDIND - */
	@Field(value = "FLAG_PDIND")
	private String flagpdind;

	/** ITEM_NAME - */
	@Field(value = "ITEM_NAME")
	private String itemName;

	/** ITEMCODE - */
	@Field(value = "ITEM_CODE")
	private String itemCode;

	/** PRICE - 当前单价 对物品来说：取物品当前包装单位下单价 */
	@Field(value = "PRICE")
	private Double price;

	/** QUAN - 数量 对物品来说：取物品当前包装单位下单价 */
	@Field(value = "QUAN")
	private Double quan;

	/** AMOUNT - 金额 */
	@Field(value = "AMOUNT")
	private Double amount;

	/** AMOUNT_PI - 金额_患者自费 */
	@Field(value = "AMOUNT_PI")
	private Double amountPi;

	/** AMOUNT_HPPI - 医保金额_患者支付 */
	@Field(value = "AMOUNT_HPPI")
	private Double amountHppi;
	/** 加收比例 */
	@Field(value = "RATIO_ADD")
	private Double ratioAdd;

	/** 加收金额 */
	@Field(value = "AMOUNT_ADD")
	private Double amountAdd;

	/** DATE_CG - 记费日期 */
	@Field(value = "DATE_CG")
	private Date dateCg;

	/** DATE_HAP - 费用发生日期 */
	@Field(value = "DATE_HAP")
	private Date dateHap;

	/** NAME_CG - 记费名称 */
	@Field(value = "NAME_CG")
	private String nameCg;

	/** SPEC - 规格 */
	@Field(value = "SPEC")
	private String spec;
	/** 项目编码 */
	@Field(value = "CODE")
	private String code;

	@Field(value = "FLAG_PD")
	private String flagpd;
	
	public String getFlagpd() {
		return flagpd;
	}

	public void setFlagpd(String flagpd) {
		this.flagpd = flagpd;
	}

	public String getDtdoSage() {
		return dtdoSage;
	}

	public void setDtdoSage(String dtdoSage) {
		this.dtdoSage = dtdoSage;
	}

	public String getFlagfit() {
		return flagfit;
	}

	public void setFlagfit(String flagfit) {
		this.flagfit = flagfit;
	}

	public String getFlagpdind() {
		return flagpdind;
	}

	public void setFlagpdind(String flagpdind) {
		this.flagpdind = flagpdind;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getItemCode() {
		return itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
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

	public Double getAmountPi() {
		return amountPi;
	}

	public void setAmountPi(Double amountPi) {
		this.amountPi = amountPi;
	}

	public Double getAmountHppi() {
		return amountHppi;
	}

	public void setAmountHppi(Double amountHppi) {
		this.amountHppi = amountHppi;
	}

	public Double getRatioAdd() {
		return ratioAdd;
	}

	public void setRatioAdd(Double ratioAdd) {
		this.ratioAdd = ratioAdd;
	}

	public Double getAmountAdd() {
		return amountAdd;
	}

	public void setAmountAdd(Double amountAdd) {
		this.amountAdd = amountAdd;
	}

	public Date getDateCg() {
		return dateCg;
	}

	public void setDateCg(Date dateCg) {
		this.dateCg = dateCg;
	}

	public Date getDateHap() {
		return dateHap;
	}

	public void setDateHap(Date dateHap) {
		this.dateHap = dateHap;
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

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

}
