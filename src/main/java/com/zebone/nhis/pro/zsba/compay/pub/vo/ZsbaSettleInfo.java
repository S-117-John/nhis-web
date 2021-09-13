package com.zebone.nhis.pro.zsba.compay.pub.vo;

import java.math.BigDecimal;
import java.util.List;

import com.zebone.nhis.bl.pub.vo.InvInfoVo;
import com.zebone.nhis.common.module.pay.BlExtPay;
import com.zebone.nhis.pro.zsba.compay.ins.ksyb.vo.InsZsKsybStKsyb;
import com.zebone.nhis.pro.zsba.compay.ins.ksyb.vo.InsZsKsybStKsybJjfx;
import com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.Business.InsZsbaStQg;
import com.zebone.nhis.pro.zsba.compay.ins.ydyb.vo.InsZsYdybStSnyb;
import com.zebone.nhis.pro.zsba.compay.ins.zsyb.vo.InsZsBaYbSt;
import com.zebone.nhis.pro.zsba.compay.ins.zsyb.vo.InsZsBaYbStItemcate;

public class ZsbaSettleInfo {

	private BigDecimal  amountPrep;//患者预交金合计
	private BigDecimal  amountSt;//患者费用合计
	private BigDecimal  amountPi;//患者自付合计
	private BigDecimal  amountInsu;//医保支付
	private BigDecimal  amountUnit;//单位支付
	private BigDecimal  amountInsuThird;//第三方医保接口返回的医保支付
	private BigDecimal  amountAcc;//患者账户支付
	private String pkAcc;
	private String pkPv;
	private String euStresult;
	private String dateBegin;//结算开始日期
	private String dateEnd;
	private String euSttype;	
	private List<InvInfoVo> invos;
	private  ZsbaBlDeposit deposit;
	private List<ZsbaBlDeposit> depoList;
	private  ZsbaBlDeposit depositAcc;
	private  BlExtPay blExtPay;  //第三方支付交易数据
	private String pkInsSt; //医保结算主键
	private InsZsBaYbSt insSt;//外部医保-中山医保住院结算
	private List<InsZsBaYbStItemcate>insStItemcateList;//外部医保-中山医保结算返回项目分类
	private InsZsYdybStSnyb insStSnyb; //省内异地医保结算数据
	private InsZsKsybStKsyb insStKsyb; //跨省异地医保结算数据
	private List<InsZsKsybStKsybJjfx> jjfxList; //外部医保-跨省医保住院结算基金分项 
	private InsZsbaStQg insStQgyb;//全国医保结算数据
	private List<String> pkDepoList;//本次结算选择的预交金信息
	private String dtDeptType;
    // 特殊项目结算明细主键
    private List<String> pkCgips;
    // 标志是否是特殊项目结算
    public String flagSpItemCg;
    //特诊是否合并打印发票
    public String flagHbPrint;
    
    public String pkSettle;//预结再结使用(灵璧)
    
    public String flagPrint;//是否打印纸质票据
    
    private String pkPayer;//付款方主键，科研结算用
    
    private String pkPosTr;//社保三代卡个账交易记录主键
    
    private BigDecimal  amountDisc;//优惠金额
    
    private String note;//备注  目前用于存储优惠原因
    
    private String ZsbaDzpzJshlInfo;//电子凭证入参
    
    private PayPosTr payPosTr;//社保三代卡扣个账实体类，不过这里目前用来存储部分医保电子凭证结算回流的入参  从前端扫描电子凭证获取
    
    private String dtFayplgitem;//计生项目编码
    
    private String addrCurTown;//所在镇区：计划生育结算时需要填写这个
    
    private String pkDept;//科室主键，分科结算用的
    
    private String pkCgipsStr;//明细主键(多个以逗号分隔)，明细结算用的
    
