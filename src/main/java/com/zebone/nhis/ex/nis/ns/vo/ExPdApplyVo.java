package com.zebone.nhis.ex.nis.ns.vo;

import java.util.List;

import com.zebone.nhis.common.module.ex.nis.ns.ExPdApply;

public class ExPdApplyVo extends ExPdApply{
	
	private List<ExPdApplyDetailVo> exPdApplyDetailVoList;
	
	//执行单明细
	private List<OrderOccVo> exOrdOccList;
	
	//发药及记费明细
	private List<OrderOccCgVo> cgList;

	public List<ExPdApplyDetailVo> getExPdApplyDetailVoList() {
		return exPdApplyDetailVoList;
	}

	public void setExPdApplyDetailVoList(
			List<ExPdApplyDetailVo> exPdApplyDetailVoList) {
		this.exPdApplyDetailVoList = exPdApplyDetailVoList;
	}

	public List<OrderOccVo> getExOrdOccList() {
		return exOrdOccList;
	}

	public void setExOrdOccList(List<OrderOccVo> exOrdOccList) {
		this.exOrdOccList = exOrdOccList;
	}

	public List<OrderOccCgVo> getCgList() {
		return cgList;
	}

	public void setCgList(List<OrderOccCgVo> cgList) {
		this.cgList = cgList;
	}

	

}
