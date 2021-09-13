package com.zebone.nhis.pro.zsba.compay.up.vo;

import java.math.BigDecimal;
import java.util.Date;

import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.FieldType;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * 结算多余预交金退费记录表
 * @author lipz
 *
 */
@Table(value = "PAY_SETTLE_REFUND_RECORD")
public class SettleRefundRecord {
	
	@PK
	@Field(value = "PK_SETTLE_REFUND_RECORD", id = KeyId.UUID)
	private String pkSettleRefundRecord;//主键

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
	
	@Field(value = "ORDER_TYPE")
	private String orderType;// 订单类型 0原生微信、1原生支付宝、2银联、3现金、4衫德的微信、5衫德的支付宝、6衫德银行卡
	
	@Field(value = "PK_PAY_RECORD")
	private String pkPayRecord;//交易记录主键，orderType为3时该值为空
	
	@Field(value = "ORIGINAL_CARD_NO")
	private String originalCardNo;//原交易卡号
	
	@Field(value = "ORIGINAL_TRADE_NO")
	private String originalTradeNo;//原交易号
	
	@Field(value = "ORIGINAL_AMOUNT")
	private BigDecimal originalAmount;//原交易金额
	
	@Field(value = "REFUND_AMOUNT")
	private BigDecimal refundAmount;//退款金额
	
	@Field(value = "YT_AMOUNT")
	private BigDecimal ytAmount;//银联退款金额
	
	@Field(value = "REFUND_STATUS")
	private String refundStatus;//退款状态，0未退款，1已退款，2转财务记账退， 3转现金退
	
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

	public String getPkSettleRefundRecord() {
		return pkSettleRefundRecord;
	}

	public void setPkSettleRefundRecord(String pkSettleRefundRecord) {
		this.pkSettleRefundRecord = pkSettleRefundRecord;
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

	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	public String getPkPayRecord() {
		return pkPayRecord;
	}

	public void setPkPayRecord(String pkPayRecord) {
		this.pkPayRecord = pkPayRecord;
	}
	
	public String getOriginalCardNo() {
		return originalCardNo;
	}

	public void setOriginalCardNo(String originalCardNo) {
		this.originalCardNo = originalCardNo;
	}

	public String getOriginalTradeNo() {
		return originalTradeNo;
	}

	public void setOriginalTradeNo(String originalTradeNo) {
		this.originalTradeNo = originalTradeNo;
	}

	public BigDecimal getOriginalAmount() {
		return originalAmount;
	}

	public void setOriginalAmount(BigDecimal originalAmount) {
		this.originalAmount = originalAmount;
	}

	public BigDecimal getRefundAmount() {
		return refundAmount;
	}

	public void setRefundAmount(BigDecimal refundAmount) {
		this.refundAmount = refundAmount;
	}

	public BigDecimal getYtAmount() {
		return ytAmount;
	}

	public void setYtAmount(BigDecimal ytAmount) {
		this.ytAmount = ytAmount;
	}

	public String getRefundStatus() {
		return refundStatus;
	}

	public void setRefundStatus(String refundStatus) {
		this.refundStatus = refundStatus;
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
	
	

}
