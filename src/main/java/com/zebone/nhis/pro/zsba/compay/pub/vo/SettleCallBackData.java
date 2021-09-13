package com.zebone.nhis.pro.zsba.compay.pub.vo;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * 电子结算回流入参Data
 * @author Administrator
 *
 */
public class SettleCallBackData {

	@JSONField(ordinal = 1)
	private String orgId;//医保定点机构商户代码  传POS的商户号
	@JSONField(ordinal = 2)
	private String orgName;  //医保定点机构商户名称 POS的商户名称
	@JSONField(ordinal = 3)
	private String orgType;  //机构类别 见字典定义
	@JSONField(ordinal = 4)
	private String medType; //诊疗类别 见字典定义
	@JSONField(ordinal = 5)
	private String cityCode;  //城市编码 采用国家城市行政编码 (442000)
	@JSONField(ordinal = 6)
	private String ecToken; //电子凭证授权TOKEN   验码后的token
	@JSONField(ordinal = 7)
	private String idNo;  //证件号码
	@JSONField(ordinal = 8)
	private String idType;  //证件类别
	@JSONField(ordinal = 9)
	private String userName;  //姓名
	@JSONField(ordinal = 10)
	private String clientId; //扫码墩或相关终端设备编号 改为必填项，传POS机的终端号
	@JSONField(ordinal = 11)
	private String status; //状态 SUCC：成功 FAIL：失败
	@JSONField(ordinal = 12)
	private String message; //信息 如失败原因
	@JSONField(ordinal = 13)
	private String optType; //操作类型 SETTLE:结算 REFUND：退款
	@JSONField(ordinal = 14)
	private String memo;  //订单备注
	@JSONField(ordinal = 15)
	private String feeSumAmt; //总金额 总金额=个人账户支出+现金支付 +基金支出 
	@JSONField(ordinal = 16)
	private String psnAcctPay; //个人账户支出
	@JSONField(ordinal = 17)
	private String ownPayAmt; //现金支付
	@JSONField(ordinal = 18)
	private String fundPay; //基金支出
	@JSONField(ordinal = 19)
	private String insuranceAmount; //总记账金额 暂时为0
	@JSONField(ordinal = 20)
	private String accountBlance;  //账户余额
	@JSONField(ordinal = 21)
	private String payType;  //消费类别  可不传
	@JSONField(ordinal = 22)
	private String bizTraceNo;  //业务流水号 不能重复 pos机的参考号加凭账号
	@JSONField(ordinal = 23)
	private String oriBizTraceNo; //原交易流水号 当optType= REFUND 时，传原结算推送出来的bizTraceNo，并且不可为空 
	@JSONField(ordinal = 24)
	private String bizTraceTime;  //业务时间  yyyyMMddHHmmss
	@JSONField(ordinal = 25)
	private SettleCallBackExtra extra; //扩展参数  JSON 对象 
	public String getOrgId() {
		return orgId;
	}
	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	public String getOrgName() {
		return orgName;
	}
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	public String getOrgType() {
		return orgType;
	}
	public void setOrgType(String orgType) {
		this.orgType = orgType;
	}
	public String getMedType() {
		return medType;
	}
	public void setMedType(String medType) {
		this.medType = medType;
	}
	public String getCityCode() {
		return cityCode;
	}
	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}
	public String getEcToken() {
		return ecToken;
	}
	public void setEcToken(String ecToken) {
		this.ecToken = ecToken;
	}
	public String getIdNo() {
		return idNo;
	}
	public void setIdNo(String idNo) {
		this.idNo = idNo;
	}
	public String getIdType() {
		return idType;
	}
	public void setIdType(String idType) {
		this.idType = idType;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getClientId() {
		return clientId;
	}
	public void setClientId(String clientId) {
		this.clientId = clientId;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getOptType() {
		return optType;
	}
	public void setOptType(String optType) {
		this.optType = optType;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getFeeSumAmt() {
		return feeSumAmt;
	}
	public void setFeeSumAmt(String feeSumAmt) {
		this.feeSumAmt = feeSumAmt;
	}
	public String getPsnAcctPay() {
		return psnAcctPay;
	}
	public void setPsnAcctPay(String psnAcctPay) {
		this.psnAcctPay = psnAcctPay;
	}
	public String getOwnPayAmt() {
		return ownPayAmt;
	}
	public void setOwnPayAmt(String ownPayAmt) {
		this.ownPayAmt = ownPayAmt;
	}
	public String getFundPay() {
		return fundPay;
	}
	public void setFundPay(String fundPay) {
		this.fundPay = fundPay;
	}
	public String getInsuranceAmount() {
		return insuranceAmount;
	}
	public void setInsuranceAmount(String insuranceAmount) {
		this.insuranceAmount = insuranceAmount;
	}
	public String getAccountBlance() {
		return accountBlance;
	}
	public void setAccountBlance(String accountBlance) {
		this.accountBlance = accountBlance;
	}
	public String getPayType() {
		return payType;
	}
	public void setPayType(String payType) {
		this.payType = payType;
	}
	public String getBizTraceNo() {
		return bizTraceNo;
	}
	public void setBizTraceNo(String bizTraceNo) {
		this.bizTraceNo = bizTraceNo;
	}
	public String getOriBizTraceNo() {
		return oriBizTraceNo;
	}
	public void setOriBizTraceNo(String oriBizTraceNo) {
		this.oriBizTraceNo = oriBizTraceNo;
	}
	public String getBizTraceTime() {
		return bizTraceTime;
	}
	public void setBizTraceTime(String bizTraceTime) {
		this.bizTraceTime = bizTraceTime;
	}
	public SettleCallBackExtra getExtra() {
		return extra;
	}
	public void setExtra(SettleCallBackExtra extra) {
		this.extra = extra;
	}
	
	
}
