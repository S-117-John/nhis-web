package com.zebone.nhis.webservice.syx.vo.platForm;

import java.util.Date;

import com.zebone.platform.modules.dao.build.au.Field;

public class OnlineBlopdtVo {
	/** DOSAGE - 剂型名称 */
	@Field(value = "DTDOSAGE")
	private String dtdoSage;

	/** FLAG_FIT - 医嘱限制用药 */
	@Field(value = "FLAG_FIT")
	private String flagfit;

	/** ITEM_NAME - 项目名称 */
	@Field(value = "ITEM_NAME")
	private String itemName;

	/** ITEMCODE -上传编码 */
	@Field(value = "ITEM_CODE")
	private String itemCode;

	/** 收费项目、药品编码 */
	@Field(value = "CODE")
	private String code;

	@Field(value="PK_CGOP")
    private String pkCgop;
	
	/** PRICE - 当前单价 对物品来说：取物品当前包装单位下单价 */
	@Field(value = "PRICE")
	private Double price;

	/** AMOUNT - 金额 */
	@Field(value = "AMOUNT")
	private Double amount;

	/** AMOUNT_PI - 金额_患者自费 */
	@Field(value = "AMOUNT_PI")
	private Double amountPi;

	/** AMOUNT_HPPI - 医保金额_患者支付 */
	@Field(value = "AMOUNT_HPPI")
	private Double amountHppi;

	/** QUAN - 数量 对物品来说：取物品当前包装单位下单价 */
	@Field(value = "QUAN")
	private Double quan;

	/** 加收比例 */
	@Field(value = "RATIO_ADD")
	private Double ratioAdd;

	/** 加收金额 */
	@Field(value = "AMOUNT_ADD")
	private Double amountAdd;

	/** DATE_HAP - 费用发生日期 */
	@Field(value = "DATE_HAP")
	private Date dateHap;

	/** DATE_CG - 记费日期 */
	@Field(value = "DATE_CG")
	private Date dateCg;

    /** SPEC - 规格 */
	@Field(value="SPEC")
    private String spec;
	
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

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
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

	public Double getQuan() {
		return quan;
	}

	public void setQuan(Double quan) {
		this.quan = quan;
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

	public String getSpec() {
		return spec;
	}

	public void setSpec(String spec) {
		this.spec = spec;
	}

	public String getPkCgop() {
		return pkCgop;
	}

	public void setPkCgop(String pkCgop) {
		this.pkCgop = pkCgop;
	}
}
