package com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.TransferClass;

import java.util.List;

import com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.BottomClass.Input2403Adminfo;
import com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.BottomClass.Input2403Diseinfo;

public class Input2403{
	
	private Input2403Adminfo adminfo;
	private List<Input2403Diseinfo> diseinfo;
	public Input2403Adminfo getAdminfo() {
		return adminfo;
	}
	public void setAdminfo(Input2403Adminfo adminfo) {
		this.adminfo = adminfo;
	}
	public List<Input2403Diseinfo> getDiseinfo() {
		return diseinfo;
	}
	public void setDiseinfo(List<Input2403Diseinfo> diseinfo) {
		this.diseinfo = diseinfo;
	}
	
	
}
