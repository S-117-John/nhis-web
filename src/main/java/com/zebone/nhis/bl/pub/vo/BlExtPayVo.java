package com.zebone.nhis.bl.pub.vo;

import java.util.Date;

/**
 * 第三方支付返回vo
 * @author Administrator
 *
 */
public class BlExtPayVo {

	private String pkExtpay; //支付接口主键
	private Double amount; //支付金额
	private String euPaytypeCode; //支付方式
	private String dtBank; //关联银行
	private String nameBank; //银行名称
	private String flagPay; //支付标志
	private Date datePay;  //支付时间
	private String tradeNo; //交易编码
	private String serialNo;  //订单号码
	private String pkPi;  //患者主键
	private String pkPv; //就诊主键
	private String pkDepo; //关联就诊缴款
	private String pkDepopi; //关联患者缴款
	private String resultPay; //支付回写内容
	private String refundNo; //退款单号
	private String euBill; //对账状态
	private String pkBill; // 对账单主键
	private String serialNum;//序号
	private String flagRefund; //退费标记
	private String creator;//创建人
	private String sysname;// 外部系统名称
	private String flagRefType;//业务标记
	
	public String getFlagRefType() {
		return flagRefType;
	}
	public void setFlagRefType(String flagRefType) {
		this.flagRefType = flagRefType;
	}
	public String getSysname() {
		return sysname;
	}
	public void setSysname(String sysname) {
		this.sysname = sysname;
	}
	public String getCreator() {
		return creator;
	}
	public void setCreator(String creator) {
		this.creator = creator;
	}
	public String getSerialNum() {
		return serialNum;
	}
	public void setSerialNum(String serialNum) {
		this.serialNum = serialNum;
	}
	public String getFlagRefund() {
		return flagRefund;
	}
	public void setFlagRefund(String flagRefund) {
		this.flagRefund = flagRefund;
	}
	public String getPkExtpay() {
		return pkExtpay;
	}
	public void setPkExtpay(String pkExtpay) {
		this.pkExtpay = pkExtpay;
	}
	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	public String getEuPaytypeCode() {
		return euPaytypeCode;
	}
	public void setEuPaytypeCode(String euPaytypeCode) {
		this.euPaytypeCode = euPaytypeCode;
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
	public String getPkDepo() {
		return pkDepo;
	}
	public void setPkDepo(String pkDepo) {
		this.pkDepo = pkDepo;
	}
	public String getPkDepopi() {
		return pkDepopi;
	}
	public void setPkDepopi(String pkDepopi) {
		this.pkDepopi = pkDepopi;
	}
	public String getResultPay() {
		return resultPay;
	}
	public void setResultPay(String resultPay) {
		this.resultPay = resultPay;
	}
	public String getRefundNo() {
		return refundNo;
	}
	public void setRefundNo(String refundNo) {
		this.refundNo = refundNo;
	}
	public String getEuBill() {
		return euBill;
	}
	public void setEuBill(String euBill) {
		this.euBill = euBill;
	}
	public String getPkBill() {
		return pkBill;
	}
	public void setPkBill(String pkBill) {
		this.pkBill = pkBill;
	}
	
}
