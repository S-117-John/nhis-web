package com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.TransferClass;

import java.util.List;

import com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.BottomClass.Output1101Baseinfo;
import com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.BottomClass.Output1101Idetinfo;
import com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.BottomClass.Output1101Insuinfo;

public class OutputVoid {
	
	private Output1101Baseinfo baseinfo; //基本信息
	
	private List<Output1101Insuinfo> insuinfo; //参保信息列表
	
	private List<Output1101Idetinfo> idetinfo; //身份信息列表

	public Output1101Baseinfo getBaseinfo() {
		return baseinfo;
	}

	public void setBaseinfo(Output1101Baseinfo baseinfo) {
		this.baseinfo = baseinfo;
	}

	public List<Output1101Insuinfo> getInsuinfo() {
		return insuinfo;
	}

	public void setInsuinfo(List<Output1101Insuinfo> insuinfo) {
		this.insuinfo = insuinfo;
	}

	public List<Output1101Idetinfo> getIdetinfo() {
		return idetinfo;
	}

	public void setIdetinfo(List<Output1101Idetinfo> idetinfo) {
		this.idetinfo = idetinfo;
	}
	
	
}
