package com.zebone.nhis.pro.zsba.compay.daysettle.pub.vo;

import java.util.List;

public class ZsGroupDayStSum {
	private List<ZsGroupDaySt> zsGroupDayStList;
	private Double amtSettleSum;
	private Double amtPrepSum;
	public List<ZsGroupDaySt> getZsGroupDayStList() {
		return zsGroupDayStList;
	}
	public void setZsGroupDayStList(List<ZsGroupDaySt> zsGroupDayStList) {
		this.zsGroupDayStList = zsGroupDayStList;
	}
	public Double getAmtSettleSum() {
		return amtSettleSum;
	}
	public void setAmtSettleSum(Double amtSettleSum) {
		this.amtSettleSum = amtSettleSum;
	}
	public Double getAmtPrepSum() {
		return amtPrepSum;
	}
	public void setAmtPrepSum(Double amtPrepSum) {
		this.amtPrepSum = amtPrepSum;
	}
}
