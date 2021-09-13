package com.zebone.nhis.cn.ipdw.vo;

import java.util.Map;

import com.zebone.nhis.common.module.cn.ipdw.InsBear;
import com.zebone.nhis.common.module.cn.ipdw.InsOptDay;
import com.zebone.nhis.common.module.cn.ipdw.InsOptPb;

public class InsOptParam {
    private String insType;
    private String pkPv;
    private InsOptPb insOptPb;
    private InsBear insBear;
    private InsOptDay insOptDay;

	public String getInsType() {
		return insType;
	}
	public void setInsType(String insType) {
		this.insType = insType;
	}
	public String getPkPv() {
		return pkPv;
	}
	public void setPkPv(String pkPv) {
		this.pkPv = pkPv;
	}
	public InsOptPb getInsOptPb() {
		return insOptPb;
	}
	public void setInsOptPb(InsOptPb insOptPb) {
		this.insOptPb = insOptPb;
	}
	
	public InsBear getInsBear() {
		return insBear;
	}
	public void setInsBear(InsBear insBear) {
		this.insBear = insBear;
	}
	public InsOptDay getInsOptDay() {
		return insOptDay;
	}
	public void setInsOptDay(InsOptDay insOptDay) {
		this.insOptDay = insOptDay;
	}
}
