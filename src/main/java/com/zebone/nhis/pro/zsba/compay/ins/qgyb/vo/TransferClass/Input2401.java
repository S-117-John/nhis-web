package com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.TransferClass;

import java.util.List;

import com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.BottomClass.Input2401Diseinfo;
import com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.BottomClass.Input2401Mdtrtinfo;

public class Input2401{
	
	private Input2401Mdtrtinfo mdtrtinfo;
	private List<Input2401Diseinfo> diseinfo;
	public Input2401Mdtrtinfo getMdtrtinfo() {
		return mdtrtinfo;
	}
	public void setMdtrtinfo(Input2401Mdtrtinfo mdtrtinfo) {
		this.mdtrtinfo = mdtrtinfo;
	}
	public List<Input2401Diseinfo> getDiseinfo() {
		return diseinfo;
	}
	public void setDiseinfo(List<Input2401Diseinfo> diseinfo) {
		this.diseinfo = diseinfo;
	}
}
