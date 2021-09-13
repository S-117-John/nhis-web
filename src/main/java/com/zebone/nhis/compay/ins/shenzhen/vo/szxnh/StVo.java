package com.zebone.nhis.compay.ins.shenzhen.vo.szxnh;

import com.zebone.nhis.common.module.compay.ins.shenzhen.xnh.SzybSt;
import com.zebone.nhis.common.module.compay.ins.shenzhen.xnh.SzybStCity;
import com.zebone.nhis.common.module.compay.ins.shenzhen.xnh.SzybStXnh;

public class StVo {
	private SzybSt st;// 医保结算
	private SzybStCity stCity;// 医保结算-市医保
	private SzybStXnh stXnh;// 医保结算-新农合
	public SzybSt getSt() {
		return st;
	}
	public void setSt(SzybSt st) {
		this.st = st;
	}
	public SzybStCity getStCity() {
		return stCity;
	}
	public void setStCity(SzybStCity stCity) {
		this.stCity = stCity;
	}
	public SzybStXnh getStXnh() {
		return stXnh;
	}
	public void setStXnh(SzybStXnh stXnh) {
		this.stXnh = stXnh;
	}
	
}
