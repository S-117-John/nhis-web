package com.zebone.nhis.webservice.vo.prepayvo;

import javax.xml.bind.annotation.XmlElement;

/**
 * 查询住院预交金充值记录 VO
 * @ClassName: ResPrePayVo   
 * @Description:TODO(这里用一句话描述这个类的作用)   
 * @author: zhangheng 
 * @date: 2019年4月20日 下午2:19:43     
 * @Copyright: 2019
 */
public class ResPrePayVo {
	// 交款金额
	private String amount = "";
	// 银行卡号
	private String bankNo = "";
	// 收款日期
	private String datePay= "";
	// 支付银行编码
	private String dtDank = "";
	// 支付方式
	private String dtPaymode = "";
	// 支付方向1收 -1退
	private String euDirect = "";
	// 结算标志
	private String flagSettle = "";
	// 支付银行名称
	private String nameBank = "";
	// 收款科室名称
	private String nameDept = "";
	// 收款员名称
	private String nameEmpPay = "";
	// 支付方式名称
	private String namePaymode = "";
	// 支付信息(交易号码、支票号等)
	private String payInfo = "";
	// 账户主键
	private String pkAcc = "";
	// 退款对应原收款主键
	private String pkDepoBack = "";
	// 收款科室主键
	private String pkDept = "";
	// 收款员主键
	private String pkEmpPay = "";
	// 结算主键
	private String pkSettle = "";
	// 收据号
	private String reptNo= "";
	// 序列号
	private String serialNo = "";
	// 支付系统名称
	private String sysname = "";
	// 交易号
	private String tradeNo = "";
	
	private String pkPv = "";
	
	private String codePv = "";
    
	@XmlElement(name = "pkPv")
	public String getPkPv() {
		return pkPv;
	}

	public void setPkPv(String pkPv) {
		this.pkPv = pkPv;
	}
	
	@XmlElement(name = "codePv")
	public String getCodePv() {
		return codePv;
	}

	public void setCodePv(String codePv) {
		this.codePv = codePv;
	}

	@XmlElement(name = "amount")
	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	@XmlElement(name = "bankNo")
	public String getBankNo() {
		return bankNo;
	}

	public void setBankNo(String bankNo) {
		this.bankNo = bankNo;
	}

	@XmlElement(name = "datePay")
	public String getDatePay() {
		return datePay;
	}

	public void setDatePay(String datePay) {
		this.datePay = datePay;
	}

	@XmlElement(name = "dtDank")
	public String getDtDank() {
		return dtDank;
	}

	public void setDtDank(String dtDank) {
		this.dtDank = dtDank;
	}

	@XmlElement(name = "dtPaymode")
	public String getDtPaymode() {
		return dtPaymode;
	}

	public void setDtPaymode(String dtPaymode) {
		this.dtPaymode = dtPaymode;
	}

	@XmlElement(name = "euDirect")
	public String getEuDirect() {
		return euDirect;
	}

	public void setEuDirect(String euDirect) {
		this.euDirect = euDirect;
	}

	@XmlElement(name = "flagSettle")
	public String getFlagSettle() {
		return flagSettle;
	}

	public void setFlagSettle(String flagSettle) {
		this.flagSettle = flagSettle;
	}

	@XmlElement(name = "nameBank")
	public String getNameBank() {
		return nameBank;
	}

	public void setNameBank(String nameBank) {
		this.nameBank = nameBank;
	}

	@XmlElement(name = "nameDept")
	public String getNameDept() {
		return nameDept;
	}

	public void setNameDept(String nameDept) {
		this.nameDept = nameDept;
	}

	@XmlElement(name = "nameEmpPay")
	public String getNameEmpPay() {
		return nameEmpPay;
	}

	public void setNameEmpPay(String nameEmpPay) {
		this.nameEmpPay = nameEmpPay;
	}

	@XmlElement(name = "namePaymode")
	public String getNamePaymode() {
		return namePaymode;
	}

	public void setNamePaymode(String namePaymode) {
		this.namePaymode = namePaymode;
	}

	@XmlElement(name = "payInfo")
	public String getPayInfo() {
		return payInfo;
	}

	public void setPayInfo(String payInfo) {
		this.payInfo = payInfo;
	}

	@XmlElement(name = "pkAcc")
	public String getPkAcc() {
		return pkAcc;
	}

	public void setPkAcc(String pkAcc) {
		this.pkAcc = pkAcc;
	}

	@XmlElement(name = "pkDepoBack")
	public String getPkDepoBack() {
		return pkDepoBack;
	}

	public void setPkDepoBack(String pkDepoBack) {
		this.pkDepoBack = pkDepoBack;
	}

	@XmlElement(name = "pkDept")
	public String getPkDept() {
		return pkDept;
	}

	public void setPkDept(String pkDept) {
		this.pkDept = pkDept;
	}

	@XmlElement(name = "pkEmpPay")
	public String getPkEmpPay() {
		return pkEmpPay;
	}

	public void setPkEmpPay(String pkEmpPay) {
		this.pkEmpPay = pkEmpPay;
	}

	@XmlElement(name = "pkSettle")
	public String getPkSettle() {
		return pkSettle;
	}

	public void setPkSettle(String pkSettle) {
		this.pkSettle = pkSettle;
	}

	@XmlElement(name = "reptNo")
	public String getReptNo() {
		return reptNo;
	}

	public void setReptNo(String reptNo) {
		this.reptNo = reptNo;
	}

	@XmlElement(name = "serialNo")
	public String getSerialNo() {
		return serialNo;
	}

	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}

	@XmlElement(name = "sysname")
	public String getSysname() {
		return sysname;
	}

	public void setSysname(String sysname) {
		this.sysname = sysname;
	}

	@XmlElement(name = "tradeNo")
	public String getTradeNo() {
		return tradeNo;
	}

	public void setTradeNo(String tradeNo) {
		this.tradeNo = tradeNo;
	}

}
