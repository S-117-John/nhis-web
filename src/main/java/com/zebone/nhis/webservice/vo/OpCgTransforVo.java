package com.zebone.nhis.webservice.vo;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.zebone.nhis.common.module.bl.BlDeposit;
import com.zebone.nhis.common.module.bl.BlEmpInvoice;
import com.zebone.nhis.common.module.bl.BlInvoiceDt;
import com.zebone.nhis.common.module.bl.opcg.BlOpDt;
import com.zebone.nhis.common.module.pay.BlExtPay;

/***
 * 预结算返回参数
 * @author Administrator
 * 
 */
public class OpCgTransforVo {

	private String pkPi; // 患者主键

	private String pkPv;// 患者就诊主键
	
	private String pkOrgSt;//结算机构--收款结算时使用
	
	private String codeEmpSt;//结算操作员员工号---收款结算时使用
	
	private String pkDeptSt;//结算科室 --收款结算时使用
	
	private String cardNo; //卡号
	
	private Date curDate; // 当前日期

	private List<BlOpDt> blOpDts; // 费用信息

	private String pkInsurance;// 患者主医保主键

	private BigDecimal aggregateAmount;// 金额合计

	private BigDecimal patientsPay;// 患者支付

	private BigDecimal medicarePayments;// 医保支付

	private String inVoiceNo;// 发票号

	private String pkEmpinvoice;// 票据领用主键

	private String pkInvcate; // 票据分类主键

	private List<BlDeposit> blDeposits;// 支付方式

	private BigDecimal accountBalance;// 账户余额

	private BigDecimal accountPrepaid;// 账户已付

	private BigDecimal accountPay;// 账户支付

	private BigDecimal accountReceivable;// 结算应收

	private List<Map<String, Object>> blInVoiceOtherInfo;

	private List<BlInvoiceDt> blInvoiceDts;// 发票明细
	
	private List<InvoiceInfo> invoiceInfo ; // 发票列表

	private List<BlEmpInvoice> billInfo;// 票据领用信息
	
	//外部医保传参
	private BigDecimal amtInsuThird;//医保总金额
	
	private BigDecimal ybzf; //医保总金额
	
	private BigDecimal xjzf; //医保总金额
	
	private String pkSettle; //结算主键
	
	private String flagCalinc;//门诊医生站退号标志
	
	private String pkDeptExec;//执行科室
	
	private boolean isSaoMaPay; //是否用扫码枪支付
	
	private List<BlExtPay> blExtPays; //第三方支付信息
	
	private String notDisplayFlagPv;//不显示诊查费
	
	private String isNotShowPv; //不显示诊查费
	
	private String invStatus;//灵璧自助机、第三方支付无发票信息状态
	
	private String note;//描述

	
	public List<BlExtPay> getBlExtPays() {
		return blExtPays;
	}

	public void setBlExtPays(List<BlExtPay> blExtPays) {
		this.blExtPays = blExtPays;
	}

	public String getPkDeptExec() {
		return pkDeptExec;
	}

	public void setPkDeptExec(String pkDeptExec) {
		this.pkDeptExec = pkDeptExec;
	}

	public String getFlagCalinc() {
		return flagCalinc;
	}

	public void setFlagCalinc(String flagCalinc) {
		this.flagCalinc = flagCalinc;
	}

	

	public String getPkPv() {

		return pkPv;
	}

	public void setPkPv(String pkPv) {

		this.pkPv = pkPv;
	}

	public List<InvoiceInfo> getInvoiceInfo() {
		return invoiceInfo;
	}

	public void setInvoiceInfo(List<InvoiceInfo> invoiceInfo) {
		this.invoiceInfo = invoiceInfo;
	}

	public BigDecimal getAggregateAmount() {
		return aggregateAmount;
	}

	public void setAggregateAmount(BigDecimal aggregateAmount) {
		this.aggregateAmount = aggregateAmount;
	}

	public BigDecimal getPatientsPay() {
		return patientsPay;
	}

	public void setPatientsPay(BigDecimal patientsPay) {
		this.patientsPay = patientsPay;
	}

	public BigDecimal getMedicarePayments() {
		return medicarePayments;
	}

	public void setMedicarePayments(BigDecimal medicarePayments) {
		this.medicarePayments = medicarePayments;
	}

	public List<BlDeposit> getBlDeposits() {

		return blDeposits;
	}

	public void setBlDeposits(List<BlDeposit> blDeposits) {

		this.blDeposits = blDeposits;
	}

	public String getInVoiceNo() {

		return inVoiceNo;
	}

	public String getPkEmpinvoice() {

		return pkEmpinvoice;
	}

	public void setPkEmpinvoice(String pkEmpinvoice) {

		this.pkEmpinvoice = pkEmpinvoice;
	}

