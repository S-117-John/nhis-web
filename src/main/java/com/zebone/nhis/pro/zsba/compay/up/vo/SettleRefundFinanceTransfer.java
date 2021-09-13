package com.zebone.nhis.pro.zsba.compay.up.vo;

import java.math.BigDecimal;
import java.util.Date;

import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Field.FieldType;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;


/**
 * 结算多余预交金退费-财务转账退费记录
 * @author lipz
 *
 */
@Table(value = "PAY_SETTLE_REFUND_FINANCE_TRANSFER")
public class SettleRefundFinanceTransfer {

	@PK
	@Field(value = "PK_SETTLE_REFUND_FT", id = KeyId.UUID)
	private String pkSettleRefundFt;//主键
	
	@Field(value = "PK_SETTLE_REFUND_TF")
	private String pkSettleRefundTf;//转财务退主键

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
	
	@Field(value = "OUT_AMOUNT")
	private BigDecimal outAmount;//转出金额
	
	@Field(value = "OUT_BANK_CARD_NO")
	private String outBankCardNo;//转出银行卡号码
	
	@Field(value = "OUT_BANK_NAME")
	private String outBankName;//转出银行
	
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

	public String getPkSettleRefundFt() {
		return pkSettleRefundFt;
	}

	public void setPkSettleRefundFt(String pkSettleRefundFt) {
		this.pkSettleRefundFt = pkSettleRefundFt;
	}

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

	public BigDecimal getOutAmount() {
		return outAmount;
	}

	public void setOutAmount(BigDecimal outAmount) {
		this.outAmount = outAmount;
	}

	public String getOutBankCardNo() {
		return outBankCardNo;
	}

	public void setOutBankCardNo(String outBankCardNo) {
		this.outBankCardNo = outBankCardNo;
	}

	public String getOutBankName() {
		return outBankName;
	}

	public void setOutBankName(String outBankName) {
		this.outBankName = outBankName;
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
