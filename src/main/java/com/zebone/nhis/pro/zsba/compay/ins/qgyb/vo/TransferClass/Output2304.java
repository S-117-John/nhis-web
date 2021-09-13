package com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.TransferClass;

import java.util.List;

import com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.BottomClass.Output2304Setlinfo;
import com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.BottomClass.Output2304Setldetail;

public class Output2304 {
	
	private Output2304Setlinfo setlinfo; 
	
	private List<Output2304Setldetail> setldetail;

	public Output2304Setlinfo getSetlinfo() {
		return setlinfo;
	}

	public void setSetlinfo(Output2304Setlinfo setlinfo) {
		this.setlinfo = setlinfo;
	}

	public List<Output2304Setldetail> getSetldetail() {
		return setldetail;
	}

	public void setSetldetail(List<Output2304Setldetail> setldetail) {
		this.setldetail = setldetail;
	}

	
}
