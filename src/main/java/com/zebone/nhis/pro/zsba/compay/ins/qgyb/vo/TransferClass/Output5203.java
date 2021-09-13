package com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.TransferClass;

import java.util.List;

import com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.BottomClass.Output5203Setldetail;
import com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.BottomClass.Output5203Setlinfo;

public class Output5203 {
	
	private Output5203Setlinfo setlinfo; 
	private List<Output5203Setldetail> setldetail;
	public Output5203Setlinfo getSetlinfo() {
		return setlinfo;
	}
	public void setSetlinfo(Output5203Setlinfo setlinfo) {
		this.setlinfo = setlinfo;
	}
	public List<Output5203Setldetail> getSetldetail() {
		return setldetail;
	}
	public void setSetldetail(List<Output5203Setldetail> setldetail) {
		this.setldetail = setldetail;
	}
}
