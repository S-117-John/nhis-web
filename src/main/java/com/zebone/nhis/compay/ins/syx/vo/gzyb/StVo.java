package com.zebone.nhis.compay.ins.syx.vo.gzyb;

import com.zebone.nhis.common.module.compay.ins.syx.gzyb.GzybSt;
import com.zebone.nhis.common.module.compay.ins.syx.gzyb.GzybStCity;
import com.zebone.nhis.common.module.compay.ins.syx.gzyb.GzybStXnh;

public class StVo {
	private GzybSt st;// 医保结算
	private GzybStCity stCity;// 医保结算-市医保
	private GzybStXnh stXnh;// 医保结算-新农合
	public GzybSt getSt() {
		return st;
	}
	public void setSt(GzybSt st) {
		this.st = st;
	}
	public GzybStCity getStCity() {
		return stCity;
	}
	public void setStCity(GzybStCity stCity) {
		this.stCity = stCity;
	}
	public GzybStXnh getStXnh() {
		return stXnh;
	}
	public void setStXnh(GzybStXnh stXnh) {
		this.stXnh = stXnh;
	}
	
}
