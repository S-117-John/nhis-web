package com.zebone.nhis.pro.zsba.compay.up.vo;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;

import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;

/**
 * 第三方微信支付对账单
 * @author songs
 * @date 2017-7-19
 */
@Table(value = "PAY_WECHAT_BILL")
public class PayWechatBill {

	@PK
	@Field(value = "PK_PAY_WECHAT_BILL", id = KeyId.UUID)
	private String pkPayWechatBill;//微信对账单主键
	
	@Field(value = "BILL_BACKAGE_PK")
	private String billBackagePk;//账单包主键

	@Field(value = "TIME_END")
	private String timeEnd;//交易时间

	@Field(value = "APPID")
	private String appid;//公众账号ID

	@Field(value = "MCH_ID")
	private String mchId;//商户号

	@Field(value = "SUB_MCH_ID")
	private String subMchId;//子商户号

	@Field(value = "DEVICE_INFO")
	private String deviceInfo;//设备号

	@Field(value = "TRANSACTION_ID")
	private String transactionId;//微信订单号

	@Field(value = "OUT_TRADE_NO")
	private String outTradeNo;//商户订单号

	@Field(value = "OPENID")
	private String openid;//用户标识

	@Field(value = "TRADE_TYPE")
	private String tradeType;//交易类型

	@Field(value = "TRADE_STATE")
	private String tradeState;//交易状态

	@Field(value = "BANK_TYPE")
	private String bankType;//付款银行

	@Field(value = "FEE_TYPE")
	private String feeType;//货币种类

	@Field(value = "TOTAL_FEE")
	private BigDecimal totalFee;//总金额

	@Field(value = "COUPON_FEE")
	private BigDecimal couponFee;//代金券

	@Field(value = "REFUND_ID")
	private String refundId;//微信退款单号

	@Field(value = "OUT_REFUND_NO")
	private String outRefundNo;//商户退款单号

	@Field(value = "REFUND_FEE")
	private BigDecimal refundFee;//退款金额

	@Field(value = "COUPON_REFUND_FEE")
	private BigDecimal couponRefundFee;//代金券退款金额

	@Field(value = "REFUND_CHANNEL")
	private String refundChannel;//退款类型

	@Field(value = "REFUND_STATUS")
	private String refundStatus;//退款状态
	
	@Field(value = "BODY")
	private String body;//商品名称

	@Field(value = "OUT_DATA_PACKAGE")
	private String outDataPackage;//商户数据包

	@Field(value = "SX_FEE")
	private BigDecimal sxFee;//手续费

	@Field(value = "RATE")
	private String rate;//费率

	@Field(value = "GENERATE_TIME")
	private Date generateTime;//记录生成时间
	
	@Field(value = "BILL_STATUS")
	private String billStatus;//对账状态：0未对账、1对账成功、2金额不一致、3单边账
	
	@Field(value = "BILL_TIME")
	private Date billTime;//对账时间
	
	@Field(value = "BILL_DESC")
	private String billDesc;//对账描述

	public PayWechatBill(){
		
	}

	public PayWechatBill(String billBackagePk, String[] obj){
		this.billBackagePk = billBackagePk;
		this.timeEnd=obj[0];
		this.appid=obj[1];
		this.mchId=obj[2];
		this.subMchId=obj[3];
		this.deviceInfo=obj[4];
		this.transactionId=obj[5];
		this.outTradeNo=obj[6];
		this.openid=obj[7];
		this.tradeType=obj[8];
		this.tradeState=obj[9];
		this.bankType=obj[10];
		this.feeType=obj[11];
		this.totalFee=new BigDecimal(obj[12]);
		this.couponFee=new BigDecimal(obj[13]);
		this.refundId=obj[14];
		this.outRefundNo=obj[15];
		this.refundFee=new BigDecimal(StringUtils.isNotEmpty(obj[16])?obj[16]:"0").multiply(new BigDecimal(-1));
		this.couponRefundFee=new BigDecimal(obj[17]);
		this.refundChannel=obj[18];
		this.refundStatus=obj[19];
		this.body=obj[20];
		this.outDataPackage=obj[21];
		this.sxFee=new BigDecimal(obj[22]);
		this.rate=obj[23];
		this.generateTime=new Date();
		this.billStatus = "0";
		this.billTime = null;
		this.billDesc = null;
	}
	
