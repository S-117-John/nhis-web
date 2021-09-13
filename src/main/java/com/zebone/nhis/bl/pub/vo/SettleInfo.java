package com.zebone.nhis.bl.pub.vo;

import java.math.BigDecimal;
import java.util.List;

import com.zebone.nhis.common.module.bl.BlDeposit;
import com.zebone.nhis.common.module.compay.ins.zsba.ksyb.InsKsybStKsyb;
import com.zebone.nhis.common.module.compay.ins.zsba.ksyb.InsKsybStKsybJjfx;
import com.zebone.nhis.common.module.compay.ins.zsba.ydyb.InsYdybStSnyb;
import com.zebone.nhis.common.module.compay.ins.zsba.zsyb.InsZsybSt;
import com.zebone.nhis.common.module.compay.ins.zsba.zsyb.InsZsybStItemcate;
import com.zebone.nhis.common.module.pay.BlExtPay;

public class SettleInfo {

	private BigDecimal  amountPrep;//患者预交金合计
	private BigDecimal  amountSt;//患者费用合计
	private BigDecimal  amountPi;//患者自付合计
	private BigDecimal  amountInsu;//医保支付
	private BigDecimal  amountUnit;//单位支付
	
	public BigDecimal getAmountUnit() {
		return amountUnit;
	}
	public void setAmountUnit(BigDecimal amountUnit) {
		this.amountUnit = amountUnit;
	}
	public BigDecimal getAmountInsuThird() {
		return amountInsuThird;
	}
	public void setAmountInsuThird(BigDecimal amountInsuThird) {
		this.amountInsuThird = amountInsuThird;
	}
	private BigDecimal  amountInsuThird;//第三方医保接口返回的医保支付

	private BigDecimal  amountAcc;//患者账户支付
	private String pkAcc;
	
	public String getPkAcc() {
		return pkAcc;
	}
	public void setPkAcc(String pkAcc) {
		this.pkAcc = pkAcc;
	}
	public BigDecimal getAmountAcc() {
		return amountAcc;
	}
	public void setAmountAcc(BigDecimal amountAcc) {
		this.amountAcc = amountAcc;
	}
	private String pkPv;
	private String euStresult;
	private String dateBegin;//结算开始日期
	private String dateEnd;
	private String euSttype;	
	private List<InvInfoVo> invos;
	private  BlDeposit deposit;
	private List<BlDeposit> depoList;
	private  BlDeposit depositAcc;
	private  BlExtPay blExtPay;  //第三方支付交易数据
	private String pkInsSt; //医保结算主键
	private InsZsybSt insSt;//外部医保-中山医保住院结算
	private List<InsZsybStItemcate>insStItemcateList;//外部医保-中山医保结算返回项目分类
	private InsYdybStSnyb insStSnyb; //省内异地医保结算数据
	private InsKsybStKsyb insStKsyb; //跨省异地医保结算数据
	private List<InsKsybStKsybJjfx> jjfxList; //外部医保-跨省医保住院结算基金分项 
	private List<String> pkDepoList;//本次结算选择的预交金信息
	
    // 特殊项目结算明细主键
    private List<String> pkCgips;

    // 标志是否是特殊项目结算
    public String flagSpItemCg;
    
    //特诊是否合并打印发票
    public String flagHbPrint;
    
    public String pkSettle;//预结再结使用(灵璧)
    
    public String flagPrint;//是否打印纸质票据
    
    //舍入金额
    public BigDecimal amtRound;
    
    public List<BlExtPay> blExtPayList;
    
