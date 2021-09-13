package com.zebone.nhis.bl.pub.vo;

import java.math.BigDecimal;
import java.util.List;

import com.zebone.nhis.common.module.bl.BlDeposit;
import com.zebone.nhis.common.module.bl.BlEmpInvoice;
import com.zebone.nhis.common.module.bl.BlInvoiceDt;
import com.zebone.nhis.common.module.bl.opcg.BlOpDt;
import com.zebone.nhis.common.module.pv.PvEncounter;

public class BlPubSettleVo {

	private String pkPi;
	
	private String pkPv;

	private PvEncounter pvVo;

	// 收费明细主键
	private String pkBlOpDtInSql;

	// 交款记录
	private List<BlDeposit> depositList;

	// 票据信息(针对多张发票)
	private List<BlEmpInvoice> billInfo;
	
	private List<InvoiceInfo> InvoiceInfo ; // 发票列表
	
	private List<BlInvoiceDt> blInvoiceDts;// 发票明细
	
	// 票据领用主键
	private String pkEmpinvoice;

	// 票据分类主键
	private String pkInvcate;

	// 发票号码
	private String codeInv;

	private List<BlOpDt> blOpDts;

	private String euPvType;
	
	private String isFlagClinic;

	private String pkSettle;//结算主键
	
	private String invStatus;//灵璧自助机、第三方支付无发票信息状态
	
	private String machineName;//本地计算机名称
	
	private BigDecimal amtRound;//舍入金额
	
	private String flagPrint;//是否打印纸质票据(电子票据使用)
	
	private BigDecimal amountDisc;//总额优惠金额
	
	public String getFlagPrint() {
		return flagPrint;
	}

	public void setFlagPrint(String flagPrint) {
		this.flagPrint = flagPrint;
	}

	public BigDecimal getAmtRound() {
		return amtRound;
	}

	public void setAmtRound(BigDecimal amtRound) {
		this.amtRound = amtRound;
	}

	public String getMachineName() {
		return machineName;
	}

	public void setMachineName(String machineName) {
		this.machineName = machineName;
	}

	public String getPkSettle() {
		return pkSettle;
	}

	public void setPkSettle(String pkSettle) {
		this.pkSettle = pkSettle;
	}

	public String getIsFlagClinic() {
		return isFlagClinic;
	}

	public void setIsFlagClinic(String isFlagClinic) {
		this.isFlagClinic = isFlagClinic;
	}

	public BlPubSettleVo() {

		super();
	}

	public List<BlOpDt> getBlOpDts() {

		return blOpDts;
	}

	public void setBlOpDts(List<BlOpDt> blOpDts) {

		this.blOpDts = blOpDts;
	}

	public String getPkInvcate() {

		return pkInvcate;
	}

	public void setPkInvcate(String pkInvcate) {

		this.pkInvcate = pkInvcate;
	}

	public String getCodeInv() {

		return codeInv;
	}

	public void setCodeInv(String codeInv) {

		this.codeInv = codeInv;
	}

	public List<InvoiceInfo> getInvoiceInfo() {
		return InvoiceInfo;
	}

	public void setInvoiceInfo(List<InvoiceInfo> invoiceInfo) {
		InvoiceInfo = invoiceInfo;
	}


	public String getPkPi() {

		return pkPi;
	}

	public void setPkPi(String pkPi) {

		this.pkPi = pkPi;
	}

	public List<BlDeposit> getDepositList() {

		return depositList;
	}

	public void setDepositList(List<BlDeposit> depositList) {

		this.depositList = depositList;
	}

	public String getPkEmpinvoice() {

		return pkEmpinvoice;
	}

	public void setPkEmpinvoice(String pkEmpinvoice) {

		this.pkEmpinvoice = pkEmpinvoice;
	}

	public PvEncounter getPvVo() {

		return pvVo;
	}

	public void setPvVo(PvEncounter pvVo) {

		this.pvVo = pvVo;
	}

	public String getEuPvType() {

		return euPvType;
	}

	public void setEuPvType(String euPvType) {

		this.euPvType = euPvType;
	}

	public String getPkBlOpDtInSql() {

		return pkBlOpDtInSql;
	}

	public void setPkBlOpDtInSql(String pkBlOpDtInSql) {

		this.pkBlOpDtInSql = pkBlOpDtInSql;
	}

	public List<BlEmpInvoice> getBillInfo() {

		return billInfo;
	}

	public void setBillInfo(List<BlEmpInvoice> billInfo) {

		this.billInfo = billInfo;
	}

	public List<BlInvoiceDt> getBlInvoiceDts() {
		return blInvoiceDts;
	}

	public void setBlInvoiceDts(List<BlInvoiceDt> blInvoiceDts) {
		this.blInvoiceDts = blInvoiceDts;
	}

	public String getPkPv() {
		return pkPv;
	}

	public void setPkPv(String pkPv) {
		this.pkPv = pkPv;
	}

	public String getInvStatus() {
		return invStatus;
	}

	public void setInvStatus(String invStatus) {
		this.invStatus = invStatus;
	}

	public BigDecimal getAmountDisc() {
		return amountDisc;
	}

	public void setAmountDisc(BigDecimal amountDisc) {
		this.amountDisc = amountDisc;
	}
	


}
