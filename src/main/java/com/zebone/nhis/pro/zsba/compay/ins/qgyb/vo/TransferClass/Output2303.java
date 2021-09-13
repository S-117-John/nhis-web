package com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.TransferClass;

import java.util.List;

import com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.BottomClass.Output2303Setlinfo;
import com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.BottomClass.Output2303Setldetail;

public class Output2303 {
	
	private Output2303Setlinfo setlinfo; 
	
	private List<Output2303Setldetail> setldetail;

	public Output2303Setlinfo getSetlinfo() {
		return setlinfo;
	}

	public void setSetlinfo(Output2303Setlinfo setlinfo) {
		this.setlinfo = setlinfo;
	}

	public List<Output2303Setldetail> getSetldetail() {
		return setldetail;
	}

	public void setSetldetail(List<Output2303Setldetail> setldetail) {
		this.setldetail = setldetail;
	}

	

	
}
