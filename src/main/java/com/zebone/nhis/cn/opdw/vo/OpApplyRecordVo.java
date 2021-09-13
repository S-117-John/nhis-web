package com.zebone.nhis.cn.opdw.vo;

import java.util.ArrayList;
import java.util.List;

import com.zebone.nhis.bl.opcg.vo.OpCgCnOrder;
import com.zebone.nhis.common.module.cn.ipdw.CnLabApply;
import com.zebone.nhis.common.module.cn.ipdw.CnRisApply;

public class OpApplyRecordVo {
	private List<OpCgCnOrder> opApply = new ArrayList<OpCgCnOrder>();
	private List<OpCgCnOrder> opApplyDel = new ArrayList<OpCgCnOrder>();
	private List<CnRisApply> opRis = new ArrayList<CnRisApply>();
	private List<CnLabApply> opLab = new ArrayList<CnLabApply>();

	public List<CnRisApply> getOpRis() {
		return opRis;
	}
	public void setOpRis(List<CnRisApply> opRis) {
		this.opRis = opRis;
	}
	public List<CnLabApply> getOpLab() {
		return opLab;
	}
	public void setOpLab(List<CnLabApply> opLab) {
		this.opLab = opLab;
	}
	public List<OpCgCnOrder> getOpApply() {
		return opApply;
	}
	public void setOpApply(List<OpCgCnOrder> opApply) {
		this.opApply = opApply;
	}
	public List<OpCgCnOrder> getOpApplyDel() {
		return opApplyDel;
	}
	public void setOpApplyDel(List<OpCgCnOrder> opApplyDel) {
		this.opApplyDel = opApplyDel;
	}

}
