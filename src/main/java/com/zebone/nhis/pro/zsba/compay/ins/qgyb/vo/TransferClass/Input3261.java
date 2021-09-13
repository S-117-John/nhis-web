package com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.TransferClass;

import java.util.List;

import com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.BottomClass.Input3261Data;
import com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.BottomClass.Input3261Detail;

public class Input3261 {
	
	private Input3261Data data;
	private List<Input3261Detail> detail;
	public Input3261Data getData() {
		return data;
	}
	public void setData(Input3261Data data) {
		this.data = data;
	}
	public List<Input3261Detail> getDetail() {
		return detail;
	}
	public void setDetail(List<Input3261Detail> detail) {
		this.detail = detail;
	}
}
