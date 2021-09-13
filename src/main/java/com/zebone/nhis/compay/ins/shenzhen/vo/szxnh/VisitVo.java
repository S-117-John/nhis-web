package com.zebone.nhis.compay.ins.shenzhen.vo.szxnh;

import com.zebone.nhis.common.module.compay.ins.shenzhen.szyb.SzybVisit;
import com.zebone.nhis.common.module.compay.ins.shenzhen.szyb.SzybVisitCity;
import com.zebone.nhis.common.module.compay.ins.shenzhen.szyb.SzybVisitXnh;

public class VisitVo {

	private SzybVisit visit;// 医保登记
	private SzybVisitCity visitCity;// 医保登记-市医保
	private SzybVisitXnh visitXnh;// 医保登记-新农合

	public SzybVisit getVisit() {
		return visit;
	}

	public void setVisit(SzybVisit visit) {
		this.visit = visit;
	}

	public SzybVisitCity getVisitCity() {
		return visitCity;
	}

	public void setVisitCity(SzybVisitCity visitCity) {
		this.visitCity = visitCity;
	}

	public SzybVisitXnh getVisitXnh() {
		return visitXnh;
	}

	public void setVisitXnh(SzybVisitXnh visitXnh) {
		this.visitXnh = visitXnh;
	}
}
