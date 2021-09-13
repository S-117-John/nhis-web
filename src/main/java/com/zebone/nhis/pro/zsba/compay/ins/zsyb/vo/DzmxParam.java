package com.zebone.nhis.pro.zsba.compay.ins.zsyb.vo;

import java.util.Date;
import java.util.List;

/**
 * 中山医保对账明细参数
 * @author zrj
 *
 */
public class DzmxParam {

	private String tjlb; //统计类别
	private Date ksrq; //开始日期
	private Date zzrq; //终止日期
	private Double ylfyzesSumDz;//医保对账总费用
	private Double ylfyzesSumYb; //医保结算总费用
	private List<InsZsBaYbDzmx> dzmxList; //对账明细list
	private List<InsZsBaYbSt> stList;//医院有差异的数据
	public String getTjlb() {
		return tjlb;
	}
	public void setTjlb(String tjlb) {
		this.tjlb = tjlb;
	}
	public Date getKsrq() {
		return ksrq;
	}
	public void setKsrq(Date ksrq) {
		this.ksrq = ksrq;
	}
	public Date getZzrq() {
		return zzrq;
	}
	public void setZzrq(Date zzrq) {
		this.zzrq = zzrq;
	}
	public Double getYlfyzesSumDz() {
		return ylfyzesSumDz;
	}
	public void setYlfyzesSumDz(Double ylfyzesSumDz) {
		this.ylfyzesSumDz = ylfyzesSumDz;
	}

	public Double getYlfyzesSumYb() {
		return ylfyzesSumYb;
	}
	public void setYlfyzesSumYb(Double ylfyzesSumYb) {
		this.ylfyzesSumYb = ylfyzesSumYb;
	}

	public List<InsZsBaYbDzmx> getDzmxList() {
		return dzmxList;
	}
	public void setDzmxList(List<InsZsBaYbDzmx> dzmxList) {
		this.dzmxList = dzmxList;
	}
	public List<InsZsBaYbSt> getStList() {
		return stList;
	}
	public void setStList(List<InsZsBaYbSt> stList) {
		this.stList = stList;
	}
	
}
