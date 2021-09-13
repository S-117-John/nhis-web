package com.zebone.nhis.cn.ipdw.vo;

import java.util.ArrayList;
import java.util.List;

import com.zebone.nhis.common.module.cn.ipdw.CnOpApply;

public class OpHeadSignParam {

	private  String operaType;
	private  List<CnOpApply> opApplys = new ArrayList<CnOpApply>();
	private  OpHeadSignQryParam qryParam = new OpHeadSignQryParam();
	
	public String getOperaType() {
		return operaType;
	}
	public void setOperaType(String operaType) {
		this.operaType = operaType;
	}
	public List<CnOpApply> getOpApplys() {
		return opApplys;
	}
	public void setOpApplys(List<CnOpApply> opApplys) {
		this.opApplys = opApplys;
	}
	public OpHeadSignQryParam getQryParam() {
		return qryParam;
	}
	public void setQryParam(OpHeadSignQryParam qryParam) {
		this.qryParam = qryParam;
	}
}
