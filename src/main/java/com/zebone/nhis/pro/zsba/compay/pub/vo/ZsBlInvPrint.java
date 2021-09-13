package com.zebone.nhis.pro.zsba.compay.pub.vo;

import java.util.ArrayList;
import java.util.List;

import com.zebone.nhis.common.module.bl.BlDeposit;
import com.zebone.nhis.common.module.bl.BlInvoice;
import com.zebone.nhis.common.module.bl.BlInvoiceDt;
import com.zebone.nhis.common.module.bl.BlSettle;
import com.zebone.nhis.common.module.bl.BlSettleAr;
import com.zebone.nhis.common.module.bl.BlSettleDetail;
import com.zebone.nhis.common.module.compay.ins.zsba.zsyb.InsZsybSt;
import com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.Business.InsZsbaStQg;
import com.zebone.nhis.pro.zsba.mz.ins.zsba.vo.InsQgybSt;
import com.zebone.nhis.pro.zsba.mz.ins.zsba.vo.InsSgsybSt;

public class ZsBlInvPrint {
	
	private BlSettle blSettle;
	
	private List<BlSettleDetail> blSettleDetail;
	
	private List<BlInvoice> blInvoice;
	
	private List<BlInvoiceDt> invoDt;//小票的费用明细，护士站用的，护士站不打发票
	
	private List<BlSettleAr> blArList;//欠款集合
	
	private BlDeposit blDeposit;
	
	private InsZsybSt insSt;//中山医保
	
	private InsZsbaStQg insStQg;//全国医保
	
	private String codeDept;//科室编号
	
	private String nameDept;//科室名称

	private String ipTimes;//住院次数
	
	private String amountCapital;//大写总金额(小票用的)
	
	private String amountCapitalInv;//大写总金额（发票用的）
	
	private String amtPay; //实收金额
	
	private String jzcs;//结账次数
	
	private String flagGy;//是否是公医
	
	private Double cezf;//超额自费
	
	private String jsfs;//结算方式
	
	private String dsfzf;//第三方支付
	
	private String ybzf;//医保支付

	private List<BlDeposit> blDepositList = new ArrayList<>();
	
	private InsQgybSt insQgybSt;//门诊全国医保
	
	private InsSgsybSt insSgsybSt;//门诊工伤医保
	
	public String getFlagGy() {
		return flagGy;
	}

	public void setFlagGy(String flagGy) {
		this.flagGy = flagGy;
	}

	public Double getCezf() {
		return cezf;
	}

	public void setCezf(Double cezf) {
		this.cezf = cezf;
	}

	public List<BlDeposit> getBlDepositList() {
		return blDepositList;
	}

	public void setBlDepositList(List<BlDeposit> blDepositList) {
		this.blDepositList = blDepositList;
	}

	public BlSettle getBlSettle() {
		return blSettle;
	}

	public void setBlSettle(BlSettle blSettle) {
		this.blSettle = blSettle;
	}

	public List<BlSettleDetail> getBlSettleDetail() {
		return blSettleDetail;
	}

	public void setBlSettleDetail(List<BlSettleDetail> blSettleDetail) {
		this.blSettleDetail = blSettleDetail;
	}

	public List<BlInvoice> getBlInvoice() {
		return blInvoice;
	}

	public List<BlInvoiceDt> getInvoDt() {
		return invoDt;
	}

	public void setInvoDt(List<BlInvoiceDt> invoDt) {
		this.invoDt = invoDt;
	}

	public void setBlInvoice(List<BlInvoice> blInvoice) {
		this.blInvoice = blInvoice;
	}

	public BlDeposit getBlDeposit() {
		return blDeposit;
	}

	public void setBlDeposit(BlDeposit blDeposit) {
		this.blDeposit = blDeposit;
	}

	public InsZsybSt getInsSt() {
		return insSt;
	}

	public void setInsSt(InsZsybSt insSt) {
		this.insSt = insSt;
	}

	public String getCodeDept() {
		return codeDept;
	}

	public void setCodeDept(String codeDept) {
		this.codeDept = codeDept;
	}

	public String getNameDept() {
		return nameDept;
	}

	public void setNameDept(String nameDept) {
		this.nameDept = nameDept;
	}

	public String getIpTimes() {
		return ipTimes;
	}

	public void setIpTimes(String ipTimes) {
		this.ipTimes = ipTimes;
	}

	public String getAmountCapital() {
		return amountCapital;
	}

	public void setAmountCapital(String amountCapital) {
		this.amountCapital = amountCapital;
	}

	public String getAmountCapitalInv() {
		return amountCapitalInv;
	}

	public void setAmountCapitalInv(String amountCapitalInv) {
		this.amountCapitalInv = amountCapitalInv;
	}

	public String getAmtPay() {
		return amtPay;
	}

	public void setAmtPay(String amtPay) {
		this.amtPay = amtPay;
	}

	public String getJzcs() {
		return jzcs;
	}

	public void setJzcs(String jzcs) {
		this.jzcs = jzcs;
	}

	public List<BlSettleAr> getBlArList() {
		return blArList;
	}

	public void setBlArList(List<BlSettleAr> blArList) {
		this.blArList = blArList;
	}

	public String getJsfs() {
		return jsfs;
	}

	public void setJsfs(String jsfs) {
		this.jsfs = jsfs;
	}

	public String getDsfzf() {
		return dsfzf;
	}

	public void setDsfzf(String dsfzf) {
		this.dsfzf = dsfzf;
	}

	public String getYbzf() {
		return ybzf;
	}

	public void setYbzf(String ybzf) {
		this.ybzf = ybzf;
	}

	public InsZsbaStQg getInsStQg() {
		return insStQg;
	}

	public void setInsStQg(InsZsbaStQg insStQg) {
		this.insStQg = insStQg;
	}

	public InsQgybSt getInsQgybSt() {
		return insQgybSt;
	}

	public void setInsQgybSt(InsQgybSt insQgybSt) {
		this.insQgybSt = insQgybSt;
	}

	public InsSgsybSt getInsSgsybSt() {
		return insSgsybSt;
	}

	public void setInsSgsybSt(InsSgsybSt insSgsybSt) {
		this.insSgsybSt = insSgsybSt;
	}
}
