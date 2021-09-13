package com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.TransferClass;

import java.util.List;

import com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.BottomClass.Output2305Setldetail;
import com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.BottomClass.Output2305Setlinfo;

public class Output2305 {
	
	private Output2305Setlinfo setlinfo; 
	
	private List<Output2305Setldetail> setldetail;

	public Output2305Setlinfo getSetlinfo() {
		return setlinfo;
	}

	public void setSetlinfo(Output2305Setlinfo setlinfo) {
		this.setlinfo = setlinfo;
	}

	public List<Output2305Setldetail> getSetldetail() {
		return setldetail;
	}

	public void setSetldetail(List<Output2305Setldetail> setldetail) {
		this.setldetail = setldetail;
	}
}
