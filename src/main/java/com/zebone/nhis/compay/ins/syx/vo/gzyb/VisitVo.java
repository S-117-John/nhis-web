package com.zebone.nhis.compay.ins.syx.vo.gzyb;

import com.zebone.nhis.common.module.compay.ins.syx.gzyb.GzybVisit;
import com.zebone.nhis.common.module.compay.ins.syx.gzyb.GzybVisitCity;
import com.zebone.nhis.common.module.compay.ins.syx.gzyb.GzybVisitXnh;

public class VisitVo {

	private GzybVisit visit;// 医保登记
	private GzybVisitCity visitCity;// 医保登记-市医保
	private GzybVisitXnh visitXnh;// 医保登记-新农合

	public GzybVisit getVisit() {
		return visit;
	}

	public void setVisit(GzybVisit visit) {
		this.visit = visit;
	}

	public GzybVisitCity getVisitCity() {
		return visitCity;
	}

	public void setVisitCity(GzybVisitCity visitCity) {
		this.visitCity = visitCity;
	}

	public GzybVisitXnh getVisitXnh() {
		return visitXnh;
	}

	public void setVisitXnh(GzybVisitXnh visitXnh) {
		this.visitXnh = visitXnh;
	}
}
