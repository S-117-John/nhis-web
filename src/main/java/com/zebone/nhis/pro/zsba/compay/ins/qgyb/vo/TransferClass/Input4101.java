package com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.TransferClass;

import java.util.List;

import com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.BottomClass.Input4101Diseinfo;
import com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.BottomClass.Input4101Icuinfo;
import com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.BottomClass.Input4101Iteminfo;
import com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.BottomClass.Input4101Oprninfo;
import com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.BottomClass.Input4101Opspdiseinfo;
import com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.BottomClass.Input4101Payinfo;
import com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.BottomClass.Input4101Setlinfo;

public class Input4101 {
	
	//结算清单信息
	private Input4101Setlinfo setlinfo;
	
	//基金支付信息
	private List<Input4101Payinfo> payinfo;
	
	//门诊慢特病诊断信息
	private List<Input4101Opspdiseinfo> opspdiseinfo;
	
	//住院诊断信息
	private List<Input4101Diseinfo> diseinfo;
	
	//收费项目信息
	private List<Input4101Iteminfo> iteminfo;
	
	//手术操作信息
	private List<Input4101Oprninfo> oprninfo;
	
	//重症监护信息
	private List<Input4101Icuinfo> icuinfo;

	public Input4101Setlinfo getSetlinfo() {
		return setlinfo;
	}

	public void setSetlinfo(Input4101Setlinfo setlinfo) {
		this.setlinfo = setlinfo;
	}

	public List<Input4101Payinfo> getPayinfo() {
		return payinfo;
	}

	public void setPayinfo(List<Input4101Payinfo> payinfo) {
		this.payinfo = payinfo;
	}

	public List<Input4101Opspdiseinfo> getOpspdiseinfo() {
		return opspdiseinfo;
	}

	public void setOpspdiseinfo(List<Input4101Opspdiseinfo> opspdiseinfo) {
		this.opspdiseinfo = opspdiseinfo;
	}

	public List<Input4101Diseinfo> getDiseinfo() {
		return diseinfo;
	}

	public void setDiseinfo(List<Input4101Diseinfo> diseinfo) {
		this.diseinfo = diseinfo;
	}

	public List<Input4101Iteminfo> getIteminfo() {
		return iteminfo;
	}

	public void setIteminfo(List<Input4101Iteminfo> iteminfo) {
		this.iteminfo = iteminfo;
	}

	public List<Input4101Oprninfo> getOprninfo() {
		return oprninfo;
	}

	public void setOprninfo(List<Input4101Oprninfo> oprninfo) {
		this.oprninfo = oprninfo;
	}

	public List<Input4101Icuinfo> getIcuinfo() {
		return icuinfo;
	}

	public void setIcuinfo(List<Input4101Icuinfo> icuinfo) {
		this.icuinfo = icuinfo;
	}
	
}
