package com.zebone.nhis.compay.ins.shenzhen.vo.city;

import java.util.List;

import com.zebone.nhis.common.module.compay.ins.shenzhen.InsSzybSt;
import com.zebone.nhis.common.module.compay.ins.shenzhen.InsSzybStCity;
import com.zebone.nhis.common.module.compay.ins.shenzhen.InsSzybStCitydt;
import com.zebone.nhis.common.module.compay.ins.shenzhen.InsSzybStDiff;

public class SettleInfoMore {
    private InsSzybSt insSzybSt;
    private InsSzybStCity insSzybStCity;
    private InsSzybStDiff insSzybStDiff;
    private List<InsSzybStCitydt> insSzybStCitydtList;

    public InsSzybStDiff getInsSzybStDiff() {
        return insSzybStDiff;
    }

    public void setInsSzybStDiff(InsSzybStDiff insSzybStDiff) {
        this.insSzybStDiff = insSzybStDiff;
    }

    public InsSzybSt getInsSzybSt() {
        return insSzybSt;
    }

    public void setInsSzybSt(InsSzybSt insSzybSt) {
        this.insSzybSt = insSzybSt;
    }

    public InsSzybStCity getInsSzybStCity() {
        return insSzybStCity;
    }

    public void setInsSzybStCity(InsSzybStCity insSzybStCity) {
        this.insSzybStCity = insSzybStCity;
    }

	public List<InsSzybStCitydt> getInsSzybStCitydtList() {
		return insSzybStCitydtList;
	}

	public void setInsSzybStCitydtList(List<InsSzybStCitydt> insSzybStCitydtList) {
		this.insSzybStCitydtList = insSzybStCitydtList;
	}


}