	public String getPkPayWechatBill() {
		return pkPayWechatBill;
	}

	public void setPkPayWechatBill(String pkPayWechatBill) {
		this.pkPayWechatBill = pkPayWechatBill;
	}

	public String getTimeEnd() {
		return timeEnd;
	}

	public void setTimeEnd(String timeEnd) {
		this.timeEnd = timeEnd;
	}

	public String getAppid() {
		return appid;
	}

	public void setAppid(String appid) {
		this.appid = appid;
	}

	public String getMchId() {
		return mchId;
	}

	public void setMchId(String mchId) {
		this.mchId = mchId;
	}

	public String getSubMchId() {
		return subMchId;
	}

	public void setSubMchId(String subMchId) {
		this.subMchId = subMchId;
	}

	public String getDeviceInfo() {
		return deviceInfo;
	}

	public void setDeviceInfo(String deviceInfo) {
		this.deviceInfo = deviceInfo;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public String getOutTradeNo() {
		return outTradeNo;
	}

	public void setOutTradeNo(String outTradeNo) {
		this.outTradeNo = outTradeNo;
	}

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	public String getTradeType() {
		return tradeType;
	}

	public void setTradeType(String tradeType) {
		this.tradeType = tradeType;
	}

	public String getTradeState() {
		return tradeState;
	}

	public void setTradeState(String tradeState) {
		this.tradeState = tradeState;
	}

	public String getBankType() {
		return bankType;
	}

	public void setBankType(String bankType) {
		this.bankType = bankType;
	}

	public String getFeeType() {
		return feeType;
	}

	public void setFeeType(String feeType) {
		this.feeType = feeType;
	}

	public BigDecimal getTotalFee() {
		return totalFee;
	}

	public void setTotalFee(BigDecimal totalFee) {
		this.totalFee = totalFee;
	}

	public BigDecimal getCouponFee() {
		return couponFee;
	}

	public void setCouponFee(BigDecimal couponFee) {
		this.couponFee = couponFee;
	}

	public String getRefundId() {
		return refundId;
	}

	public void setRefundId(String refundId) {
		this.refundId = refundId;
	}

	public String getOutRefundNo() {
		return outRefundNo;
	}

	public void setOutRefundNo(String outRefundNo) {
		this.outRefundNo = outRefundNo;
	}

	public BigDecimal getRefundFee() {
		return refundFee;
	}

	public void setRefundFee(BigDecimal refundFee) {
		this.refundFee = refundFee;
	}

	public BigDecimal getCouponRefundFee() {
		return couponRefundFee;
	}

	public void setCouponRefundFee(BigDecimal couponRefundFee) {
		this.couponRefundFee = couponRefundFee;
	}

	public String getRefundChannel() {
		return refundChannel;
	}

	public void setRefundChannel(String refundChannel) {
		this.refundChannel = refundChannel;
	}

	public String getRefundStatus() {
		return refundStatus;
	}

	public void setRefundStatus(String refundStatus) {
		this.refundStatus = refundStatus;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getOutDataPackage() {
		return outDataPackage;
	}

	public void setOutDataPackage(String outDataPackage) {
		this.outDataPackage = outDataPackage;
	}

	public BigDecimal getSxFee() {
		return sxFee;
	}

	public void setSxFee(BigDecimal sxFee) {
		this.sxFee = sxFee;
	}

	public String getRate() {
		return rate;
	}

	public void setRate(String rate) {
		this.rate = rate;
	}

	public Date getGenerateTime() {
		return generateTime;
	}

	public void setGenerateTime(Date generateTime) {
		this.generateTime = generateTime;
	}



	public String getBillStatus() {
		return billStatus;
	}

	public void setBillStatus(String billStatus) {
		this.billStatus = billStatus;
	}

	public String getBillDesc() {
		return billDesc;
	}

	public void setBillDesc(String billDesc) {
		this.billDesc = billDesc;
	}

	public Date getBillTime() {
		return billTime;
	}

	public void setBillTime(Date billTime) {
		this.billTime = billTime;
	}

	public String getBillBackagePk() {
		return billBackagePk;
	}

	public void setBillBackagePk(String billBackagePk) {
		this.billBackagePk = billBackagePk;
	}

	
}