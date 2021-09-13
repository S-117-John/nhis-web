package com.zebone.nhis.pro.zsba.mz.pub.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Field.FieldType;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;

@Table(value = "BL_OTHER_PAY")
public class BlOtherPay implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * PK_OTHERPAY - 支付接口主键
	 */
	@PK
	@Field(value = "PK_OTHERPAY", id = KeyId.UUID)
	private String pkOtherPay;

	/**
	 * 机构主键
	 */
	@Field(value = "PK_ORG", userfield = "pkOrg", userfieldscop = FieldType.INSERT)
	private String pkOrg;

	/**
	 * 支付金额
	 */
	@Field(value = "AMOUNT")
	private BigDecimal amount;

	/**
	 * 支付方式
	 */
	@Field(value = "EU_PAYTYPE")
	private String euPaytype;
	
	/**
	 * 描述
     */
    @Field(value = "DESC_PAY")
    private String descPay;

	/**
	 * 关联银行
	 */
	@Field(value = "DT_BANK")
	private String dtBank;

	/**
	 * 银行名称
	 */
	@Field(value = "NAME_BANK")
	private String nameBank;

	/**
	 * 支付标志
	 */
	@Field(value = "FLAG_PAY")
	private String flagPay;

	/**
	 * 支付时间
	 */
	@Field(value = "DATE_PAY")
	private Date datePay;

	/**
	 * 交易编码
	 */
	@Field(value = "TRADE_NO")
	private String tradeNo;

	/**
	 * 系统交易流水号(商户订单号)
	 */
	@Field(value = "SERIAL_NO")
	private String serialNo;

	/**
	 * 外部系统名称
	 */
	@Field(value = "SYSNAME")
	private String sysname;

	/**
	 * 退款单号
	 */
	@Field(value = "REFUND_NO")
	private String refundNo;

	/**
	 * 退款支付时间
	 */
	@Field(value = "DATE_REFUND")
	private Date dateRefund;

	/**
	 * 创建人
	 */
	@Field(userfield = "pkEmp", userfieldscop = FieldType.INSERT)
	private String creator;

	/**
	 * 创建时间
	 */
	@Field(value = "CREATE_TIME", date = FieldType.INSERT)
	private Date createTime;

	/**
	 * 修改人
	 */
	@Field(userfield = "pkEmp", userfieldscop = FieldType.ALL)
	private String modifier;

	/**
	 * 修改时间
	 */
	@Field(value = "MODITY_TIME", date = FieldType.INSERT)
	private Date modityTime;

	/**
	 * 时间戳
	 */
	@Field(value = "TS", date = FieldType.ALL)
	private Date ts;

	/**
	 * 删除标志
	 */
	@Field(value = "DEL_FLAG")
	private String delFlag = "0"; // 0未删除 1：删除

	/**
	 * 银行卡号
	 */
	@Field(value = "CARD_NO")
	private String cardNo;

	/**
	 * 收费项目代码
	 */
	@Field(value = "CHARGE_CODE")
	private String chargeCode;

	/**
	 * 收费项目名称
	 */
	@Field(value = "CHARGE_NAME")
	private String chargeName;

	/**
	 *单价
	 */
	@Field(value = "PRICE")
	private BigDecimal price;

	/**
	 * 数量
	 */
	@Field(value = "QUAN")
	private Integer quan;
	
	/**
	 * 重结主键
	 */
	@Field(value = "PK_OTHERPAY_RECHARGE")
	private String pkOtherpayRecharge;

	public String getPkOtherPay() {
		return pkOtherPay;
	}

	public void setPkOtherPay(String pkOtherPay) {
		this.pkOtherPay = pkOtherPay;
	}

	public String getPkOrg() {
		return pkOrg;
	}

	public void setPkOrg(String pkOrg) {
		this.pkOrg = pkOrg;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public String getEuPaytype() {
		return euPaytype;
	}

	public void setEuPaytype(String euPaytype) {
		this.euPaytype = euPaytype;
	}

	public String getDtBank() {
		return dtBank;
	}

	public void setDtBank(String dtBank) {
		this.dtBank = dtBank;
	}

	public String getNameBank() {
		return nameBank;
	}

	public void setNameBank(String nameBank) {
		this.nameBank = nameBank;
	}

	public String getFlagPay() {
		return flagPay;
	}

	public void setFlagPay(String flagPay) {
		this.flagPay = flagPay;
	}

	public Date getDatePay() {
		return datePay;
	}

	public void setDatePay(Date datePay) {
		this.datePay = datePay;
	}

	public String getTradeNo() {
		return tradeNo;
	}

	public void setTradeNo(String tradeNo) {
		this.tradeNo = tradeNo;
	}

	public String getSerialNo() {
		return serialNo;
	}

	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}

	public String getSysname() {
		return sysname;
	}

	public void setSysname(String sysname) {
		this.sysname = sysname;
	}

	public String getRefundNo() {
		return refundNo;
	}

	public void setRefundNo(String refundNo) {
		this.refundNo = refundNo;
	}

	public Date getDateRefund() {
		return dateRefund;
	}

	public void setDateRefund(Date dateRefund) {
		this.dateRefund = dateRefund;
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

	public Date getTs() {
		return ts;
	}

	public void setTs(Date ts) {
		this.ts = ts;
	}

	public String getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}

	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	public String getChargeCode() {
		return chargeCode;
	}

	public void setChargeCode(String chargeCode) {
		this.chargeCode = chargeCode;
	}

	public String getChargeName() {
		return chargeName;
	}

	public void setChargeName(String chargeName) {
		this.chargeName = chargeName;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public Integer getQuan() {
		return quan;
	}

	public void setQuan(Integer quan) {
		this.quan = quan;
	}

	public String getDescPay() {
		return descPay;
	}

	public void setDescPay(String descPay) {
		this.descPay = descPay;
	}

	public String getPkOtherpayRecharge() {
		return pkOtherpayRecharge;
	}

	public void setPkOtherpayRecharge(String pkOtherpayRecharge) {
		this.pkOtherpayRecharge = pkOtherpayRecharge;
	}
}