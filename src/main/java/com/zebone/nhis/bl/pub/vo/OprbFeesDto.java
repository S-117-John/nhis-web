package com.zebone.nhis.bl.pub.vo;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.zebone.nhis.common.module.bl.BlDeposit;
import com.zebone.nhis.common.module.bl.BlEmpInvoice;
import com.zebone.nhis.common.module.bl.BlInvoiceDt;
import com.zebone.nhis.common.module.bl.opcg.BlOpDt;
import com.zebone.nhis.common.module.pv.BlExtPayBankVo;

/**
 * 2017年3月7日17:21:09
 * @author gongxy
 * 
 */
public class OprbFeesDto {

	private String pkPi;
	
	private String pkPv;//就诊主键

	private String pkOrg;

	private Date dateStart;

	private Date dateEnd;

	private String codeInv;

	private String pkPress;

	private String pkCnords;
	
	private String pkCgops;

	private String isPres;

	private String pkSettle;
	
	private String pkSettleCanc;//取消结算主键

	private BigDecimal aggregateAmount;// 金额合计

	private BigDecimal patientsPay; // 患者自付金额

	private BigDecimal medicarePayments;// 医保支付

	private BigDecimal accountBalance;// 账户余额

	private BigDecimal accountPrepaid;// /账户已支付（医保病人使用一卡通已支付）

	private BigDecimal accountPay;// 账户支付

	private BigDecimal rebackAmount;// 应退金额

	private List<BlInvoiceDt> blInvoiceDts;// 发票明细

	private List<BlEmpInvoice> billInfo;// 票据领用信息
	
	private List<InvoiceInfo> invoiceInfo ; // 发票列表

	private List<BlDeposit> blDeposits;// 支付方式

	private String inVoiceNo;// 发票号

	private String pkEmpinvoice;// 票据领用主键

	private String pkInvcate; // 票据分类主键

	private List<BlOpDt> blOpDts;

	private List<BlExtPayBankVo> blExtPayBankList;  //若支付方式是银行卡，关联bl_ext_pay字段
	
	private String machineName;
	
	private String flagEtce;//附加项目标志
	
	private List<BlOpDt> blOpDtsNew;
	
	private String flagUntlFees;//医保单边账处理：如果是部分退，则不生成未退费部分的结算信息，需到门诊收费重新结算(灵璧用)
	
	private BigDecimal oldAmtRound;//上次结算现金短款
	
	private BigDecimal newAmtRound;//本次退款现金短款
	
	private String noPratRet;//是否按现金退

	private String flagPrepReback;//配药时是否可进行退药0：可退  1：不可退

	public String getFlagPrepReback() {
		return flagPrepReback;
	}

	public void setFlagPrepReback(String flagPrepReback) {
		this.flagPrepReback = flagPrepReback;
	}

	public BigDecimal getOldAmtRound() {
		return oldAmtRound;
	}

	public void setOldAmtRound(BigDecimal oldAmtRound) {
		this.oldAmtRound = oldAmtRound;
	}

	public BigDecimal getNewAmtRound() {
		return newAmtRound;
	}

	public void setNewAmtRound(BigDecimal newAmtRound) {
		this.newAmtRound = newAmtRound;
	}

	public String getFlagUntlFees() {
		return flagUntlFees;
	}

	public void setFlagUntlFees(String flagUntlFees) {
		this.flagUntlFees = flagUntlFees;
	}

	public List<BlOpDt> getBlOpDtsNew() {
		return blOpDtsNew;
	}

	public void setBlOpDtsNew(List<BlOpDt> blOpDtsNew) {
		this.blOpDtsNew = blOpDtsNew;
	}

	public String getPkCgops() {
		return pkCgops;
	}

	public void setPkCgops(String pkCgops) {
		this.pkCgops = pkCgops;
	}

	public String getFlagEtce() {
		return flagEtce;
	}

	public void setFlagEtce(String flagEtce) {
		this.flagEtce = flagEtce;
	}

	public String getMachineName() {
		return machineName;
	}

	public void setMachineName(String machineName) {
		this.machineName = machineName;
	}

	public String getPkPv() {
		return pkPv;
	}

	public void setPkPv(String pkPv) {
		this.pkPv = pkPv;
	}

	public List<BlExtPayBankVo> getBlExtPayBankList() {
		return blExtPayBankList;
	}

	public void setBlExtPayBankList(List<BlExtPayBankVo> blExtPayBankList) {
		this.blExtPayBankList = blExtPayBankList;
	}

	public String getPkPi() {

		return pkPi;
	}

	public void setPkPi(String pkPi) {

		this.pkPi = pkPi;
	}

	public Date getDateStart() {

		return dateStart;
	}

	public void setDateStart(Date dateStart) {

		this.dateStart = dateStart;
	}

	public Date getDateEnd() {

		return dateEnd;
	}

	public void setDateEnd(Date dateEnd) {

		this.dateEnd = dateEnd;
	}

	public String getCodeInv() {

		return codeInv;
	}

	public void setCodeInv(String codeInv) {

		this.codeInv = codeInv;
	}

	public String getPkPress() {

		return pkPress;
	}

	public void setPkPress(String pkPress) {

		this.pkPress = pkPress;
	}

	public String getPkCnords() {

		return pkCnords;
	}

	public void setPkCnords(String pkCnords) {

		this.pkCnords = pkCnords;
	}

	public String getIsPres() {

		return isPres;
	}

	public void setIsPres(String isPres) {

		this.isPres = isPres;
	}

	public String getPkOrg() {

		return pkOrg;
	}

	public void setPkOrg(String pkOrg) {

		this.pkOrg = pkOrg;
	}

	public List<BlOpDt> getBlOpDts() {

		return blOpDts;
	}

	public void setBlOpDts(List<BlOpDt> blOpDts) {

		this.blOpDts = blOpDts;
	}

	public List<BlEmpInvoice> getBillInfo() {

		return billInfo;
	}

	public void setBillInfo(List<BlEmpInvoice> billInfo) {

		this.billInfo = billInfo;
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

	public void setInVoiceNo(String inVoiceNo) {

		this.inVoiceNo = inVoiceNo;
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

	public List<BlInvoiceDt> getBlInvoiceDts() {

		return blInvoiceDts;
	}

	public void setBlInvoiceDts(List<BlInvoiceDt> blInvoiceDts) {

		this.blInvoiceDts = blInvoiceDts;
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

	public BigDecimal getRebackAmount() {
		return rebackAmount;
	}

	public void setRebackAmount(BigDecimal rebackAmount) {
		this.rebackAmount = rebackAmount;
	}

	public String getPkSettle() {

		return pkSettle;
	}

	public void setPkSettle(String pkSettle) {

		this.pkSettle = pkSettle;
	}

	public String getNoPratRet() {
		return noPratRet;
	}

	public void setNoPratRet(String noPratRet) {
		this.noPratRet = noPratRet;
	}

	public List<InvoiceInfo> getInvoiceInfo() {
		return invoiceInfo;
	}

	public void setInvoiceInfo(List<InvoiceInfo> invoiceInfo) {
		this.invoiceInfo = invoiceInfo;
	}

	public String getPkSettleCanc() {
		return pkSettleCanc;
	}

	public void setPkSettleCanc(String pkSettleCanc) {
		this.pkSettleCanc = pkSettleCanc;
	}
	
}