	public String getPkInvcate() {

		return pkInvcate;
	}

	public void setPkInvcate(String pkInvcate) {

		this.pkInvcate = pkInvcate;
	}

	public void setInVoiceNo(String inVoiceNo) {

		this.inVoiceNo = inVoiceNo;
	}

	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	
	public String getPkPi() {

		return pkPi;
	}

	public void setPkPi(String pkPi) {

		this.pkPi = pkPi;
	}

	public boolean getIsSaoMaPay() {
		return isSaoMaPay;
	}

	public void setIsSaoMaPay(boolean isSaoMaPay) {
		this.isSaoMaPay = isSaoMaPay;
	}

	public Date getCurDate() {

		return curDate;
	}

	public void setCurDate(Date curDate) {

		this.curDate = curDate;
	}

	public List<BlOpDt> getBlOpDts() {

		return blOpDts;
	}

	public void setBlOpDts(List<BlOpDt> blOpDts) {

		this.blOpDts = blOpDts;
	}

	public String getPkInsurance() {

		return pkInsurance;
	}

	public void setPkInsurance(String pkInsurance) {

		this.pkInsurance = pkInsurance;
	}

	public List<BlInvoiceDt> getBlInvoiceDts() {

		return blInvoiceDts;
	}

	public void setBlInvoiceDts(List<BlInvoiceDt> blInvoiceDts) {

		this.blInvoiceDts = blInvoiceDts;
	}

	public List<BlEmpInvoice> getBillInfo() {

		return billInfo;
	}

	public void setBillInfo(List<BlEmpInvoice> billInfo) {

		this.billInfo = billInfo;
	}

	public List<Map<String, Object>> getBlInVoiceOtherInfo() {
		return blInVoiceOtherInfo;
	}

	public void setBlInVoiceOtherInfo(List<Map<String, Object>> blInVoiceOtherInfo) {
		this.blInVoiceOtherInfo = blInVoiceOtherInfo;
	}

	public BigDecimal getAccountBalance() {
		return accountBalance;
	}

	public void setAccountBalance(BigDecimal accountBalance) {
		this.accountBalance = accountBalance;
	}

	public BigDecimal getAccountPrepaid() {
		return accountPrepaid;
	}

	public void setAccountPrepaid(BigDecimal accountPrepaid) {
		this.accountPrepaid = accountPrepaid;
	}

	public BigDecimal getAccountPay() {
		return accountPay;
	}

	public void setAccountPay(BigDecimal accountPay) {
		this.accountPay = accountPay;
	}

	public BigDecimal getAccountReceivable() {
		return accountReceivable;
	}

	public void setAccountReceivable(BigDecimal accountReceivable) {
		this.accountReceivable = accountReceivable;
	}

	public BigDecimal getAmtInsuThird() {
		return amtInsuThird;
	}

	public void setAmtInsuThird(BigDecimal amtInsuThird) {
		this.amtInsuThird = amtInsuThird;
	}

	public void setSaoMaPay(boolean isSaoMaPay) {
		this.isSaoMaPay = isSaoMaPay;
	}

	public BigDecimal getYbzf() {
		return ybzf;
	}

	public void setYbzf(BigDecimal ybzf) {
		this.ybzf = ybzf;
	}

	public BigDecimal getXjzf() {
		return xjzf;
	}

	public void setXjzf(BigDecimal xjzf) {
		this.xjzf = xjzf;
	}

	public String getPkSettle() {
		return pkSettle;
	}

	public void setPkSettle(String pkSettle) {
		this.pkSettle = pkSettle;
	}

	public String getNotDisplayFlagPv() {
		return notDisplayFlagPv;
	}

	public void setNotDisplayFlagPv(String notDisplayFlagPv) {
		this.notDisplayFlagPv = notDisplayFlagPv;
	}

	public String getIsNotShowPv() {
		return isNotShowPv;
	}

	public void setIsNotShowPv(String isNotShowPv) {
		this.isNotShowPv = isNotShowPv;
	}

	public String getInvStatus() {
		return invStatus;
	}

	public void setInvStatus(String invStatus) {
		this.invStatus = invStatus;
	}

	public String getPkOrgSt() {
		return pkOrgSt;
	}

	public void setPkOrgSt(String pkOrgSt) {
		this.pkOrgSt = pkOrgSt;
	}

	public String getCodeEmpSt() {
		return codeEmpSt;
	}

	public void setCodeEmpSt(String codeEmpSt) {
		this.codeEmpSt = codeEmpSt;
	}

	public String getPkDeptSt() {
		return pkDeptSt;
	}

	public void setPkDeptSt(String pkDeptSt) {
		this.pkDeptSt = pkDeptSt;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}
	
}
