package com.zebone.nhis.pro.zsba.compay.up.vo;

import java.beans.Transient;
import java.math.BigDecimal;
import java.util.Date;

import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.FieldType;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;

/**
 * 结算多余预交金退费-转财务退
 * @author lipz
 *
 */
@Table(value = "PAY_SETTLE_REFUND_TURN_FINANCE")
public class SettleRefundTurnFinance {

	@PK
	@Field(value = "PK_SETTLE_REFUND_TF", id = KeyId.UUID)
	private String pkSettleRefundTf;//主键

	@Field(value = "PK_ORG")
	private String pkOrg;//机构主键
	
	@Field(value="PK_PI")
    private String pkPi;// 患者主键

	@Field(value="PK_PV")
    private String pkPv;// 就诊主键
	
	@Field(value = "PK_SETTLE")
	private String pkSettle;// 结算主键
	
	@Field(value="PK_DEPO")
    private String pkDepo;// 关联就诊缴款
	
	@Field(value = "REFUND_AMOUNT")
	private BigDecimal refundAmount;//退款金额
	
	@Field(value = "REFUND_STATUS")
	private String refundStatus;//退款状态，0未退款，1已退款
	
	@Field(value = "IN_BANK_NAME")
	private String inBankName;//转入银行
	
	@Field(value = "IN_BANK_ADDRESS")
	private String inBankAddress;//转入银行地址
	
	@Field(value = "IN_BANK_CARD_NO")
	private String inBankCardNo;//转入银行卡号码
	
	@Field(value = "IN_BANK_USER_NAME")
	private String inBankUserName;//转入银行卡持卡人姓名
	
	@Field(value = "IN_USER_TEL_PHONE")
	private String inUserTelPhone;//转入持卡人联系电话
	
	@Field(value = "DEL_FLAG")
	private String delFlag;//删除标志，0正常，1删除
	
	@Field(value = "CREATOR")
	private String creator;//创建者
	
	@Field(value = "CREATE_TIME")
	private Date createTime;//创建时间
	
	@Field(value = "MODIFIER")
	private String modifier;//更新者
	
	@Field(value = "MODITY_TIME",date=FieldType.UPDATE)
	private Date modityTime;//更新时间


	private BigDecimal amount;//退款金额
	private String dtSttype;//结算类型
	
	public String getPkSettleRefundTf() {
		return pkSettleRefundTf;
	}

	public void setPkSettleRefundTf(String pkSettleRefundTf) {
		this.pkSettleRefundTf = pkSettleRefundTf;
	}

	public String getPkOrg() {
		return pkOrg;
	}

	public void setPkOrg(String pkOrg) {
		this.pkOrg = pkOrg;
	}

	public String getPkPi() {
		return pkPi;
	}

	public void setPkPi(String pkPi) {
		this.pkPi = pkPi;
	}

	public String getPkPv() {
		return pkPv;
	}

	public void setPkPv(String pkPv) {
		this.pkPv = pkPv;
	}

	public String getPkSettle() {
		return pkSettle;
	}

	public void setPkSettle(String pkSettle) {
		this.pkSettle = pkSettle;
	}

	public String getPkDepo() {
		return pkDepo;
	}

	public void setPkDepo(String pkDepo) {
		this.pkDepo = pkDepo;
	}

	public BigDecimal getRefundAmount() {
		return refundAmount;
	}

	public void setRefundAmount(BigDecimal refundAmount) {
		this.refundAmount = refundAmount;
	}

	public String getRefundStatus() {
		return refundStatus;
	}

	public void setRefundStatus(String refundStatus) {
		this.refundStatus = refundStatus;
	}

	public String getInBankName() {
		return inBankName;
	}

	public void setInBankName(String inBankName) {
		this.inBankName = inBankName;
	}

	public String getInBankAddress() {
		return inBankAddress;
	}

	public void setInBankAddress(String inBankAddress) {
		this.inBankAddress = inBankAddress;
	}

	public String getInBankCardNo() {
		return inBankCardNo;
	}

	public void setInBankCardNo(String inBankCardNo) {
		this.inBankCardNo = inBankCardNo;
	}

	public String getInBankUserName() {
		return inBankUserName;
	}

	public void setInBankUserName(String inBankUserName) {
		this.inBankUserName = inBankUserName;
	}

	public String getInUserTelPhone() {
		return inUserTelPhone;
	}

	public void setInUserTelPhone(String inUserTelPhone) {
		this.inUserTelPhone = inUserTelPhone;
	}

	public String getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
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

	@Transient
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public String getDtSttype() {
		return dtSttype;
	}

	public void setDtSttype(String dtSttype) {
		this.dtSttype = dtSttype;
	}
	
	
	
}
