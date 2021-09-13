package com.zebone.nhis.compay.pub.syx.vo;

import java.util.List;
import java.util.Map;

public class CostForecastVo {

	/* 未结预交金 */
	private double amtPrep;
	/* 估计自付 */
	private double amountPaid;
	/* 总费用 */
	private double amtSum;
	/* 未结费用 */
	private double amtNosettle;
	/* 内部医保患者自付 */
	private double amtPi;
	/* 可用余额 */
	private double amtAcc;
	/* 医保预测 */
	private double amtHp;
	/* 是否有余额 */
	private boolean isArrearage;
	/* 预测医保支付 */
	private double amtHpPay;
	/* 预测可用余额 */
	private double amtHpUsable;
	
	/* 绿色通道金额 */
	private double amtIpAcc;
	/* 纳入定额 */
	private double quotaAmt;
	/* 欠费限额 */
	private double arrearageAmt;

	/* 在途费用金额 */
	private double ztfee;
	/* 预交金余额 */
	private double amtPrepBalance;
	/* 科室限额 */
	private double deptlimitamt;

	/* 剩余限额 */
	private double surpluslimitamt;

	private List<CostZtFeeVo> ztfeelist; // 在途费用明细

	public double getAmtSum() {
		return amtSum;
	}

	public void setAmtSum(double amtSum) {
		this.amtSum = amtSum;
	}

	public double getAmtNosettle() {
		return amtNosettle;
	}

	public void setAmtNosettle(double amtNosettle) {
		this.amtNosettle = amtNosettle;
	}

	public double getAmtPi() {
		return amtPi;
	}

	public void setAmtPi(double amtPi) {
		this.amtPi = amtPi;
	}

	public double getAmtPrep() {
		return amtPrep;
	}

	public void setAmtPrep(double amtPrep) {
		this.amtPrep = amtPrep;
	}

	public double getAmountPaid() {
		return amountPaid;
	}

	public void setAmountPaid(double amountPaid) {
		this.amountPaid = amountPaid;
	}

	public double getAmtAcc() {
		return amtAcc;
	}

	public void setAmtAcc(double amtAcc) {
		this.amtAcc = amtAcc;
	}

	public double getAmtHp() {
		return amtHp;
	}

	public void setAmtHp(double amtHp) {
		this.amtHp = amtHp;
	}

	public boolean isArrearage() {
		return isArrearage;
	}

	public void setArrearage(boolean isArrearage) {
		this.isArrearage = isArrearage;
	}

	public double getAmtIpAcc() {
		return amtIpAcc;
	}

	public void setAmtIpAcc(double amtIpAcc) {
		this.amtIpAcc = amtIpAcc;
	}

	public double getQuotaAmt() {
		return quotaAmt;
	}

	public void setQuotaAmt(double quotaAmt) {
		this.quotaAmt = quotaAmt;
	}

	public double getArrearageAmt() {
		return arrearageAmt;
	}

	public void setArrearageAmt(double arrearageAmt) {
		this.arrearageAmt = arrearageAmt;
	}

	public double getZtfee() {
		return ztfee;
	}

	public void setZtfee(double ztfee) {
		this.ztfee = ztfee;
	}

	public double getAmtPrepBalance() {
		return amtPrepBalance;
	}
	
	public double getAmtHpPay() {
		return amtHpPay;
	}

	public void setAmtHpPay(double amtHpPay) {
		this.amtHpPay = amtHpPay;
	}

	public double getAmtHpUsable() {
		return amtHpUsable;
	}

	public void setAmtHpUsable(double amtHpUsable) {
		this.amtHpUsable = amtHpUsable;
	}

	public void setAmtPrepBalance(double amtPrepBalance) {
		this.amtPrepBalance = amtPrepBalance;
	}

	public double getDeptlimitamt() {
		return deptlimitamt;
	}

	public void setDeptlimitamt(double deptlimitamt) {
		this.deptlimitamt = deptlimitamt;
	}

	public double getSurpluslimitamt() {
		return surpluslimitamt;
	}

	public void setSurpluslimitamt(double surpluslimitamt) {
		this.surpluslimitamt = surpluslimitamt;
	}

	public List<CostZtFeeVo> getZtfeelist() {
		return ztfeelist;
	}

	public void setZtfeelist(List<CostZtFeeVo> ztfeelist) {
		this.ztfeelist = ztfeelist;
	}
}