    private BigDecimal zdbjdxAmt;//重点保健对象金额
    
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
	public List<ZsbaBlDeposit> getDepoList() {
		return depoList;
	}
	public void setDepoList(List<ZsbaBlDeposit> depoList) {
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
	public ZsbaBlDeposit getDepositAcc() {
		return depositAcc;
	}
	public void setDepositAcc(ZsbaBlDeposit depositAcc) {
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
	public ZsbaBlDeposit getDeposit() {
		return deposit;
	}
	public void setDeposit(ZsbaBlDeposit deposit) {
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
	public InsZsBaYbSt getInsSt() {
		return insSt;
	}
	public void setInsSt(InsZsBaYbSt insSt) {
		this.insSt = insSt;
	}
	public List<InsZsBaYbStItemcate> getInsStItemcateList() {
		return insStItemcateList;
	}
	public void setInsStItemcateList(List<InsZsBaYbStItemcate> insStItemcateList) {
		this.insStItemcateList = insStItemcateList;
	}
	public InsZsYdybStSnyb getInsStSnyb() {
		return insStSnyb;
	}
	public void setInsStSnyb(InsZsYdybStSnyb insStSnyb) {
		this.insStSnyb = insStSnyb;
	}
	public InsZsKsybStKsyb getInsStKsyb() {
		return insStKsyb;
	}
	public void setInsStKsyb(InsZsKsybStKsyb insStKsyb) {
		this.insStKsyb = insStKsyb;
	}
	public List<InsZsKsybStKsybJjfx> getJjfxList() {
		return jjfxList;
	}
	public void setJjfxList(List<InsZsKsybStKsybJjfx> jjfxList) {
		this.jjfxList = jjfxList;
	}
	public String getDateBegin() {
		return dateBegin;
	}
	public void setDateBegin(String dateBegin) {
		this.dateBegin = dateBegin;
	}
	public String getDtDeptType() {
		return dtDeptType;
	}
	public void setDtDeptType(String dtDeptType) {
		this.dtDeptType = dtDeptType;
	}
	public String getPkPayer() {
		return pkPayer;
	}
	public void setPkPayer(String pkPayer) {
		this.pkPayer = pkPayer;
	}
	public String getPkPosTr() {
		return pkPosTr;
	}
	public void setPkPosTr(String pkPosTr) {
		this.pkPosTr = pkPosTr;
	}
	public BigDecimal getAmountDisc() {
		return amountDisc;
	}
	public void setAmountDisc(BigDecimal amountDisc) {
		this.amountDisc = amountDisc;
	}
	public InsZsbaStQg getInsStQgyb() {
		return insStQgyb;
	}
	public void setInsStQgyb(InsZsbaStQg insStQgyb) {
		this.insStQgyb = insStQgyb;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public String getZsbaDzpzJshlInfo() {
		return ZsbaDzpzJshlInfo;
	}
	public void setZsbaDzpzJshlInfo(String zsbaDzpzJshlInfo) {
		ZsbaDzpzJshlInfo = zsbaDzpzJshlInfo;
	}
	public PayPosTr getPayPosTr() {
		return payPosTr;
	}
	public void setPayPosTr(PayPosTr payPosTr) {
		this.payPosTr = payPosTr;
	}
	public String getDtFayplgitem() {
		return dtFayplgitem;
	}
	public void setDtFayplgitem(String dtFayplgitem) {
		this.dtFayplgitem = dtFayplgitem;
	}
	public String getAddrCurTown() {
		return addrCurTown;
	}
	public void setAddrCurTown(String addrCurTown) {
		this.addrCurTown = addrCurTown;
	}
	public String getPkDept() {
		return pkDept;
	}
	public void setPkDept(String pkDept) {
		this.pkDept = pkDept;
	}
	public String getPkCgipsStr() {
		return pkCgipsStr;
	}
	public void setPkCgipsStr(String pkCgipsStr) {
		this.pkCgipsStr = pkCgipsStr;
	}
	public BigDecimal getZdbjdxAmt() {
		return zdbjdxAmt;
	}
	public void setZdbjdxAmt(BigDecimal zdbjdxAmt) {
		this.zdbjdxAmt = zdbjdxAmt;
	}
	
}