	public List<BlExtPay> getBlExtPayList() {
		return blExtPayList;
	}
	public void setBlExtPayList(List<BlExtPay> blExtPayList) {
		this.blExtPayList = blExtPayList;
	}
	public BigDecimal getAmtRound() {
		return amtRound;
	}
	public void setAmtRound(BigDecimal amtRound) {
		this.amtRound = amtRound;
	}
	public String getFlagPrint() {
		return flagPrint;
	}
	public void setFlagPrint(String flagPrint) {
		this.flagPrint = flagPrint;
	}
	public String getPkSettle() {
		return pkSettle;
	}
	public void setPkSettle(String pkSettle) {
		this.pkSettle = pkSettle;
	}
	public List<String> getPkDepoList() {
		return pkDepoList;
	}
	public void setPkDepoList(List<String> pkDepoList) {
		this.pkDepoList = pkDepoList;
	}
	public String getFlagHbPrint() {
		return flagHbPrint;
	}
	public void setFlagHbPrint(String flagHbPrint) {
		this.flagHbPrint = flagHbPrint;
	}
	public List<BlDeposit> getDepoList() {
		return depoList;
	}
	public void setDepoList(List<BlDeposit> depoList) {
		this.depoList = depoList;
	}
	public List<String> getPkCgips() {
		return pkCgips;
	}
	public void setPkCgips(List<String> pkCgips) {
		this.pkCgips = pkCgips;
	}
	public String getFlagSpItemCg() {
		return flagSpItemCg;
	}
	public void setFlagSpItemCg(String flagSpItemCg) {
		this.flagSpItemCg = flagSpItemCg;
	}
	public BlDeposit getDepositAcc() {
		return depositAcc;
	}
	public void setDepositAcc(BlDeposit depositAcc) {
		this.depositAcc = depositAcc;
	}
	public String getEuStresult() {
		return euStresult;
	}
	public void setEuStresult(String euStresult) {
		this.euStresult = euStresult;
	}
	
	public String getPkPv() {
		return pkPv;
	}
	public void setPkPv(String pkPv) {
		this.pkPv = pkPv;
	}
	public String getDateEnd() {
		return dateEnd;
	}
	public void setDateEnd(String dateEnd) {
		this.dateEnd = dateEnd;
	}
	public String getEuSttype() {
		return euSttype;
	}
	public void setEuSttype(String euSttype) {
		this.euSttype = euSttype;
	}
	public List<InvInfoVo> getInvos() {
		return invos;
	}
	public void setInvos(List<InvInfoVo> invos) {
		this.invos = invos;
	}
	public BlDeposit getDeposit() {
		return deposit;
	}
	public void setDeposit(BlDeposit deposit) {
		this.deposit = deposit;
	}
	public BigDecimal getAmountPrep() {
		return amountPrep;
	}
	public void setAmountPrep(BigDecimal amountPrep) {
		this.amountPrep = amountPrep;
	}
	public BigDecimal getAmountSt() {
		return amountSt;
	}
	public void setAmountSt(BigDecimal amountSt) {
		this.amountSt = amountSt;
	}
	public BigDecimal getAmountPi() {
		return amountPi;
	}
	public void setAmountPi(BigDecimal amountPi) {
		this.amountPi = amountPi;
	}
	public BigDecimal getAmountInsu() {
		return amountInsu;
	}
	public void setAmountInsu(BigDecimal amountInsu) {
		this.amountInsu = amountInsu;
	}
	public BlExtPay getBlExtPay() {
		return blExtPay;
	}
	public void setBlExtPay(BlExtPay blExtPay) {
		this.blExtPay = blExtPay;
	}
	public String getPkInsSt() {
		return pkInsSt;
	}
	public void setPkInsSt(String pkInsSt) {
		this.pkInsSt = pkInsSt;
	}
	public InsZsybSt getInsSt() {
		return insSt;
	}
	public void setInsSt(InsZsybSt insSt) {
		this.insSt = insSt;
	}
	public List<InsZsybStItemcate> getInsStItemcateList() {
		return insStItemcateList;
	}
	public void setInsStItemcateList(List<InsZsybStItemcate> insStItemcateList) {
		this.insStItemcateList = insStItemcateList;
	}
	public InsYdybStSnyb getInsStSnyb() {
		return insStSnyb;
	}
	public void setInsStSnyb(InsYdybStSnyb insStSnyb) {
		this.insStSnyb = insStSnyb;
	}
	public InsKsybStKsyb getInsStKsyb() {
		return insStKsyb;
	}
	public void setInsStKsyb(InsKsybStKsyb insStKsyb) {
		this.insStKsyb = insStKsyb;
	}
	public List<InsKsybStKsybJjfx> getJjfxList() {
		return jjfxList;
	}
	public void setJjfxList(List<InsKsybStKsybJjfx> jjfxList) {
		this.jjfxList = jjfxList;
	}
	public String getDateBegin() {
		return dateBegin;
	}
	public void setDateBegin(String dateBegin) {
		this.dateBegin = dateBegin;
	}
	
}
