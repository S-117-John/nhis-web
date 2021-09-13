package com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.TransferClass;

import java.util.List;

import com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.BottomClass.Input2402Diseinfo;
import com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.BottomClass.Input2402Dscginfo;

public class Input2402{
	
	private Input2402Dscginfo dscginfo;
	private List<Input2402Diseinfo> diseinfo;
	public Input2402Dscginfo getDscginfo() {
		return dscginfo;
	}
	public void setDscginfo(Input2402Dscginfo dscginfo) {
		this.dscginfo = dscginfo;
	}
	public List<Input2402Diseinfo> getDiseinfo() {
		return diseinfo;
	}
	public void setDiseinfo(List<Input2402Diseinfo> diseinfo) {
		this.diseinfo = diseinfo;
	}
}
