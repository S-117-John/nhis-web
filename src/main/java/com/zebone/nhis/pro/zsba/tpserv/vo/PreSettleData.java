package com.zebone.nhis.pro.zsba.tpserv.vo;

import java.util.List;


/**
 * 预结算返回值
 *  
 * @author zhengrj
 * @date 2017年9月25日
 *
 */
public class PreSettleData {
	
	/** 护工单价 */
	private double hgdj;
	
	/** 患者住院天数 */
	private int hzzyts;
	
	/** 护工费 */
	private double hgf;
	
	/** 项目出租信息 */
	private List<TpServItemRent> rentList;
	
	/** 总费用 */
	private double totalCost;
	
	/** 总押金 */
	private double totalCashPledge;
	
	/** 需缴纳金额 */
	private double payAmount;
	
	/** 是否需要缴非医疗费用，0:不需要 1:需要  区分总费用为0和不需要缴费 */
	private String ifPay;
	
	public double getHgdj() {
		return hgdj;
	}

	public void setHgdj(double hgdj) {
		this.hgdj = hgdj;
	}

	public int getHzzyts() {
		return hzzyts;
	}

	public void setHzzyts(int hzzyts) {
		this.hzzyts = hzzyts;
	}

	public double getHgf() {
		return hgf;
	}

	public void setHgf(double hgf) {
		this.hgf = hgf;
	}

	public List<TpServItemRent> getRentList() {
		return rentList;
	}

	public void setRentList(List<TpServItemRent> rentList) {
		this.rentList = rentList;
	}

	public double getTotalCost() {
		return totalCost;
	}

	public void setTotalCost(double totalCost) {
		this.totalCost = totalCost;
	}

	public double getTotalCashPledge() {
		return totalCashPledge;
	}

	public void setTotalCashPledge(double totalCashPledge) {
		this.totalCashPledge = totalCashPledge;
	}

	public double getPayAmount() {
		return payAmount;
	}

	public void setPayAmount(double payAmount) {
		this.payAmount = payAmount;
	}

	public String getIfPay() {
		return ifPay;
	}

	public void setIfPay(String ifPay) {
		this.ifPay = ifPay;
	}
}
