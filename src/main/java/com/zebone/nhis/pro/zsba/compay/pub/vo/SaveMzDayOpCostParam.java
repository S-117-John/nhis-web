package com.zebone.nhis.pro.zsba.compay.pub.vo;

import java.util.List;

import com.zebone.nhis.pro.zsba.compay.ins.zsyb.vo.MzDayDetailsData;
import com.zebone.nhis.pro.zsba.compay.ins.zsyb.vo.MzDayInvData;

public class SaveMzDayOpCostParam {

	private String pkPv;
	private String pkPi;
	private String pkDeptSn;
	
	private List<MzDayInvData> mzDayInvDataList;
	
	private List<MzDayDetailsData> mzDayDetailsDataList;

	public List<MzDayInvData> getMzDayInvDataList() {
		return mzDayInvDataList;
	}

	public void setMzDayInvDataList(List<MzDayInvData> mzDayInvDataList) {
		this.mzDayInvDataList = mzDayInvDataList;
	}

	public List<MzDayDetailsData> getMzDayDetailsDataList() {
		return mzDayDetailsDataList;
	}

	public void setMzDayDetailsDataList(List<MzDayDetailsData> mzDayDetailsDataList) {
		this.mzDayDetailsDataList = mzDayDetailsDataList;
	}

	public String getPkPv() {
		return pkPv;
	}

	public void setPkPv(String pkPv) {
		this.pkPv = pkPv;
	}

	public String getPkPi() {
		return pkPi;
	}

	public void setPkPi(String pkPi) {
		this.pkPi = pkPi;
	}

	public String getPkDeptSn() {
		return pkDeptSn;
	}

	public void setPkDeptSn(String pkDeptSn) {
		this.pkDeptSn = pkDeptSn;
	}
	
}
